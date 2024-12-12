package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.findByAddressInfoOrThrow
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.request.UpdateArticleRequest
import com.numberone.daepiro.domain.community.dto.request.UpsertArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleDetailResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleLikeResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.dto.response.CommentResponse
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.event.ArticleFileUploadEvent
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.article.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.comment.CommentRepository
import com.numberone.daepiro.domain.file.entity.FileDocumentType
import com.numberone.daepiro.domain.file.model.RawFile
import com.numberone.daepiro.domain.file.repository.FileRepository
import com.numberone.daepiro.domain.user.entity.UserLike
import com.numberone.daepiro.domain.user.entity.UserLikeDocumentType
import com.numberone.daepiro.domain.user.repository.UserLikeRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.domain.user.repository.isLikedArticle
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val fileRepository: FileRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val addressRepository: AddressRepository,
    private val commentRepository: CommentRepository,
    private val userLikeRepository: UserLikeRepository,
) {

    @Transactional
    fun createOne(
        request: UpsertArticleRequest,
        attachFileList: List<MultipartFile>?,
        userId: Long,
    ): ArticleSimpleResponse {
        val author = userRepository.findByIdOrThrow(userId)

        val article = articleRepository.save(
            Article.of(
                title = request.title,
                body = request.body,
                type = request.articleType,
                category = request.articleCategory,
                visibility = request.visibility,
                authUser = author,
            )
        )

        attachFileList?.let { files ->
            eventPublisher.publishEvent(ArticleFileUploadEvent(article.id!!, files.map { RawFile.of(it) }))
        }

        return ArticleSimpleResponse.from(
            article = article,
        )
    }

    @Transactional
    fun upsertOne(
        id: Long,
        request: UpdateArticleRequest,
        attachFileList: List<MultipartFile>?,
    ): ArticleSimpleResponse {
        val article = articleRepository.findByIdOrThrow(id)

        article.update(
            title = request.title ?: article.title,
            body = request.body ?: article.body,
            type = request.articleType ?: article.type,
            category = request.articleCategory ?: article.category,
            isLocationVisible = request.visibility ?: article.isLocationVisible,
        )

        attachFileList?.let { files ->
            // 해당 아티클에 매핑된 파일을 모두 제거하고
            fileRepository.deleteAllByDocumentTypeAndDocumentId(
                documentType = FileDocumentType.ARTICLE,
                documentId = article.id!!,
            )
            // 새로 요청온 파일을 적용함
            eventPublisher.publishEvent(ArticleFileUploadEvent(article.id!!, files.map { RawFile.of(it) }))
        }

        return ArticleSimpleResponse.from(article)
    }

    @Transactional(readOnly = true)
    fun getOne(id: Long): ArticleDetailResponse {
        val article = articleRepository.findByIdOrThrow(id)
        val files = fileRepository.findAllByDocumentTypeAndDocumentId(
            documentType = FileDocumentType.ARTICLE,
            article.id!!
        )

        val comments = commentRepository.findCommentsByDocumentId(article.id!!)

        val roots = mutableListOf<CommentResponse>()
        val commentById = comments.associateBy { it.id }

        comments.forEach { it ->
            val comment = commentById[it.id] ?: return@forEach
            if (comment.parentCommentId != null) {
                val parentComment = commentById[comment.parentCommentId]
                parentComment?.children?.add(comment)
                return@forEach
            }
            roots.add(comment)
        }


        return ArticleDetailResponse.of(
            article = article,
            files = files,
            comments = roots,
        )
    }

    @Transactional(readOnly = true)
    fun getMulti(
        request: GetArticleRequest,
    ): Slice<ArticleListResponse> {
        val address = request.address?.let {
            addressRepository.findByAddressInfoOrThrow(AddressInfo.from(it))
        }

        val slice = articleRepository.getArticles(
            request = request,
            siDo = address?.siDo,
            siGunGu = address?.siGunGu,
        )

        val files = fileRepository.findAllByDocumentTypeAndDocumentIdIn(
            documentType = FileDocumentType.ARTICLE,
            documentIds = slice.map { it.id }.distinct(),
        )

        slice.forEach { articleResponse ->
            val relatedFile = files.find { it.documentId == articleResponse.id }
            relatedFile?.let {
                articleResponse.updatePreviewImageUrl(it.path)
            }
        }

        return slice
    }

    @Transactional
    fun like(
        userId: Long,
        articleId: Long,
    ): ArticleLikeResponse {
        val article = articleRepository.findByIdOrThrow(articleId)

        when (userLikeRepository.isLikedArticle(userId, article)) {
            true -> {
                article.decreaseLikeCount()
                userLikeRepository.deleteByUserIdAndDocumentTypeAndDocumentId(
                    userId = userId,
                    documentType = UserLikeDocumentType.from(article.type),
                    documentId = article.id!!,
                )
            };
            false -> {
                article.increaseLikeCount()
                userLikeRepository.save(
                    UserLike(
                        user = userRepository.findByIdOrThrow(userId),
                        documentType = UserLikeDocumentType.from(article.type),
                        documentId = article.id!!,
                    )
                )
            };
        }
        return ArticleLikeResponse.from(article)
    }
}

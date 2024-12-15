package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.findByAddressInfoOrThrow
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.request.ReportRequest
import com.numberone.daepiro.domain.community.dto.request.UpdateArticleRequest
import com.numberone.daepiro.domain.community.dto.request.UpsertArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleDetailResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleLikeResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.dto.response.CommentResponse
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ReportedDocument
import com.numberone.daepiro.domain.community.event.ArticleAddressMappingEvent
import com.numberone.daepiro.domain.community.event.ArticleFileUploadEvent
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.article.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.comment.CommentRepository
import com.numberone.daepiro.domain.community.repository.reported.ReportedDocumentRepository
import com.numberone.daepiro.domain.community.repository.reported.isAlreadyReportedArticle
import com.numberone.daepiro.domain.community.repository.verified.UserAddressVerifiedRepository
import com.numberone.daepiro.domain.file.entity.FileDocumentType
import com.numberone.daepiro.domain.file.model.RawFile
import com.numberone.daepiro.domain.file.repository.FileRepository
import com.numberone.daepiro.domain.user.entity.UserLike
import com.numberone.daepiro.domain.user.entity.UserLikeDocumentType
import com.numberone.daepiro.domain.user.repository.UserLikeRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findAllLikedArticleId
import com.numberone.daepiro.domain.user.repository.findAllLikedCommentId
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.domain.user.repository.isLikedArticle
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
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
    private val userAddressVerifyRepository: UserAddressVerifiedRepository,
    private val reportedDocumentRepository: ReportedDocumentRepository,
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

        if (request.visibility) {
            validateLocationInfo(request)
            eventPublisher.publishEvent(
                ArticleAddressMappingEvent(
                    article.id!!,
                    request.longitude!!,
                    request.latitude!!,
                )
            )
        }

        return ArticleSimpleResponse.from(
            article = article,
        )
    }

    private fun validateLocationInfo(request: UpsertArticleRequest) {
        if (request.latitude == null && request.longitude == null) {
            throw CustomException(
                CustomErrorContext.INVALID_VALUE,
                additionalDetail = "visibility 가 true 이면 위경도를 nulㅣ 이 아닌 값으로 요청해주세요."
            )
        }
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
    fun getOne(
        articleId: Long,
        userId: Long,
    ): ArticleDetailResponse {
        val article = articleRepository.findByIdOrThrow(articleId)
        val files = fileRepository.findAllByDocumentTypeAndDocumentId(
            documentType = FileDocumentType.ARTICLE,
            article.id!!
        )

        val comments = commentRepository.findCommentsByDocumentId(article.id!!)

        val verifiedAddressIdMapOfAuthorId =
            userAddressVerifyRepository.findAllByUserIdIn(comments.mapNotNull { it.author?.userId })
                .groupBy { it.userId }
                .mapValues { it -> it.value.map { it.addressId } }

        val roots = mutableListOf<CommentResponse>()
        val commentById = comments.associateBy { it.id }

        val likedCommentIdSet = userLikeRepository.findAllLikedCommentId(userId)
        val articleAddress = article.address
        comments.forEach {
            val comment = commentById[it.id] ?: return@forEach
            if (comment.parentCommentId != null) {
                val parentComment = commentById[comment.parentCommentId]
                parentComment?.children?.add(comment)
                return@forEach
            }
            if (likedCommentIdSet.contains(comment.id)) {
                comment.isLiked = true
            }
            val commentAuthor = comment.author
            if (articleAddress != null && commentAuthor != null) {
                val verifiedAddresses = verifiedAddressIdMapOfAuthorId[commentAuthor.userId]
                commentAuthor.isVerified = verifiedAddresses?.contains(articleAddress.id) ?: false
            }
            roots.add(comment)
        }

        val isLikedArticle = userLikeRepository.existsByUserIdAndDocumentTypeAndDocumentId(
            userId = userId,
            documentType = UserLikeDocumentType.from(article.type),
            documentId = article.id!!,
        )

        val isVerifiedAuthor = article.authUser?.let { author ->
            val authorVerifiedAddressIds = userAddressVerifyRepository.findAllByUserId(author.id!!)
                .map { it.addressId }
            return@let authorVerifiedAddressIds.contains(article.address?.id)
        } ?: false

        return ArticleDetailResponse.of(
            article = article,
            isLiked = isLikedArticle,
            files = files,
            comments = roots,
            isVerified = isVerifiedAuthor,
        )
    }

    @Transactional(readOnly = true)
    fun getMulti(
        request: GetArticleRequest,
        userId: Long,
    ): Slice<ArticleListResponse> {
        val address = request.address?.let {
            addressRepository.findByAddressInfoOrThrow(AddressInfo.from(it))
        }

        val slice = articleRepository.getArticles(
            request = request,
            siDo = address?.siDo,
            siGunGu = address?.siGunGu,
        )

        val authorMapOfArticleId = slice.content.associate { it.id to it.authorUser }
        val verifiedAddressIdMapOfAuthorId =
            userAddressVerifyRepository.findAllByUserIdIn(authorMapOfArticleId.mapNotNull { it.value?.userId })
                .groupBy { it.userId }
                .mapValues { it -> it.value.map { it.addressId } }


        val files = fileRepository.findAllByDocumentTypeAndDocumentIdIn(
            documentType = FileDocumentType.ARTICLE,
            documentIds = slice.map { it.id }.distinct(),
        )

        val likedArticleIdSet = userLikeRepository.findAllLikedArticleId(userId)

        slice.forEach { article ->
            val relatedFile = files.find { it.documentId == article.id }
            relatedFile?.let {
                article.updatePreviewImageUrl(it.path)
            }
            if (likedArticleIdSet.contains(article.id)) {
                article.updateIsLiked(true)
            }
            val author = article.authorUser
            val articleAddress = article.address

            if (author != null && articleAddress != null) {
                val verifiedAuthorAddresses = verifiedAddressIdMapOfAuthorId[author.userId]
                author.isVerified = verifiedAuthorAddresses?.contains(articleAddress.addressId) ?: false
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

    @Transactional
    fun report(
        userId: Long,
        articleId: Long,
        request: ReportRequest,
    ) {
        val user = userRepository.findByIdOrThrow(userId)
        val article = articleRepository.findByIdOrThrow(articleId)

        if (reportedDocumentRepository.isAlreadyReportedArticle(user, article)) {
            throw CustomException(CustomErrorContext.INVALID_VALUE, "해당 유저에 의해 이미 신고된 게시물입니다.")
        }

        reportedDocumentRepository.save(
            ReportedDocument.from(
                article = article,
                user = user,
                type = request.type,
                detail = request.detail,
                email = request.email,
            )
        )

        articleRepository.save(
            article.increaseReportCount()
        )
    }
}

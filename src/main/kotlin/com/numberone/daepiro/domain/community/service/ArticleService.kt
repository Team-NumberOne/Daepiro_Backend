package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.community.dto.request.UpsertArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleDetailResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.event.ArticleFileUploadEvent
import com.numberone.daepiro.domain.community.repository.ArticleRepository
import com.numberone.daepiro.domain.community.repository.findByIdOrThrow
import com.numberone.daepiro.domain.file.entity.FileDocumentType
import com.numberone.daepiro.domain.file.model.RawFile
import com.numberone.daepiro.domain.file.repository.FileRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val fileRepository: FileRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
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
        request: UpsertArticleRequest,
        attachFileList: List<MultipartFile>?,
    ): ArticleSimpleResponse {
        val article = articleRepository.findByIdOrThrow(id)

        article.update(
            title = request.title,
            body = request.body,
            type = request.articleType,
            category = request.articleCategory,
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

    fun getOne(id: Long): ArticleDetailResponse {
        val article = articleRepository.findByIdOrThrow(id)
        val files = fileRepository.findAllByDocumentTypeAndDocumentId(
            documentType = FileDocumentType.ARTICLE,
            article.id!!
        )
        return ArticleDetailResponse.of(
            article = article,
            files = files,
        )
    }
}

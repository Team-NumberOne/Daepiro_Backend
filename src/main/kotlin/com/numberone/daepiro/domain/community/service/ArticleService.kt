package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.community.dto.request.CreateArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.event.ArticleFileUploadEvent
import com.numberone.daepiro.domain.community.repository.ArticleRepository
import com.numberone.daepiro.domain.file.model.RawFile
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
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun createOne(
        request: CreateArticleRequest,
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
}

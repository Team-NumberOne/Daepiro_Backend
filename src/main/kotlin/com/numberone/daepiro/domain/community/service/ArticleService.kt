package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.saveAddressIfNotExists
import com.numberone.daepiro.domain.community.dto.request.CreateArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.repository.ArticleRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
) {
    @Transactional
    fun createOne(
        request: CreateArticleRequest,
        userId: Long,
    ): ArticleSimpleResponse {
        val author = userRepository.findByIdOrThrow(userId)

        val address = if (request.isLocationVisible) {
            addressRepository.saveAddressIfNotExists(
                si = request.si,
                gu = request.gu,
                dong = request.dong,
            )
        } else {
            null
        }

        val article = articleRepository.save(
            Article.of(
                title = request.title,
                body = request.body,
                type = request.articleType,
                category = request.articleCategory,
                isLocationVisible = request.isLocationVisible,
                authUser = author,
                address = address,
            )
        )

        return ArticleSimpleResponse.from(article)
    }
}

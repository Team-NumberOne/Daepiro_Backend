package com.numberone.daepiro.domain.community.repository.article

import com.numberone.daepiro.domain.address.entity.QAddress.address
import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.QAddressResponse
import com.numberone.daepiro.domain.community.dto.response.QArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.QAuthorResponse
import com.numberone.daepiro.domain.community.entity.QArticle.article
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

class ArticleRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ArticleRepositoryCustom {
    override fun getArticles(
        request: GetArticleRequest,
        siDo: String?,
        siGunGu: String?,
    ): Slice<ArticleListResponse> {
        val pageable = request.getPageable()

        val content = queryFactory.select(
            QArticleListResponse(
                article.id,
                article.status,
                article.type.stringValue(),
                article.category,
                article.title,
                article.body,
                article.likeCount,
                article.viewCount,
                article.commentCount,
                article.reportCount,
                QAddressResponse(
                    caseWhenLocationVisible(address.id, null),
                    caseWhenLocationVisible(address.siDo, null),
                    caseWhenLocationVisible(address.siGunGu, null)
                ),
                article.createdAt,
                article.lastModifiedAt,
                QAuthorResponse(
                    article.authUser.id,
                    article.authUser.nickname,
                    article.authUser.realname,
                    article.authUser.isCompletedOnboarding
                )
            )
        ).from(article)
            .leftJoin(address).on(article.address.id.eq(address.id))
            .where(
                siDo?.let { address.siDo.eq(it) },
                siGunGu?.let { address.siGunGu.eq(it) },
                request.type?.let { article.type.eq(it) },
                request.category?.let { article.category.eq(it) },
                request.status?.let { article.status.eq(it) },
            )
            .orderBy(article.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize + 1L)
            .fetch()


        return SliceImpl(
            content,
            pageable,
            hasNextPage(content, pageable.pageSize)
        )
    }

    private fun hasNextPage(contents: MutableList<ArticleListResponse>, pageSize: Int): Boolean {
        return if (contents.size > pageSize) {
            contents.removeAt(pageSize) // 초과한 한 개 제거
            true
        } else {
            false
        }
    }

    private fun <T> caseWhenLocationVisible(then: Expression<T>, otherwise: T?): Expression<T> {
        return CaseBuilder()
            .`when`(article.isLocationVisible.isTrue)
            .then(then)
            .otherwise(otherwise)
    }

}

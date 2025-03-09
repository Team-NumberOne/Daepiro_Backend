package com.numberone.daepiro.domain.community.repository.article

import com.numberone.daepiro.domain.address.entity.QAddress.address
import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.QAddressResponse
import com.numberone.daepiro.domain.community.dto.response.QArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.QAuthorResponse
import com.numberone.daepiro.domain.community.entity.ArticleStatus
import com.numberone.daepiro.domain.community.entity.QArticle.article
import com.numberone.daepiro.domain.mypage.dto.request.GetMyArticleRequest
import com.numberone.daepiro.global.utils.SecurityContextUtils
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import java.time.LocalDateTime

class ArticleRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ArticleRepositoryCustom {
    override fun getArticles(
        request: GetArticleRequest,
        addressValues: List<com.numberone.daepiro.domain.address.entity.Address>
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
                    caseWhenLocationVisible(address.siGunGu, null),
                    caseWhenLocationVisible(address.eupMyeonDong, null),
                ),
                article.createdAt,
                article.lastModifiedAt,
                QAuthorResponse(
                    article.authUser.id,
                    article.authUser.nickname,
                    article.authUser.realname,
                    article.authUser.isCompletedOnboarding,
                    article.authUser.profileImageUrl
                ),
                article.authUser.id.eq(SecurityContextUtils.getUserId()),
            )
        ).from(article)
            .leftJoin(address).on(article.address.id.eq(address.id))
            .where(
                addressValues.map { addr ->
                    address.siDo.eq(addr.siDo).and(address.siGunGu.eq(addr.siGunGu))
                }.reduce(BooleanExpression::or),
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

    override fun getLatestArticles(
        request: GetArticleRequest,
        addressValues: List<com.numberone.daepiro.domain.address.entity.Address>
    ): Slice<ArticleListResponse> {
        val pageable = request.getPageable()

        val oneWeekAgo = LocalDateTime.now().minusWeeks(1)

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
                    address.id,
                    address.siDo,
                    address.siGunGu,
                    caseWhenLocationVisible(address.eupMyeonDong, null),
                ),
                article.createdAt,
                article.lastModifiedAt,
                QAuthorResponse(
                    article.authUser.id,
                    article.authUser.nickname,
                    article.authUser.realname,
                    article.authUser.isCompletedOnboarding,
                    article.authUser.profileImageUrl
                ),
                article.authUser.id.eq(SecurityContextUtils.getUserId()),
            )
        ).from(article)
            .leftJoin(address).on(article.address.id.eq(address.id))
            .where(
                addressValues.map { addr ->
                    address.siDo.eq(addr.siDo).and(address.siGunGu.eq(addr.siGunGu))
                }.reduce(BooleanExpression::or),
                request.type?.let { article.type.eq(it) },
                request.category?.let { article.category.eq(it) },
                request.status?.let { article.status.eq(it) },
                article.createdAt.after(oneWeekAgo)
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

    override fun getMyArticles(userId: Long, request: GetMyArticleRequest): Slice<ArticleListResponse> {
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
                    caseWhenLocationVisible(address.siGunGu, null),
                    caseWhenLocationVisible(address.eupMyeonDong, null),
                ),
                article.createdAt,
                article.lastModifiedAt,
                QAuthorResponse(
                    article.authUser.id,
                    article.authUser.nickname,
                    article.authUser.realname,
                    article.authUser.isCompletedOnboarding,
                    article.authUser.profileImageUrl
                ),
                article.authUser.id.eq(SecurityContextUtils.getUserId()),
            )
        ).from(article)
            .leftJoin(address).on(article.address.id.eq(address.id))
            .where(article.authUser.id.eq(userId), article.status.eq(ArticleStatus.ACTIVE))
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

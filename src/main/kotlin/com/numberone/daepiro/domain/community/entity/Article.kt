package com.numberone.daepiro.domain.community.entity

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "article")
class Article(
    @Column(nullable = false)
    val title: String = "",

    @Column(nullable = false)
    val body: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    val type: ArticleType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    val category: ArticleCategory,

    @Column(nullable = false)
    val isLocationVisible: Boolean = false,

    @Column(nullable = false)
    val likeCount: Int = 0,

    @Column(nullable = false)
    val viewCount: Int = 0,

    @Column(nullable = false)
    val commentCount: Int = 0,

    @Column(nullable = false)
    val reportCount: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    val status: ArticleStatus = ArticleStatus.ACTIVE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val authUser: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val address: Address? = null,
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            title: String,
            body: String,
            type: ArticleType,
            category: ArticleCategory,
            isLocationVisible: Boolean,
            likeCount: Int = 0,
            viewCount: Int = 0,
            commentCount: Int = 0,
            reportCount: Int = 0,
            status: ArticleStatus = ArticleStatus.ACTIVE,
            authUser: UserEntity,
            address: Address? = null
        ): Article {
            return Article(
                title = title,
                body = body,
                type = type,
                category = category,
                isLocationVisible = isLocationVisible,
                likeCount = likeCount,
                viewCount = viewCount,
                commentCount = commentCount,
                reportCount = reportCount,
                status = status,
                authUser = authUser,
                address = address,
            )
        }
    }
}

enum class ArticleCategory {
    LIFE,
    TRAFFIC,
    SAFE,
    OTHER,
    ;
}

enum class ArticleType {
    DONGNE,
    INFORMATION,
    ;
}

enum class ArticleStatus {
    DELETED,
    ACTIVE,
    ;
}

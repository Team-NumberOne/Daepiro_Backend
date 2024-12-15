package com.numberone.daepiro.domain.community.entity

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.disaster.entity.DisasterType
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
    title: String = "",
    body: String = "",
    type: ArticleType,
    category: ArticleCategory,
    isLocationVisible: Boolean = false,
    likeCount: Int = 0,
    viewCount: Int = 0,
    commentCount: Int = 0,
    reportCount: Int = 0,
    status: ArticleStatus = ArticleStatus.ACTIVE,
    authUser: UserEntity? = null,
    address: Address? = null,
    disasterType: DisasterType? = null,
) : PrimaryKeyEntity() {
    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(nullable = false)
    var body: String = body
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var type: ArticleType = type
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var category: ArticleCategory = category
        protected set

    @Column(nullable = false)
    var isLocationVisible: Boolean = isLocationVisible
        protected set

    @Column(nullable = false)
    var likeCount: Int = likeCount
        protected set

    @Column(nullable = false)
    var viewCount: Int = viewCount
        protected set

    @Column(nullable = false)
    var commentCount: Int = commentCount
        protected set

    @Column(nullable = false)
    var reportCount: Int = reportCount
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var status: ArticleStatus = status
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var authUser: UserEntity? = authUser
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var address: Address? = address
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_type_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var disasterType: DisasterType? = disasterType
        protected set

    companion object {
        fun of(
            title: String,
            body: String,
            type: ArticleType,
            category: ArticleCategory,
            visibility: Boolean,
            likeCount: Int = 0,
            viewCount: Int = 0,
            commentCount: Int = 0,
            reportCount: Int = 0,
            status: ArticleStatus = ArticleStatus.ACTIVE,
            authUser: UserEntity? = null,
            address: Address? = null,
            disasterType: DisasterType? = null,
        ): Article {
            return Article(
                title = title,
                body = body,
                type = type,
                category = category,
                isLocationVisible = visibility,
                likeCount = likeCount,
                viewCount = viewCount,
                commentCount = commentCount,
                reportCount = reportCount,
                status = status,
                authUser = authUser,
                address = address,
                disasterType = disasterType,
            )
        }
    }

    fun updateAddress(address: Address): Article {
        return this.apply {
            this.address = address
        }
    }

    fun increaseCommentCount(): Article {
        return this.apply {
            commentCount++
        }
    }

    fun increaseLikeCount(): Article {
        return this.apply {
            likeCount++;
        }
    }

    fun increaseReportCount(): Article {
        return this.apply {
            reportCount++;
        }
    }

    fun decreaseLikeCount(): Article {
        return this.apply {
            likeCount = (likeCount - 1)
                .coerceAtLeast(0)
        }
    }


    fun decreaseCommentCount(): Article {
        return this.apply {
            commentCount = (commentCount - 1)
                .coerceAtLeast(0) // 0 이상은 보장
        }
    }


    fun update(
        title: String,
        body: String,
        type: ArticleType,
        category: ArticleCategory,
        isLocationVisible: Boolean,
    ) {
        this.title = title
        this.body = body
        this.type = type
        this.category = category
        this.isLocationVisible = isLocationVisible
    }
}

enum class ArticleCategory {
    LIFE,
    TRAFFIC,
    SAFE,
    OTHER,
    ;
}

enum class ArticleType(
    val description: String
) {
    DONGNE("동네생활"),
    INFORMATION("정보"),
    DISASTER("재난상황"),
    ;
}

enum class ArticleStatus {
    DELETED,
    ACTIVE,
    ;
}

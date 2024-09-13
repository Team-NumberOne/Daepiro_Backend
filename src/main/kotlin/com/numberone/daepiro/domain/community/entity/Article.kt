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
    title: String = "",
    body: String = "",
    type: ArticleType = ArticleType.SNS,
    likeCount: Int = 0,
    viewCount: Int = 0,
    commentCount: Int = 0,
    reportCount: Int = 0,
    status: ArticleStatus = ArticleStatus.ACTIVE,
    authUser: UserEntity? = null,
    address: Address? = null,
) : PrimaryKeyEntity() {
    @Column(nullable = false)
    var title = title
        protected set

    @Column(nullable = false)
    var body = body
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var type = type
        protected set

    @Column(nullable = false)
    var likeCount = likeCount
        protected set

    @Column(nullable = false)
    var viewCount = viewCount
        protected set

    @Column(nullable = false)
    var commentCount = commentCount
        protected set

    @Column(nullable = false)
    var reportComment = reportCount
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var authUser = authUser
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var address = address
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var status = status
        protected set
}

enum class ArticleType {
    SNS, INFORMATION
}

enum class ArticleStatus {
    DELETED, ACTIVE
}

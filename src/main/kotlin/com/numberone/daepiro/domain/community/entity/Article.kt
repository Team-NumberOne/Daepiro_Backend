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
    val type: ArticleType = ArticleType.SNS,

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
) : PrimaryKeyEntity()

enum class ArticleType {
    SNS, INFORMATION
}

enum class ArticleStatus {
    DELETED, ACTIVE
}

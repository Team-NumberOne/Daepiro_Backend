package com.numberone.daepiro.domain.dataCollecter.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

// todo 뉴스 데이터는 추후 만들어질 게시글 스키마에 포함되고 News 스키마는 삭제 예정
@Entity
@Table(name = "`news`")
class News(
    val title: String,

    val publishedAt: LocalDateTime,

    val subtitle: String,

    val body: String,

    val thumbnailUrl: String?,
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            title: String,
            publishedAt: LocalDateTime,
            subtitle: String,
            body: String,
            thumbnailUrl: String?
        ): News {
            return News(
                title = title,
                publishedAt = publishedAt,
                subtitle = subtitle,
                body = body,
                thumbnailUrl = thumbnailUrl
            )
        }
    }
}

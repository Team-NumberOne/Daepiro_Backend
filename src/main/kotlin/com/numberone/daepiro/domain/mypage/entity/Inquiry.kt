package com.numberone.daepiro.domain.mypage.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Inquiry(
    @Enumerated(EnumType.STRING)
    val type: InquiryType,
    val content: String,
    val email: String,
    val userId: Long
) : PrimaryKeyEntity(){
    companion object {
        fun of(
            type: InquiryType,
            content: String,
            email: String,
            userId: Long
        ): Inquiry {
            return Inquiry(
                type = type,
                content = content,
                email = email,
                userId = userId
            )
        }
    }
}

enum class InquiryType {
    SERVICE, DISASTER, COMMUNITY, ETC
}

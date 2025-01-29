package com.numberone.daepiro.domain.mypage.repository

import com.numberone.daepiro.domain.mypage.entity.Inquiry
import org.springframework.data.jpa.repository.JpaRepository

interface InquiryRepository: JpaRepository<Inquiry, Long> {
}

package com.numberone.daepiro.domain.dataCollecter.repository

import com.numberone.daepiro.domain.dataCollecter.entity.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

// todo 뉴스 데이터는 추후 만들어질 게시글 스키마에 포함되고 News 스키마는 삭제 예정
interface NewsRepository : JpaRepository<News, Long> {
    @Query("select n from News n order by n.publishedAt desc")
    fun findLatestNews(): News?
}

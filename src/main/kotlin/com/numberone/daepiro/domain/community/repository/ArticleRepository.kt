package com.numberone.daepiro.domain.community.repository

import com.numberone.daepiro.domain.community.entity.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long>

package com.numberone.daepiro.domain.address.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "`address`")
class Address(
    val si: String,
    val gu: String? = null,
    val dong: String? = null,
    val depth: Int = 0
) : PrimaryKeyEntity()

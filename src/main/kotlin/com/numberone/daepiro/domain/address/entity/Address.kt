package com.numberone.daepiro.domain.address.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "`address`")
class Address(
    si: String? = null,
    gu: String? = null,
    dong: String? = null,
    depth: Int = 0
) : PrimaryKeyEntity() {
    var si: String? = si
        protected set

    var gu: String? = gu
        protected set

    var dong: String? = dong
        protected set

    var depth: Int = depth
        protected set
}

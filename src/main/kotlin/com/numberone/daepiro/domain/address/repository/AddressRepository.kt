package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AddressRepository : JpaRepository<Address, Long> {
    @Query(
        "select a " +
            "from Address a " +
            "where a.si = :si " +
            "and a.gu = :gu " +
            "and a.dong = :dong"
    )
    fun findByAddress(si: String, gu: String?, dong: String?): Address?
}

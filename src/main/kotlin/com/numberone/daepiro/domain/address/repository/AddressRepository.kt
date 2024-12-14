package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.address.vo.AddressInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AddressRepository : JpaRepository<Address, Long> {
    @Query(
        "select k from Address k " +
            "where (k.siDo = :#{#ai.si} or (:#{#ai.si} is null and k.siDo is null)) and " +
            "(k.siGunGu = :#{#ai.gu} or (:#{#ai.gu} is null and k.siGunGu is null)) and " +
            "(k.eupMyeonDong = :#{#ai.dong} or (:#{#ai.dong} is null and k.eupMyeonDong is null))"
    )
    fun findByAddressInfo(ai: AddressInfo): Address?

    @Query(
        "select k from Address k " +
            "where (:#{#ai.depth} > 1 and k.siDo = :#{#ai.si} and k.siGunGu is null and k.eupMyeonDong is null) or " +
            "(:#{#ai.depth} > 2 and k.siDo = :#{#ai.si} and k.siGunGu = :#{#ai.gu} and k.eupMyeonDong is null)"
    )
    fun findParentAddress(ai: AddressInfo): List<Address>

    @Query(
        "select k from Address k " +
            "where (:#{#ai.depth} = 1 and k.siDo = :#{#ai.si}) or " +
            "(:#{#ai.depth} = 2 and k.siDo = :#{#ai.si} and k.siGunGu = :#{#ai.gu})"
    )
    fun findChildAddress(ai: AddressInfo): List<Address>

    fun findAllByIdIn(ids: List<Long>): List<Address>
}

fun AddressRepository.findByAddressInfoOrThrow(ai: AddressInfo): Address {
    return findByAddressInfo(ai)
        ?: throw IllegalArgumentException("올바르지 않은 주소 요청입니다.")
}

fun AddressRepository.findAllRelatedAddressBy(address: Address): Set<Address> {
    val parents = findParentAddress(AddressInfo.from(address))
    val children = findChildAddress(AddressInfo.from(address))
    return (parents + children + address).toSet()
}

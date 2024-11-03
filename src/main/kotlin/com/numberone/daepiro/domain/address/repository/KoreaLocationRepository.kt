package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.KoreaLocation
import com.numberone.daepiro.domain.address.vo.AddressInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface KoreaLocationRepository : JpaRepository<KoreaLocation, Long> {
    @Query(
        "select k from KoreaLocation k " +
            "where (k.siDo = :#{#addressInfo.si} or (:#{#addressInfo.si} is null and k.siDo is null)) and " +
            "(k.siGunGu = :#{#addressInfo.gu} or (:#{#addressInfo.gu} is null and k.siGunGu is null)) and " +
            "(k.eupMyeonDong = :#{#addressInfo.dong} or (:#{#addressInfo.dong} is null and k.eupMyeonDong is null))"
    )
    fun findByAddressInfo(addressInfo: AddressInfo): KoreaLocation?

    @Query(
        "select k from KoreaLocation k " +
            "where (:#{#ai.depth} > 1 and k.siDo = :#{#ai.si} and k.siGunGu is null and k.eupMyeonDong is null) or " +
            "(:#{#ai.depth} > 2 and k.siDo = :#{#ai.si} and k.siGunGu = :#{#ai.gu} and k.eupMyeonDong is null)"
    )
    fun findParentLocation(ai: AddressInfo): List<KoreaLocation>

    @Query(
        "select k from KoreaLocation k " +
            "where (:#{#ai.depth} = 1 and k.siDo = :#{#ai.si}) or " +
            "(:#{#ai.depth} = 2 and k.siDo = :#{#ai.si} and k.siGunGu = :#{#ai.gu})"
    )
    fun findChildLocation(ai: AddressInfo): List<KoreaLocation>
}

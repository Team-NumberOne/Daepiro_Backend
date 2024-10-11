package com.numberone.daepiro.domain.address.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class KoreaLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "si_do")
    var siDo: String? = null

    @Column(name = "si_gun_gu")
    var siGunGu: String? = null

    @Column(name = "eup_myeon_dong")
    var eupMyeonDong: String? = null
}

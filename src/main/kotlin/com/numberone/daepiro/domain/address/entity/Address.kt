package com.numberone.daepiro.domain.address.entity

import com.numberone.daepiro.domain.disaster.entity.Disaster
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "si_do")
    var siDo: String,

    @Column(name = "si_gun_gu")
    var siGunGu: String? = null,

    @Column(name = "eup_myeon_dong")
    var eupMyeonDong: String? = null,

    val depth: Int = 0,

    @OneToMany(mappedBy = "address", cascade = [CascadeType.ALL])
    val disasters: List<Disaster> = emptyList(),

    @OneToMany(mappedBy = "address", cascade = [CascadeType.ALL])
    val userAddresses: List<UserAddress> = emptyList()
) {
    fun toAddress(): String {
        val ret = "$siDo $siGunGu $eupMyeonDong"
        return ret.replace(" null", "")
    }
}

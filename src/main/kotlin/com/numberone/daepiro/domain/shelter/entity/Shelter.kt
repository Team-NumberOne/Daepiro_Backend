package com.numberone.daepiro.domain.shelter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "`shelter`")
class Shelter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val name: String,

    val address: String,

    val longitude: Double,

    val latitude: Double,

    @Enumerated(EnumType.STRING)
    val type: ShelterType
) {
}

enum class ShelterType(
    val korean: String
) {
    TEMPERATURE("쉼터"),
    EARTHQUAKE("지진옥외"),
    TSUNAMI("지진해일"),
    CIVIL("민방위")
}

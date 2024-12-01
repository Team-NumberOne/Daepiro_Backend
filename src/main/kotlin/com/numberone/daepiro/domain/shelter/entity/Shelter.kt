package com.numberone.daepiro.domain.shelter.entity

import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
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
    val word: String
) {
    TEMPERATURE("temperature"),
    EARTHQUAKE("earthquake"),
    TSUNAMI("tsunami"),
    CIVIL("civil");

    companion object {
        fun word2code(korean: String): ShelterType {
            return entries.find { it.word == korean }
                ?: throw CustomException(CustomErrorContext.NOT_FOUND_SHELTER_TYPE)
        }
    }
}

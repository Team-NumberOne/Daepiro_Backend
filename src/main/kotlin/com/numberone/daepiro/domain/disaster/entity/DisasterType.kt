package com.numberone.daepiro.domain.disaster.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_DISASTER_TYPE
import com.numberone.daepiro.global.exception.CustomException
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
@Entity
@Table(name = "`disaster_type`")
class DisasterType(
    @Enumerated(EnumType.STRING)
    val type: Type,

    @OneToMany(mappedBy = "disasterType", cascade = [CascadeType.ALL])
    val userDisasterTypes: List<UserDisasterType>
) : PrimaryKeyEntity() {
    enum class Type(
        val korean: String
    ) {
        // 자연재난
        DROUGHT("가뭄"),
        STRONG_WIND("강풍"),
        DRYNESS("건조"),
        HEAVY_SNOWFALL("대설"),
        TIDAL_WAVE("대조기"),
        FINE_DUST("미세먼지"),
        WILDFIRE("산불"),
        LANDSLIDE("산사태"),
        FOG("안개"),
        EARTHQUAKE("지진"),
        TYPHOON("태풍"),
        HEATWAVE("폭염"),
        ROUGH_SEA("풍랑"),
        COLD_WAVE("한파"),
        HEAVY_RAIN("호우"),
        FLOOD("홍수"),

        // 사회재난
        GAS("가스"),
        TRAFFIC("교통"),
        FINANCE("금융"),
        COLLAPSE("붕괴"),
        WATER_SUPPLY("수도"),
        ENERGY("에너지"),
        MEDICAL("의료"),
        INFECTIOUS_DISEASE("전염병"),
        POWER_OUTAGE("정전"),
        COMMUNICATION("통신"),
        EXPLOSION("폭발"),
        FIRE("화재"),
        ENVIRONMENTAL_POLLUTION("환경오염사고"),
        AI("AI"),

        // 비상대비
        EMERGENCY("비상사태"),
        TERROR("테러"),
        CHEMICAL("화생방사고"),

        // 기타
        MISSING("실종"),
        OTHERS("기타");

        companion object {
            fun kor2code(korean: String): Type {
                return entries.find { it.korean == korean }
                    ?: throw CustomException(NOT_FOUND_DISASTER_TYPE)
            }
        }
    }
}

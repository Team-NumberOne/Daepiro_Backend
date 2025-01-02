package com.numberone.daepiro.domain.behaviourTip.service

import com.numberone.daepiro.domain.behaviourTip.dto.request.CreateTipRequest
import com.numberone.daepiro.domain.behaviourTip.dto.response.BehaviourTipDisasterResponse
import com.numberone.daepiro.domain.behaviourTip.dto.response.FilterResponse
import com.numberone.daepiro.domain.behaviourTip.dto.response.GetBehaviourTipResponse
import com.numberone.daepiro.domain.behaviourTip.entity.BehaviourTip
import com.numberone.daepiro.domain.behaviourTip.repository.BehaviourTipRepository
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.entity.DisasterType.DisasterValue.Companion
import com.numberone.daepiro.domain.disaster.enums.DisasterLevel
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.domain.disaster.repository.findByTypeOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_DISASTER_TYPE
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BehaviourTipService(
    private val behaviourTipRepository: BehaviourTipRepository,
    private val disasterTypeRepository: DisasterTypeRepository
) {
    fun getEmergencyTips(): ApiResult<List<BehaviourTipDisasterResponse>> {
        val disasterTypes = disasterTypeRepository.findByLevel(DisasterLevel.EMERGENCY)

        return ApiResult.ok(
            sortTypesByKorean(disasterTypes)
                .map {
                    BehaviourTipDisasterResponse.of(
                        it,
                        behaviourTipRepository.findTipFilters(it.id!!)
                            .map { filter ->
                                FilterResponse.of(
                                    filter,
                                    behaviourTipRepository.findByDisasterTypeAndFilter(it, filter).map { it.tip })
                            }
                    )
                }
        )
    }

    fun getCommonTips(): ApiResult<List<BehaviourTipDisasterResponse>> {
        val disasterTypes = disasterTypeRepository.findByLevel(DisasterLevel.COMMON)

        return ApiResult.ok(
            sortTypesByKorean(disasterTypes)
                .map {
                    BehaviourTipDisasterResponse.of(
                        it,
                        behaviourTipRepository.findTipFilters(it.id!!)
                            .map { filter ->
                                FilterResponse.of(
                                    filter,
                                    behaviourTipRepository.findByDisasterTypeAndFilter(it, filter).map { it.tip })
                            }
                    )
                }
        )
    }

    private fun sortTypesByKorean(tips: List<DisasterType>): List<DisasterType> {
        return tips.sortedBy { it.type.korean }
    }

    fun searchTips(keyword: String): ApiResult<List<BehaviourTipDisasterResponse>> {
        val disasterTypes = disasterTypeRepository.findAll()
        val filteredTypes = disasterTypes.filter { containsChosung(it.type.korean, keyword) }

        return ApiResult.ok(
            sortTypesByKorean(filteredTypes)
                .map {
                    BehaviourTipDisasterResponse.of(
                        it, listOf()
                    )
                }
        )
    }

    private fun containsChosung(text: String, keyword: String): Boolean {
        val chosungText = extractChosung(text)
        val chosungKeyword = extractChosung(keyword)
        return chosungText.contains(chosungKeyword)
    }

    private fun extractChosung(text: String): String {
        val chosung = listOf(
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        )
        val result = StringBuilder()
        for (char in text) {
            if (char in '가'..'힣') {
                val unicode = char - '가'
                val chosungIndex = unicode / (21 * 28)
                result.append(chosung[chosungIndex])
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }

    @Transactional
    fun createTip(request: CreateTipRequest) {
        val disasterType =
            disasterTypeRepository.findByTypeOrThrow(DisasterType.DisasterValue.kor2code(request.disasterType))
        val behaviourTip = BehaviourTip.of(
            disasterType,
            request.filter,
            request.tip
        )

        behaviourTipRepository.save(behaviourTip)
    }

    fun getBehaviourTip(disasterTypeId: Long): ApiResult<GetBehaviourTipResponse> {
        val disasterType = disasterTypeRepository.findByIdOrNull(disasterTypeId)
            ?: throw CustomException(NOT_FOUND_DISASTER_TYPE)
        val filters = behaviourTipRepository.findTipFilters(disasterType.id!!)

        return ApiResult.ok(
            GetBehaviourTipResponse.of(
                disasterType.type.korean,
                filters.map { filter ->
                    FilterResponse.of(
                        filter,
                        behaviourTipRepository.findByDisasterTypeAndFilter(disasterType, filter).map { it.tip }
                    )
                }
            )
        )
    }
}

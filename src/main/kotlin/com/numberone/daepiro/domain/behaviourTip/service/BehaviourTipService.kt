package com.numberone.daepiro.domain.behaviourTip.service

import com.numberone.daepiro.domain.behaviourTip.dto.response.BehaviourTipDisasterResponse
import com.numberone.daepiro.domain.behaviourTip.repository.BehaviourTipRepository
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.enums.DisasterLevel
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.global.dto.ApiResult
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
                .map { BehaviourTipDisasterResponse.of(it) }
        )
    }

    fun getCommonTips(): ApiResult<List<BehaviourTipDisasterResponse>> {
        val disasterTypes = disasterTypeRepository.findByLevel(DisasterLevel.COMMON)

        return ApiResult.ok(
            sortTypesByKorean(disasterTypes)
                .map { BehaviourTipDisasterResponse.of(it) }
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
                .map { BehaviourTipDisasterResponse.of(it) }
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
}

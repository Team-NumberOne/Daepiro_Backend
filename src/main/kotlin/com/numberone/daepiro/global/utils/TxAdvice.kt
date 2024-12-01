package com.numberone.daepiro.global.utils

import jakarta.transaction.NotSupportedException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class TransactionUtils(
    private val txAdvice: TxAdvice, // 초기화와 함께 불변성 보장
) {

    companion object {
        private lateinit var txAdvice: TxAdvice
        fun <T> writable(
            propagation: Propagation = Propagation.REQUIRED,
            function: () -> T,
        ): T {
            return when (propagation) {
                Propagation.REQUIRED -> txAdvice.writableRequired(function)
                Propagation.REQUIRES_NEW -> txAdvice.writableRequiresNew(function)
                else -> throw NotSupportedException() // Todo: 다른 propagation 에 대해서도 커버
            }
        }

        fun <T> readOnly(
            propagation: Propagation = Propagation.REQUIRED,
            function: () -> T,
        ): T {
            return when (propagation) {
                Propagation.REQUIRED -> txAdvice.readOnlyRequired(function)
                Propagation.REQUIRES_NEW -> txAdvice.readOnlyRequiresNew(function)
                else -> throw NotSupportedException() // Todo: 다른 propagation 에 대해서도 커버
            }
        }
    }
}

@Component
class TxAdvice {

    @Transactional(propagation = Propagation.REQUIRED)
    fun <T> writableRequired(function: () -> T): T {
        return function()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> writableRequiresNew(function: () -> T): T {
        return function()
    }

    @Transactional(readOnly = true)
    fun <T> readOnlyRequired(function: () -> T): T {
        return function()
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    fun <T> readOnlyRequiresNew(function: () -> T): T {
        return function()
    }
}

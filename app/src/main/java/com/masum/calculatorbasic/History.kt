package com.masum.calculatorbasic

import kotlinx.serialization.Serializable

@Serializable
data class CalculationHistory(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

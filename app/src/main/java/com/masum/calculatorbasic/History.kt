package com.masum.calculatorbasic

data class CalculationHistory(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

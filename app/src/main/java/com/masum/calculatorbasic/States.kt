package com.masum.calculatorbasic

data class States (
    val number1: String = "",
    val number2: String = "",
    val operation: Operations? = null,
    val history: List<CalculationHistory> = emptyList(),
    val showHistory: Boolean = false
)
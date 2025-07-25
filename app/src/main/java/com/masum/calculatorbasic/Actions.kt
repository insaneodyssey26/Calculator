package com.masum.calculatorbasic

sealed class Actions {
    data class Number(val number: Int): Actions()
    object Clear: Actions()
    object Delete: Actions()
    object Calculate: Actions()
    object Decimal: Actions()
    data class Operation(val operation: Operations): Actions()
    data class UnaryOperation(val operation: Operations): Actions()
    data class DeleteHistoryItem(val item: CalculationHistory): Actions()
    object ToggleHistory: Actions()
    object ClearHistory: Actions()
    data class UseHistoryResult(val result: String): Actions()
    object ToggleScientific: Actions()
}
package com.masum.calculatorbasic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
import kotlin.math.abs

class CalculatorViewModel: ViewModel() {
    var state by mutableStateOf(States())
        private set

    private val formatter = DecimalFormat("#.#########").apply {
        isGroupingUsed = false
    }

    fun onAction(action: Actions) {
        when(action) {
            is Actions.Number -> enterNumber(action.number)
            is Actions.Operation -> enterOperation(action.operation)
            Actions.Clear -> state = States(history = state.history)
            Actions.Calculate -> calculateResult()
            Actions.Delete -> performDelete()
            Actions.Decimal -> enterDecimal()
            Actions.ToggleHistory -> state = state.copy(showHistory = !state.showHistory)
            Actions.ClearHistory -> state = state.copy(history = emptyList())
            is Actions.UseHistoryResult -> {
                state = state.copy(
                    number1 = action.result,
                    number2 = "",
                    operation = null,
                    showHistory = false
                )
            }
        }
    }

    private fun calculateResult() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        
        if (number1 != null && number2 != null) {
            val result = when(state.operation) {
                is Operations.Add -> number1 + number2
                is Operations.Subtract -> number1 - number2
                is Operations.Multiply -> number1 * number2
                is Operations.Divide -> {
                    if (number2 != 0.0) number1 / number2 else TODO()
                }
                else -> return
            }
            
            val formattedResult = if (abs(result) >= 1e9 || (abs(result) < 1e-5 && result != 0.0)) {
                String.format("%.3e", result)
            } else {
                formatter.format(result)
            }.take(MAX_DISPLAY_LENGTH)
            
            val expression = "${state.number1} ${state.operation?.symbol} ${state.number2}"
            val historyEntry = CalculationHistory(expression, formattedResult)
            val newHistory = (listOf(historyEntry) + state.history).take(MAX_HISTORY_SIZE)
            
            state = state.copy(
                number1 = formattedResult,
                number2 = "",
                operation = null,
                history = newHistory
            )
        }
    }

    private fun performDelete() {
        when {
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.number1.isNotBlank() -> state = state.copy(
                number1 = state.number1.dropLast(1)
            )
        }
    }

    private fun enterOperation(operation: Operations) {
        if (state.number1.isNotBlank()) {
            if (state.number2.isNotBlank()) {
                calculateResult()
            }
            state = state.copy(operation = operation)
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.length >= MAX_NUMBER_LENGTH) {
                return
            }
            if (state.number1 == "0" && number != 0) {
                state = state.copy(number1 = number.toString())
            } else if (state.number1 != "0") {
                state = state.copy(number1 = state.number1 + number)
            }
            return
        }
        
        if (state.number2.length >= MAX_NUMBER_LENGTH) {
            return
        }
        if (state.number2 == "0" && number != 0) {
            state = state.copy(number2 = number.toString())
        } else if (state.number2 != "0") {
            state = state.copy(number2 = state.number2 + number)
        }
    }

    private fun enterDecimal() {
        if (state.operation == null) {
            if (!state.number1.contains(".") && state.number1.isNotBlank()) {
                state = state.copy(number1 = state.number1 + ".")
            } else if (state.number1.isBlank()) {
                state = state.copy(number1 = "0.")
            }
        } else {
            if (!state.number2.contains(".")) {
                val newNumber2 = if (state.number2.isBlank()) "0." else state.number2 + "."
                state = state.copy(number2 = newNumber2)
            }
        }
    }

    companion object {
        private const val MAX_NUMBER_LENGTH = 10
        private const val MAX_DISPLAY_LENGTH = 12
        private const val MAX_HISTORY_SIZE = 20
    }
}
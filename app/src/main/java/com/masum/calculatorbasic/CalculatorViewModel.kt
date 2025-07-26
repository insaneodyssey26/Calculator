package com.masum.calculatorbasic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.tan
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow

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
            is Actions.UnaryOperation -> {
                state = state.copy(
                    operation = action.operation,
                    number1 = "",
                    number2 = ""
                )
            }
            Actions.Clear -> state = States(history = state.history)
            Actions.Calculate -> calculateResult()
            Actions.Delete -> performDelete()
            Actions.Decimal -> enterDecimal()
            Actions.ToggleHistory -> state = state.copy(showHistory = !state.showHistory)
            Actions.ClearHistory -> state = state.copy(history = emptyList())
            Actions.ToggleScientific -> state = state.copy(showScientific = !state.showScientific)
            is Actions.DeleteHistoryItem -> {
                state = state.copy(history = state.history.filterNot { it == action.item })
            }
            is Actions.UseHistoryResult -> {
                state = state.copy(
                    number1 = action.result,
                    showHistory = false
                )
            }
        }
    }

    private fun calculateResult() {
        val op = state.operation
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        var result: Double? = null
        var expression = ""
        if (op != null) {
            when (op) {
                is Operations.Add, is Operations.Subtract, is Operations.Multiply, is Operations.Divide -> {
                    if (number1 != null && number2 != null) {
                        result = when (op) {
                            is Operations.Add -> number1 + number2
                            is Operations.Subtract -> number1 - number2
                            is Operations.Multiply -> number1 * number2
                            is Operations.Divide -> if (number2 != 0.0) number1 / number2 else null
                            else -> null
                        }
                        expression = "${state.number1} ${op.symbol} ${state.number2}"
                    }
                }
                is Operations.Percent, is Operations.PlusMinus, is Operations.Sqrt, is Operations.Square, is Operations.Reciprocal,
                is Operations.Sin, is Operations.Cos, is Operations.Tan, is Operations.Ln, is Operations.Log, is Operations.Factorial -> {
                    if (number1 != null) {
                        result = performUnaryForResult(op, number1)
                        expression = "${op.symbol}(${state.number1})"
                    }
                }
                else -> {}
            }
        }
        if (result != null) {
            val formattedResult = if (abs(result) >= 1e9 || (abs(result) < 1e-5 && result != 0.0)) {
                String.format("%.3e", result)
            } else {
                formatter.format(result)
            }.take(MAX_DISPLAY_LENGTH)
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
    
    // Used for calculation only, not for immediate apply
    private fun performUnaryForResult(op: Operations, value: Double): Double {
        return when(op) {
            is Operations.Percent -> value / 100
            is Operations.PlusMinus -> -value
            is Operations.Sqrt -> if (value >= 0) sqrt(value) else value
            is Operations.Square -> value * value
            is Operations.Reciprocal -> if (value != 0.0) 1 / value else value
            is Operations.Sin -> sin(Math.toRadians(value))
            is Operations.Cos -> cos(Math.toRadians(value))
            is Operations.Tan -> tan(Math.toRadians(value))
            is Operations.Ln -> if (value > 0) ln(value) else value
            is Operations.Log -> if (value > 0) log10(value) else value
            is Operations.Factorial -> {
                if (value >= 0 && value <= 20 && value == value.toInt().toDouble()) {
                    factorial(value.toInt()).toDouble()
                } else value
            }
            else -> value
        }
    }
    
    private fun factorial(n: Int): Long {
        return if (n <= 1) 1 else n * factorial(n - 1)
    }
}
package com.masum.calculatorbasic

import android.service.credentials.Action
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {
    var state by mutableStateOf(States())
    private set

    fun onAction (action: Actions) {
        when (action) {
            is Actions.Number -> enterNumber(action.number)
            is Actions.Operation -> enterOperation(action.operation)
            Actions.Clear -> clear()
            Actions.Calculate -> CalculateThis()
            Actions.Delete -> justDelete()
        }
    }

    private fun CalculateThis() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if (number1 != null && number2 !=null) {
            var result = when(state.operation) {
                is Operations.Add -> number1 + number2
                is Operations.Subtract -> number1 - number2
                is Operations.Multiply -> number1 * number2
                is Operations.Divide -> number1 / number2
            }
        }
    }

    private fun justDelete() {
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
        if(state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.length >= MAX_LENGTH) {
                return
            }
            state = state.copy(
                number1 = state.number1 + number
            )
            return
        }
        if (state.number2.length >= MAX_LENGTH) {
            return
        }
        state = state.copy(
            number2 = state.number2 + number
        )
    }

    private fun Decimal() {
        if (state.operation == null    // Jokhon kono operation thake na
            && !state.number1.contains(".")    // Jokhon number1 e kono decimal point na thake
            && state.number1.isNotBlank()) {   // Jokhon number1 e kono value thake
            state = state.copy(
                number1 = state.number1 + "."   // Oporer 3 te criteria satisfy hole then only decimal point add hobe
            )
            return
        }
        else if (!state.number2.contains(".")
            && !state.number2.isNotBlank()) {     // operation null er jonno check korte hobe na karon, ota null hole 2nd number enter hobe na
            state  = state.copy(
                number2 = state.number2 + "."
            )
        }
    }

    companion object {
        private const val  MAX_LENGTH = 8
    }
}
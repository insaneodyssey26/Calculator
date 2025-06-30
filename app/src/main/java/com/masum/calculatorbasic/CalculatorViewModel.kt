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

    private fun CalculateThis(operation: Operations) {

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
        if (state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterNumber(number: Int) {}
}
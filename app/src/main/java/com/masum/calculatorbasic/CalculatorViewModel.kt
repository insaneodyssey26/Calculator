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
    }

    private fun enterNumber(number: Int) {

    }

    private fun Decimal() {
        if (state.operation == null    // Jokhon kono operation thake na
            && !state.number1.contains(".")    // Jokhon number1 e kono decimal point na thake
            && state.number1.isNotBlank())    // Jokhon number1 e kono value thake
            state = state.copy(
                number1 = state.number1 + "."   // Oporer 3 te criteria satisfy hole then only decimal point add hobe
            )
        return
    }
}
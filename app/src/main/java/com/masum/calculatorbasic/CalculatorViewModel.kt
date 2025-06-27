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
            Actions.Calculate -> calculate()
            Actions.Delete -> delete()
        }
    }
}
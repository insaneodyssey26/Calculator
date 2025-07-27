package com.masum.calculatorbasic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CalculatorViewModelFactory(
    private val historyDataStore: HistoryDataStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(historyDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

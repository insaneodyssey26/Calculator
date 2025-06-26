package com.masum.calculatorbasic

sealed class Actions {
    data class Number(val number: Int): Actions()
    object clear: Actions()
    object delete: Actions()
    object calculate: Actions()
    object decimal: Actions()
    data class Operation (val operation: Operations): Actions()
}
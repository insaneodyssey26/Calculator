package com.masum.calculatorbasic

sealed class Actions {
    data class Number(val number: Int): Actions()
}
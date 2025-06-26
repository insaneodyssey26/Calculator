package com.masum.calculatorbasic

sealed class Operations (val symbol: String){
    object Add: Operations("+")
    object Subtract: Operations("-")
    object Multiply: Operations("X")
    object Divide: Operations("/")
}
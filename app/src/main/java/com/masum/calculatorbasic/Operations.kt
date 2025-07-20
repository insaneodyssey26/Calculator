package com.masum.calculatorbasic

sealed class Operations (val symbol: String){
    object Add: Operations("+")
    object Subtract: Operations("−")
    object Multiply: Operations("×")
    object Divide: Operations("÷")
    object Percent: Operations("%")
    object PlusMinus: Operations("±")
    object Sqrt: Operations("√")
    object Square: Operations("x²")
    object Reciprocal: Operations("1/x")
}
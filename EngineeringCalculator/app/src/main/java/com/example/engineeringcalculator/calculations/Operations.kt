package com.example.engineeringcalculator.calculations

import kotlin.math.pow

enum class Operations(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    POWER("^"),
    NONE("")
}

fun getOrder(operation: Operations) : Int{
    return when (operation){
        Operations.ADD -> 1
        Operations.SUBTRACT -> 1
        Operations.MULTIPLY -> 2
        Operations.DIVIDE -> 2
        Operations.POWER -> 3
        Operations.NONE -> 0
    }
}

fun getOperation(operation: Operations) : (Double, Double) -> Double{
    return when (operation){
        Operations.ADD -> {n1 : Double, n2: Double -> n1 + n2 }
        Operations.SUBTRACT -> {n1 : Double, n2: Double -> n1 - n2 }
        Operations.MULTIPLY -> {n1 : Double, n2: Double -> n1 * n2 }
        Operations.DIVIDE -> {n1 : Double, n2: Double -> n1 / n2 }
        Operations.POWER -> {n1 : Double, n2: Double -> n1.pow(n2) }
        Operations.NONE -> { _: Double, _: Double -> 0.0 }
    }

}
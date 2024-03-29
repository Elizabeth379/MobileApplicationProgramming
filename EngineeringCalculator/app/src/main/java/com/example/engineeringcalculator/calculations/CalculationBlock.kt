package com.example.engineeringcalculator.calculations

import java.lang.ArithmeticException

class  CalculationBlock(var resultFunction: (Double) -> Double,
                        var stringPattern: String,
                        val parentBlock: CalculationBlock?) {

    var operation: Operations = Operations.NONE
    var number: String = ""
    var blocks: MutableList<CalculationBlock> = mutableListOf<CalculationBlock>()
    var isCompleted: Boolean = false
    var isNegative: Boolean = false
    var isPrimitive: Boolean = true
    var requiresBrackets: Boolean = false
    var requiresFilling: Boolean = false

    constructor(parentBlock: CalculationBlock?, requiresFilling: Boolean) : this(
        { n: Double -> n },
        "",
        parentBlock
    ){
        this.requiresFilling = requiresFilling
    }

    constructor(resultFunction: (Double) -> Double,
                stringPattern: String,
                parentBlock: CalculationBlock?,
                isPrimitive: Boolean) : this(resultFunction, stringPattern, parentBlock){
        this.isPrimitive = isPrimitive
    }

    constructor(resultFunction: (Double) -> Double,
                stringPattern: String,
                parentBlock: CalculationBlock?,
                isPrimitive: Boolean,
                requiresBrackets: Boolean)
            : this(resultFunction, stringPattern, parentBlock, isPrimitive){
        this.requiresBrackets = requiresBrackets
    }

    fun appendNumber(char: String) {
        if (isPrimitive) {
            if (number.length >= 15) {
                // Достигнут лимит в 15 символов, прекратить добавление
                return
            }

            if (number == "0") {
                if (char == "0") {
                    return
                } else {
                    number = char
                }
            } else {
                if (number == "") {
                    number = char
                } else {
                    if (char != "π" && char != "e" && !number.contains("π", false) && number[0] != 'e') {
                        number += char
                    } else if (char == "e") {
                        if (!number.contains("e") && number.last() != '.') {
                            number += char
                        }
                    } else if (number.length > 1 && number.subSequence(0, 2) == "πe" && char != "π") {
                        number += char
                    }
                }
            }
        }
    }


    fun setFloat(){
        if(number != "" && number != "e" && number != "π"){
            number += '.'
        }
    }

    fun delete() : Boolean{
        if(requiresFilling){
            return false
        }
        if(operation != Operations.NONE){
            operation = Operations.NONE
            if(isPrimitive){
                isCompleted = false
            }
            return true
        }
        if(isCompleted && !isPrimitive){
            isCompleted = false
            return true
        }
        if(number != ""){
            number = when(number){
                "π" -> ""
                "e" -> ""
                else -> number.dropLast(1)
            }
            if(number.isEmpty()){
                if(isNegative){
                    requiresFilling = true
                    return true
                }
            }
            return number.isNotEmpty()
        }
        if(blocks.count() > 0){
            if(blocks.last().delete()) {
                blocks.dropLast(1)
            }
            return true
        }
        else if(isNegative){
            requiresFilling = true
            stringPattern = ""
            isCompleted = false
            return true
        }
        return false
    }

    fun evaluate() : Double{
        if(isPrimitive){
            val num = when (val newNum = number.replace("π", Math.PI.toString())) {
                "e" -> Math.E
                "π" -> Math.PI
                else -> newNum.toDoubleOrNull() ?: return 0.0
            }
            return num * if(isNegative) -1 else 1
        }

        val evaluated = mutableListOf<Double>()
        val operations = mutableListOf<Operations>()
        if(blocks.isEmpty()){
            return 0.0
        }
        for(block in blocks){
            evaluated.add(block.evaluate())
            operations.add(block.operation)
        }

        for(order in 3 downTo 1){
            var i = 0
            while(i < operations.count() - 1){
                val operation = operations[i]
                if(getOrder(operation) == order){
                    val n1 = evaluated[i]
                    val n2 = evaluated[i + 1]
                    if(operation == Operations.DIVIDE && n2 == 0.0){
                        throw ArithmeticException()
                    }
                    val res = getOperation(operation)(n1, n2)
                    evaluated[i] = res
                    evaluated.removeAt(i + 1)
                    operations.removeAt(i)
                    i--
                }
                i++
            }
        }
        return resultFunction(evaluated[0]) * if (isNegative) -1 else 1
    }

    override fun toString(): String {
        var output = ""
        if(isPrimitive){
            output += (if (isNegative) "-" else "") + number
            return output + operation.symbol
        }
        for(block in blocks){
            output += block.toString()
        }
        output = stringPattern + output + if (stringPattern != "" && isCompleted) ")" else ""
        output = if(isNegative) "-$output" else output
        output += operation.symbol
        return output
    }

}
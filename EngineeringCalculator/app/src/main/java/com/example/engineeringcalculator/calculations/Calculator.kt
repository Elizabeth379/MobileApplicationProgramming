package com.example.engineeringcalculator.calculations

import android.annotation.SuppressLint
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.round
import com.example.engineeringcalculator.Connector

class Calculator {
    private val defaultFunction: (Double) -> (Double) = {res:Double -> res}
    var mainBlock: CalculationBlock = CalculationBlock(defaultFunction, "", null, false)
    var currentBlock: CalculationBlock = mainBlock
    private var inputTextView: TextView? = null
    private var resultTextView: TextView? = null
    val db = Firebase.firestore
    lateinit var res: String


    fun appendNumber(char: String){
        if(currentBlock.requiresFilling){
            currentBlock.requiresFilling = false
            currentBlock.requiresBrackets = false
            currentBlock.isPrimitive = true
            currentBlock.stringPattern = ""
            currentBlock.resultFunction = {n: Double -> n}
        }
        if(currentBlock.isCompleted){
            if(currentBlock.operation == Operations.NONE){
                currentBlock.operation = Operations.MULTIPLY
            }
            currentBlock = currentBlock.parentBlock!!
            val newBlock = CalculationBlock(defaultFunction, "", currentBlock, true)
            currentBlock.blocks.add(newBlock)
            currentBlock = newBlock
        }
        if(!currentBlock.isPrimitive){
            currentBlock = CalculationBlock(defaultFunction, "", currentBlock, true)
            currentBlock.parentBlock?.blocks?.add(currentBlock)
            currentBlock.appendNumber(char)
        }
        else{
            currentBlock.appendNumber(char)
        }

        update()
    }

    fun appendOperation(operation: Operations){
        if(currentBlock.requiresFilling){
            return
        }
        if((!currentBlock.isPrimitive && !currentBlock.isCompleted) || currentBlock == mainBlock){
            if(operation == Operations.SUBTRACT){
                val newBlock = CalculationBlock(currentBlock, true)
                newBlock.isNegative = true
                currentBlock.blocks.add(newBlock)
                currentBlock = newBlock
            }
            update()
            return
        }
        currentBlock.operation = operation
        currentBlock.isCompleted = true

        update()
    }

    fun appendFunction(stringPattern: String, function: (n1: Double) -> Double){
        if(currentBlock.requiresFilling){
            currentBlock.stringPattern = stringPattern
            currentBlock.resultFunction = function
            currentBlock.isPrimitive = false
            currentBlock.requiresFilling = false
            currentBlock.requiresBrackets = true
        }
        else if(!currentBlock.isPrimitive && currentBlock.blocks.isEmpty()){
            val newBlock = CalculationBlock(function, stringPattern, currentBlock,
                isPrimitive = false,
                requiresBrackets = true
            )
            currentBlock.blocks.add(newBlock)
            currentBlock = newBlock
        }
        else {
            if(currentBlock.operation == Operations.NONE)
            {
                currentBlock.operation = Operations.MULTIPLY
            }
            val newBlock = CalculationBlock(function, stringPattern, currentBlock.parentBlock,
                isPrimitive = false,
                requiresBrackets = true
            )
            currentBlock.parentBlock?.blocks?.add(newBlock)
            currentBlock = newBlock
        }
        update()
    }

    fun complete(){
        if(currentBlock.requiresBrackets && currentBlock.blocks.isEmpty()){
            return
        }
        if(currentBlock.parentBlock != null && currentBlock.parentBlock!!.requiresBrackets){
            currentBlock.parentBlock!!.isCompleted = true
            currentBlock = currentBlock.parentBlock!!
        }
        update()
    }

    fun setFloat(){
        if(currentBlock.operation != Operations.NONE || currentBlock.isCompleted || currentBlock.number.contains('.') || currentBlock.number.contains('e')){
            return
        }
        currentBlock.setFloat()
        update()
    }

    fun delete(){
        if(!currentBlock.isPrimitive && currentBlock.isCompleted && currentBlock.operation == Operations.NONE){
            currentBlock.delete()
            currentBlock = currentBlock.blocks.last()
        }
        else if(!currentBlock.delete()){
            val parent = currentBlock.parentBlock ?: return
            parent.blocks.remove(currentBlock)

            currentBlock = if(parent.blocks.count() == 0){
                parent
            } else{
                parent.blocks.last()
            }
        }

        update()
    }

    fun percent(){
        if(currentBlock.isPrimitive){
            var num = currentBlock.number.toDouble()
            num *= 0.01
            currentBlock.number = numToString(num)
            update()
        }
    }

    fun clear(){
        mainBlock = CalculationBlock(defaultFunction, "", null, false)
        currentBlock = mainBlock

        update()
    }

    @SuppressLint("SetTextI18n")
    private fun update(){
        if(mainBlock.blocks.isEmpty()){
            resultTextView?.visibility = GONE
            resultTextView?.text = "0"
            inputTextView?.text = "0"
            return
        }
        else{
            resultTextView?.visibility = VISIBLE
        }
        inputTextView?.text = mainBlock.toString()

        val result = try{
            evaluate()
        }
        catch (e: Exception){
            resultTextView?.text = "Error"
            return
        }
        if(result.isNaN()){
            resultTextView?.text = "Error"
            return
        }
        resultTextView?.text = "= " + numToString(result)
    }

    private fun evaluate() : Double{
        return mainBlock.evaluate()
    }

    fun setListeners(inputTextView: TextView, resultTextView: TextView){
        this.inputTextView = inputTextView
        this.resultTextView = resultTextView
        update()
    }

    fun factorial(n: Double) : Double{
        if (n < 0 || n.isNaN()){
            throw IllegalArgumentException()
        }

        if(n >= 1000){
            return Double.POSITIVE_INFINITY
        }
        val num = round(n)
        if(num == 0.0){
            return 1.0
        }
        return num * factorial(num - 1)
    }

    fun equals(){
        val seq = resultTextView?.text ?: return
        res = seq.toString().replace("= ", "")
        if(res.contains("Infinity") || res == "NaN" || res == "Error"){
            return
        }
        clear()
        val newBlock = CalculationBlock(defaultFunction, "", mainBlock, true)
        if(res[0] == '-'){
            newBlock.isNegative = true
            res = res.drop(1)
        }
        newBlock.number = res
        mainBlock.blocks.add(newBlock)
        currentBlock = newBlock
        update()
        resultTextView?.visibility = GONE
        resultTextView?.text = inputTextView?.text

    }

    private fun numToString(n: Double): String{
        return if(n < 1e10 && n > -1e10){
            val res = String.format(Locale.US,"%.5f", n).trimEnd('0')
            if (res.last() == '.'){
                res.dropLast(1)
            }
            else{
                res
            }

        }
        else{
            val res = String.format(Locale.US, "%.5e", n).replace("+", "").split('e' ).toMutableList()
            res[0] = res[0].trimEnd('0', '.')
            res.joinToString("e")
        }
    }
}
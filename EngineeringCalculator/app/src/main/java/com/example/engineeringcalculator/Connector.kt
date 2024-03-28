package com.example.engineeringcalculator

import android.os.Vibrator
import com.example.engineeringcalculator.calculations.Calculator

// Нужен для получения фрагментом ссылки на экземпляр класса Calculator из MainActivity.
interface Connector {
    fun getCalculator() : Calculator
    fun getVibrator() : Vibrator
    fun isDemo() : Boolean
}
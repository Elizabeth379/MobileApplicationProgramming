package com.example.engineeringcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Context
import android.content.res.Configuration
import android.content.pm.ActivityInfo
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.Toast
import android.os.Vibrator
import com.example.engineeringcalculator.calculations.Calculator

class MainActivity : AppCompatActivity(), Connector {
    companion object {
        private var calculator = Calculator()
    }

    private lateinit var inputText : TextView
    private lateinit var resultText : TextView
    private lateinit var btn_2nd: Button
    private lateinit var vibrator : Vibrator

    private fun changeMode() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            android.os.Handler().postDelayed({
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED },5000)
        }
        else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            android.os.Handler().postDelayed({
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED },5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isDemo())
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        inputText = findViewById(R.id.textViewInput)
        resultText = findViewById(R.id.textViewResult)
        calculator.setListeners(inputText, resultText)

        //2nd mode (scientific)
        btn_2nd = findViewById(R.id.btn_2nd)
        btn_2nd.setOnClickListener{
            if (!isDemo()) {
                changeMode()
            }
            else {
                vibrator.vibrate(100)
                Toast.makeText(this, "Sorry, you need to buy full version :(",Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btn_2nd.bringToFront()

        inputText.setMovementMethod(ScrollingMovementMethod())
    }

    fun getinputText(): TextView {
        return inputText
    }

    override fun getCalculator(): Calculator { //Interface function
        return calculator
    }

    override fun getVibrator(): Vibrator {
        return vibrator
    }

    override fun isDemo() : Boolean{
        val pInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
        val version = pInfo.versionName
        return "demo" in version
    }
}
package com.example.engineeringcalculator

import android.annotation.SuppressLint
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
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import com.example.engineeringcalculator.calculations.Operations

class MainActivity : AppCompatActivity(), Connector {
    companion object {
        private var calculator = Calculator()

        private const val SWIPE_MIN_DISTANCE = 130
        private const val SWIPE_MAX_DISTANCE = 300
        private const val SWIPE_MIN_VELOCITY = 200
    }

    private lateinit var inputText : TextView
    private lateinit var resultText : TextView
    private lateinit var btn_2nd: Button
    private lateinit var vibrator : Vibrator
    private lateinit var lSwipeDetector: GestureDetectorCompat
    private lateinit var mainLayout: ConstraintLayout


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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isDemo())
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        inputText = findViewById(R.id.textViewInput)
        resultText = findViewById(R.id.textViewResult)
        calculator.setListeners(inputText, resultText)

        mainLayout = findViewById(R.id.science)

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

        lSwipeDetector = GestureDetectorCompat(this, MyGestureListener())

        mainLayout.setOnTouchListener { _, event ->
            lSwipeDetector.onTouchEvent(event)
            true
        }
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val deltaX = e2.x - e1!!.x
            val deltaY = e2.y - e1.y
            val deltaXAbs = Math.abs(deltaX)
            val deltaYAbs = Math.abs(deltaY)

            if (deltaXAbs > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY) {
                if (deltaX > 0) {
                    calculator.appendOperation(Operations.ADD)
                    //Toast.makeText(this@MainActivity, "Swipe Right", Toast.LENGTH_SHORT).show()
                } else {
                    calculator.appendOperation(Operations.SUBTRACT)
                    //Toast.makeText(this@MainActivity, "Swipe Left", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            if (deltaYAbs > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_MIN_VELOCITY) {
                if (deltaY > 0) {
                    calculator.appendOperation(Operations.DIVIDE)
                    //Toast.makeText(this@MainActivity, "Swipe Down", Toast.LENGTH_SHORT).show()
                } else {
                    calculator.appendOperation(Operations.MULTIPLY)
                    //Toast.makeText(this@MainActivity, "Swipe Up", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            return false
        }
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
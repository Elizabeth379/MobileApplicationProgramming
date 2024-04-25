package com.example.engineeringcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.pm.ActivityInfo
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.Toast
import android.os.Vibrator
import com.example.engineeringcalculator.calculations.Calculator
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import com.example.engineeringcalculator.calculations.Operations
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity(), Connector, SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastTime: Long = 0
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var lastZ: Float = 0.0f
    private val shakeThreshold = 600

    companion object {
        private var calculator = Calculator()

        private const val SWIPE_MIN_DISTANCE = 130
        private const val SWIPE_MAX_DISTANCE = 300
        private const val SWIPE_MIN_VELOCITY = 200
    }

    private lateinit var inputText : TextView
    private lateinit var resultText : TextView
    private lateinit var btn_2nd: Button
    private lateinit var btn_theme: Button
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

        FirebaseApp.initializeApp(this)

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

        btn_theme = findViewById(R.id.btn_theme)
        btn_theme.setOnClickListener{
            val i = Intent(this, ThemeActivity::class.java)
            startActivity(i)
            finish()
        }
        btn_theme.bringToFront()

        inputText.setMovementMethod(ScrollingMovementMethod())

        lSwipeDetector = GestureDetectorCompat(this, MyGestureListener())

        mainLayout.setOnTouchListener { _, event ->
            lSwipeDetector.onTouchEvent(event)
            true
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Не требуется реализация
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            val diffTime = currentTime - lastTime
            if (diffTime > 100) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000
                if (speed > shakeThreshold) {
                    onShake()
                }
                lastX = x
                lastY = y
                lastZ = z
                lastTime = currentTime
            }
        }
    }

    private fun onShake() {
        Toast.makeText(this, "Device shook! Clearing!", Toast.LENGTH_SHORT).show()
        calculator.clear()
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
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
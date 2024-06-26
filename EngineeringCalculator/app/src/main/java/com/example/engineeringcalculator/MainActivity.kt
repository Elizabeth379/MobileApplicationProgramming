package com.example.engineeringcalculator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.text.method.ScrollingMovementMethod
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import com.example.engineeringcalculator.calculations.Calculator
import com.example.engineeringcalculator.calculations.Operations
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), Connector, SensorEventListener {
    val db = Firebase.firestore

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

    lateinit var inputText : TextView
    private lateinit var resultText : TextView
    private lateinit var btn_2nd: Button
    private lateinit var btn_theme: Button
    private lateinit var btn_history: Button
    private lateinit var vibrator : Vibrator
    private lateinit var lSwipeDetector: GestureDetectorCompat
    private lateinit var mainLayout: ConstraintLayout

    private lateinit var btn_0: Button
    private lateinit var btn_1: Button
    private lateinit var btn_2: Button
    private lateinit var btn_3: Button
    private lateinit var btn_4: Button
    private lateinit var btn_5: Button
    private lateinit var btn_6: Button
    private lateinit var btn_7: Button
    private lateinit var btn_8: Button
    private lateinit var btn_9: Button
    private lateinit var btn_dot: Button
    private lateinit var btn_back: Button
    private lateinit var btn_ac: Button
    private lateinit var btn_plus: Button
    private lateinit var btn_minus: Button
    private lateinit var btn_multiply: Button
    private lateinit var btn_division: Button
    private lateinit var btn_equal: Button
    private lateinit var btn_parenthesis_l: Button
    private lateinit var btn_parenthesis_r: Button

    private lateinit var btn_sin: Button
    private lateinit var btn_cos: Button
    private lateinit var btn_tan: Button
    private lateinit var btn_rad: Button
    private lateinit var btn_sqrt: Button
    private lateinit var btn_ln: Button
    private lateinit var btn_lg: Button
    private lateinit var btn_log: Button
    private lateinit var btn_powerMinOne: Button
    private lateinit var btn_expPowX: Button
    private lateinit var btn_square: Button
    private lateinit var btn_power: Button
    private lateinit var btn_module: Button
    private lateinit var btn_pi: Button
    private lateinit var btn_exp: Button


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

        btn_theme = findViewById(R.id.btn_theme)
        btn_theme.setOnClickListener{
            val i = Intent(this, ThemeActivity::class.java)
            startActivity(i)
            finish()
        }
        btn_theme.bringToFront()

        btn_history = findViewById(R.id.btn_history)
        btn_history.setOnClickListener{
            val j = Intent(this, HistoryActivity::class.java)
            startActivity(j)
            finish()
        }
        btn_history.bringToFront()

        inputText.setMovementMethod(ScrollingMovementMethod())

        lSwipeDetector = GestureDetectorCompat(this, MyGestureListener())

        mainLayout.setOnTouchListener { _, event ->
            lSwipeDetector.onTouchEvent(event)
            true
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        applySavedTheme()


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

    private fun applyTheme(themeId: Int) {
        val orientation = resources.configuration.orientation
        when (themeId) {
            1 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.base)

                val constraintLayout: ConstraintLayout = findViewById(R.id.science)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.cool_gradient)
                constraintLayout.background = gradientDrawable

                setButtonOneColor(R.color.orange)
                setButtonColor(R.color.tomato)
                setNumberColor(R.color.white)
                setOperationColor(R.color.light_orange)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.orange)
                }
            }
            2 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.green)
                val constraintLayout: ConstraintLayout = findViewById(R.id.science)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.green_gradient)
                constraintLayout.background = gradientDrawable

                setButtonOneColor(R.color.gr_butt)
                setButtonColor(R.color.purple)
                setNumberColor(R.color.breeze)
                setOperationColor(R.color.green)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.gr_butt)
                }
            }
            3 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.breeze)

                val constraintLayout: ConstraintLayout = findViewById(R.id.science)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.blue_gradient)
                constraintLayout.background = gradientDrawable

                setButtonOneColor(R.color.br_butt)
                setButtonColor(R.color.tomato)
                setNumberColor(R.color.moon)
                setOperationColor(R.color.breeze)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.br_butt)
                }
            }
            4 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.moon)
                val constraintLayout: ConstraintLayout = findViewById(R.id.science)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.yellow_gradient)
                constraintLayout.background = gradientDrawable

                setButtonOneColor(R.color.mo_butt)
                setButtonOneTextColor(R.color.black)
                setButtonColor(R.color.purple)
                setNumberColor(R.color.breeze)
                setOperationColor(R.color.moon)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.mo_butt)
                }
            }
            else -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.base)

                setButtonOneColor(R.color.orange)
                setButtonColor(R.color.tomato)
                setNumberColor(R.color.white)
                setOperationColor(R.color.light_orange)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.orange)
                }
            }
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


    private fun applySavedTheme() {

        db.collection("theme").document("Hz9l4wlRM1jtDmUQNS0p")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val dbThemeId = documentSnapshot.getLong("theme_id")?.toInt() ?: return@addOnSuccessListener
                    setAppTheme(dbThemeId)
                    applyTheme(dbThemeId)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity, "Failed to read theme ID from Firestore!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setAppTheme(themeId: Int) {
        when (themeId) {
            1 -> window.statusBarColor = ContextCompat.getColor(this, R.color.base)
            2 -> window.statusBarColor = ContextCompat.getColor(this, R.color.green)
            3 -> window.statusBarColor = ContextCompat.getColor(this, R.color.breeze)
            4 -> window.statusBarColor = ContextCompat.getColor(this, R.color.moon)

        }
    }

    fun setButtonOneColor(colorResId: Int) {
        btn_theme = findViewById(R.id.btn_theme)
        btn_theme.setBackgroundColor(getResources().getColor(colorResId))
        btn_history = findViewById(R.id.btn_history)
        btn_history.setBackgroundColor(getResources().getColor(colorResId))
        btn_2nd = findViewById(R.id.btn_2nd)
        btn_2nd.setBackgroundColor(getResources().getColor(colorResId))
        btn_equal = findViewById(R.id.btn_equal)
        btn_equal.setBackgroundColor(getResources().getColor(colorResId))
    }

    fun setButtonOneTextColor(colorResId: Int) {
        btn_theme = findViewById(R.id.btn_theme)
        btn_theme.setTextColor(getResources().getColor(colorResId))
        btn_history = findViewById(R.id.btn_history)
        btn_history.setTextColor(getResources().getColor(colorResId))
        btn_2nd = findViewById(R.id.btn_2nd)
        btn_2nd.setTextColor(getResources().getColor(colorResId))
        btn_equal = findViewById(R.id.btn_equal)
        btn_equal.setTextColor(getResources().getColor(colorResId))
    }

    fun setButtonColor(colorResId: Int) {
        btn_ac = findViewById(R.id.btn_ac)
        btn_ac.setTextColor(getResources().getColor(colorResId))
        btn_back = findViewById(R.id.btn_back)
        btn_back.setTextColor(getResources().getColor(colorResId))

    }

    fun setNumberColor(colorResId: Int) {
        btn_0 = findViewById(R.id.btn_0)
        btn_0.setTextColor(getResources().getColor(colorResId))
        btn_1 = findViewById(R.id.btn_1)
        btn_1.setTextColor(getResources().getColor(colorResId))
        btn_2 = findViewById(R.id.btn_2)
        btn_2.setTextColor(getResources().getColor(colorResId))
        btn_3 = findViewById(R.id.btn_3)
        btn_3.setTextColor(getResources().getColor(colorResId))
        btn_4 = findViewById(R.id.btn_4)
        btn_4.setTextColor(getResources().getColor(colorResId))
        btn_5 = findViewById(R.id.btn_5)
        btn_5.setTextColor(getResources().getColor(colorResId))
        btn_6 = findViewById(R.id.btn_6)
        btn_6.setTextColor(getResources().getColor(colorResId))
        btn_7 = findViewById(R.id.btn_7)
        btn_7.setTextColor(getResources().getColor(colorResId))
        btn_8 = findViewById(R.id.btn_8)
        btn_8.setTextColor(getResources().getColor(colorResId))
        btn_9 = findViewById(R.id.btn_9)
        btn_9.setTextColor(getResources().getColor(colorResId))
        btn_dot = findViewById(R.id.btn_dot)
        btn_dot.setTextColor(getResources().getColor(colorResId))
    }

    fun setOperationColor(colorResId: Int) {
        btn_multiply = findViewById(R.id.btn_multiply)
        btn_multiply.setTextColor(getResources().getColor(colorResId))
        btn_minus = findViewById(R.id.btn_minus)
        btn_minus.setTextColor(getResources().getColor(colorResId))
        btn_plus = findViewById(R.id.btn_plus)
        btn_plus.setTextColor(getResources().getColor(colorResId))
        btn_division = findViewById(R.id.btn_division)
        btn_division.setTextColor(getResources().getColor(colorResId))
        btn_parenthesis_l = findViewById(R.id.btn_parenthesis_l)
        btn_parenthesis_l.setTextColor(getResources().getColor(colorResId))
        btn_parenthesis_r = findViewById(R.id.btn_parenthesis_r)
        btn_parenthesis_r.setTextColor(getResources().getColor(colorResId))
    }

    fun setFuncColor(colorResId: Int) {
        btn_sin = findViewById(R.id.btn_sin)
        btn_sin.setTextColor(getResources().getColor(colorResId))

        btn_cos = findViewById(R.id.btn_cos)
        btn_cos.setTextColor(getResources().getColor(colorResId))

        btn_tan = findViewById(R.id.btn_tan)
        btn_tan.setTextColor(getResources().getColor(colorResId))

        btn_rad = findViewById(R.id.btn_rad)
        btn_rad.setTextColor(getResources().getColor(colorResId))

        btn_sqrt = findViewById(R.id.btn_sqrt)
        btn_sqrt.setTextColor(getResources().getColor(colorResId))

        btn_ln = findViewById(R.id.btn_ln)
        btn_ln.setTextColor(getResources().getColor(colorResId))

        btn_lg = findViewById(R.id.btn_lg)
        btn_lg.setTextColor(getResources().getColor(colorResId))


        btn_log = findViewById(R.id.btn_log)
        btn_log.setTextColor(getResources().getColor(colorResId))

        btn_powerMinOne = findViewById(R.id.btn_powerMinOne)
        btn_powerMinOne.setTextColor(getResources().getColor(colorResId))

        btn_expPowX = findViewById(R.id.btn_expPowX)
        btn_expPowX.setTextColor(getResources().getColor(colorResId))

        btn_square = findViewById(R.id.btn_square)
        btn_square.setTextColor(getResources().getColor(colorResId))

        btn_power = findViewById(R.id.btn_power)
        btn_power.setTextColor(getResources().getColor(colorResId))

        btn_module = findViewById(R.id.btn_module)
        btn_module.setTextColor(getResources().getColor(colorResId))

        btn_pi = findViewById(R.id.btn_pi)
        btn_pi.setTextColor(getResources().getColor(colorResId))

        btn_exp = findViewById(R.id.btn_exp)
        btn_exp.setTextColor(getResources().getColor(colorResId))
    }


}
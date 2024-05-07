package com.example.engineeringcalculator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ForgetPinActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    private lateinit var activityM: Intent
    private lateinit var activityA: Intent

    private lateinit var passwordET: EditText
    private lateinit var emailET: EditText

    private lateinit var bOne: Button
    private lateinit var bTwo: Button
    private lateinit var bThree: Button
    private lateinit var bFour: Button
    private lateinit var bFive: Button
    private lateinit var bSix: Button
    private lateinit var bSeven: Button
    private lateinit var bEight: Button
    private lateinit var bNine: Button
    private lateinit var bZero: Button
    private lateinit var bClear: Button
    private lateinit var bOK: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_pin)


        activityM = Intent(this, MainActivity::class.java)
        activityA = Intent(this, AuthenticationActivity::class.java)

        passwordET = findViewById(R.id.password_ed)
        emailET = findViewById(R.id.email_ed)

        bOne = findViewById(R.id.b_one)
        bTwo = findViewById(R.id.b_two)
        bThree = findViewById(R.id.b_three)
        bFour = findViewById(R.id.b_four)
        bFive = findViewById(R.id.b_five)
        bSix = findViewById(R.id.b_six)
        bSeven = findViewById(R.id.b_seven)
        bEight = findViewById(R.id.b_eight)
        bNine = findViewById(R.id.b_nine)
        bZero = findViewById(R.id.b_zero)
        bClear = findViewById(R.id.b_clear)
        bOK = findViewById(R.id.regButton)


        bOne.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "1")
        }

        bTwo.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "2")
        }

        bThree.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "3")
        }

        bFour.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "4")
        }

        bFive.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "5")
        }

        bSix.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "6")
        }

        bSeven.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "7")
        }

        bEight.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "8")
        }

        bNine.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "9")
        }

        bZero.setOnClickListener {
            val value: String = passwordET.text.toString()
            passwordET.setText(value + "0")
        }

        bClear.setOnClickListener {
            val value: String = passwordET.text.toString()
            if (value.isNotEmpty()) {
                val newValue = value.substring(0, value.length - 1)
                passwordET.setText(newValue)
                passwordET.setSelection(newValue.length)
            }
        }

        bOK.setOnClickListener {
            val email = emailET.text.toString()
            val newPassword = passwordET.text.toString()
            val reg_pin = Regex("\\d{4}")

            if (reg_pin.matches(newPassword) && changePassword(email, newPassword)) {
                Toast.makeText(this, "PIN successfully changed", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email is not found/PIN is wrong.", Toast.LENGTH_SHORT).show()
            }

        }

        applySavedTheme()
    }

    private fun changePassword(email: String, newPassword: String): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", "")
        if (savedEmail == email) {
            val editor = sharedPreferences.edit()
            editor.putString("pin", newPassword)
            editor.apply()
            return true
        }
        return false
    }



    private fun applySavedTheme() {

        db.collection("theme").document("Hz9l4wlRM1jtDmUQNS0p")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val dbThemeId = documentSnapshot.getLong("theme_id")?.toInt() ?: return@addOnSuccessListener
                changeTheme(dbThemeId)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@ForgetPinActivity, "Failed to read theme ID from Firestore!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun changeTheme(themeId: Int) {
        when (themeId) {
            1 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.base)
                val relativeLayout: RelativeLayout = findViewById(R.id.reg)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.cool_gradient)
                relativeLayout.background = gradientDrawable
            }

            2 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.green)
                val relativeLayout: RelativeLayout = findViewById(R.id.reg)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.green_gradient)
                relativeLayout.background = gradientDrawable

            }

            3 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.breeze)
                val relativeLayout: RelativeLayout = findViewById(R.id.reg)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.blue_gradient)
                relativeLayout.background = gradientDrawable

            }

            4 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.moon)
                val relativeLayout: RelativeLayout = findViewById(R.id.reg)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.yellow_gradient)
                relativeLayout.background = gradientDrawable


            }
        }
    }


}
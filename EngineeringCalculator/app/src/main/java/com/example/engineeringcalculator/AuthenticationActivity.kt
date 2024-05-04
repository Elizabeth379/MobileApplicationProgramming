package com.example.engineeringcalculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat


class AuthenticationActivity : AppCompatActivity() {
    private var isHaveBiometric: Boolean = true
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var activityM: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication)

        activityM = Intent(this, MainActivity::class.java)

        checkBiometricInDevice() // Тут проверяем есть ли вход по биометрии у пользователя

        if (isHaveBiometric) { // Тут проверка
            initializeButtonForBiometric() // тут инициализируем кнопку входа по биометрии
        }

    }

    private fun checkBiometricInDevice() {
        val biometricManager = BiometricManager.from(this)
        val buttonOpenBiometric = findViewById<Button>(R.id.biometrics)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                buttonOpenBiometric.visibility = View.VISIBLE
                isHaveBiometric = true
            }

            else -> {
                buttonOpenBiometric.visibility = View.GONE
                isHaveBiometric = false
            }
        }
    }

    private fun initializeButtonForBiometric() {
        val buttonBiometric = findViewById<Button>(R.id.biometrics)

        biometricPrompt = BiometricPrompt(this@AuthenticationActivity, ContextCompat.getMainExecutor(this), object:androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                val cryptoObject = result.cryptoObject
                startActivity(activityM)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Toast.makeText(this@AuthenticationActivity, "Authentication error", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                Toast.makeText(this@AuthenticationActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        }
        )

        buttonBiometric.setOnClickListener {
            biometricPrompt.authenticate(createBiometricPromptInfo())
        }

    }

    private fun createBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorization")
            .setNegativeButtonText("Cancel")
            .build()
    }
}
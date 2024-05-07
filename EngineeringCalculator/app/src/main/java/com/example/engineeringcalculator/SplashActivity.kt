package com.example.engineeringcalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)

        if (isRegistered) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(
                    Intent(
                        this@SplashActivity,
                        AuthenticationActivity::class.java
                    )
                )
                finish()
            }, 2000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(
                    Intent(
                        this@SplashActivity,
                        RegistrationActivity::class.java
                    )
                )
                finish()
            }, 2000)
        }

    }
}
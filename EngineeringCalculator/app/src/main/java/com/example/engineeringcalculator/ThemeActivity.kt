package com.example.engineeringcalculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Button


class ThemeActivity : AppCompatActivity() {

    private lateinit var btn_theme1: Button
    private lateinit var btn_theme2: Button
    private lateinit var btn_theme3: Button
    private lateinit var btn_theme4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theme_selection)


        btn_theme1 = findViewById(R.id.btn_theme1)
        btn_theme1.setOnClickListener{

        }
        btn_theme2 = findViewById(R.id.btn_theme2)
        btn_theme2.setOnClickListener{

        }
        btn_theme3 = findViewById(R.id.btn_theme3)
        btn_theme3.setOnClickListener{

        }
        btn_theme4 = findViewById(R.id.btn_theme4)
        btn_theme4.setOnClickListener{

        }

    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
        super.onBackPressed()
    }
}
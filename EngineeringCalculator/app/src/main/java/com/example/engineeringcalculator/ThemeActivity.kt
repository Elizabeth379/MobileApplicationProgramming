package com.example.engineeringcalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.engineeringcalculator.R
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ThemeActivity : AppCompatActivity() {

    val db = Firebase.firestore

    private lateinit var btn_theme1: Button
    private lateinit var btn_theme2: Button
    private lateinit var btn_theme3: Button
    private lateinit var btn_theme4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theme_selection)


        btn_theme1 = findViewById(R.id.btn_theme1)
        btn_theme1.setOnClickListener{
            changeTheme(1)
        }
        btn_theme2 = findViewById(R.id.btn_theme2)
        btn_theme2.setOnClickListener{
            changeTheme(2)

        }
        btn_theme3 = findViewById(R.id.btn_theme3)
        btn_theme3.setOnClickListener{
            changeTheme(3)
        }
        btn_theme4 = findViewById(R.id.btn_theme4)
        btn_theme4.setOnClickListener{
            changeTheme(4)
        }

    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
        super.onBackPressed()
    }

    private fun changeTheme(themeId: Int) {
        when (themeId) {
            1 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.base)
                val constraintLayout: ConstraintLayout = findViewById(R.id.laymain)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.cool_gradient)
                constraintLayout.background = gradientDrawable

                btn_theme1 = findViewById(R.id.btn_theme1)
                btn_theme1.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
                btn_theme2 = findViewById(R.id.btn_theme2)
                btn_theme2.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
                btn_theme3 = findViewById(R.id.btn_theme3)
                btn_theme3.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
                btn_theme4 = findViewById(R.id.btn_theme4)
                btn_theme4.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))

            }

            2 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.green)
                val constraintLayout: ConstraintLayout = findViewById(R.id.laymain)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.green_gradient)
                constraintLayout.background = gradientDrawable

                btn_theme1 = findViewById(R.id.btn_theme1)
                btn_theme1.setBackgroundColor(ContextCompat.getColor(this, R.color.gr_butt))
                btn_theme2 = findViewById(R.id.btn_theme2)
                btn_theme2.setBackgroundColor(ContextCompat.getColor(this, R.color.gr_butt))
                btn_theme3 = findViewById(R.id.btn_theme3)
                btn_theme3.setBackgroundColor(ContextCompat.getColor(this, R.color.gr_butt))
                btn_theme4 = findViewById(R.id.btn_theme4)
                btn_theme4.setBackgroundColor(ContextCompat.getColor(this, R.color.gr_butt))

            }

            3 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.breeze)
                val constraintLayout: ConstraintLayout = findViewById(R.id.laymain)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.blue_gradient)
                constraintLayout.background = gradientDrawable

                btn_theme1 = findViewById(R.id.btn_theme1)
                btn_theme1.setBackgroundColor(ContextCompat.getColor(this, R.color.br_butt))
                btn_theme2 = findViewById(R.id.btn_theme2)
                btn_theme2.setBackgroundColor(ContextCompat.getColor(this, R.color.br_butt))
                btn_theme3 = findViewById(R.id.btn_theme3)
                btn_theme3.setBackgroundColor(ContextCompat.getColor(this, R.color.br_butt))
                btn_theme4 = findViewById(R.id.btn_theme4)
                btn_theme4.setBackgroundColor(ContextCompat.getColor(this, R.color.br_butt))

            }

            4 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.moon)
                val constraintLayout: ConstraintLayout = findViewById(R.id.laymain)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.yellow_gradient)
                constraintLayout.background = gradientDrawable

                btn_theme1 = findViewById(R.id.btn_theme1)
                btn_theme1.setBackgroundColor(ContextCompat.getColor(this, R.color.mo_butt))
                btn_theme2 = findViewById(R.id.btn_theme2)
                btn_theme2.setBackgroundColor(ContextCompat.getColor(this, R.color.mo_butt))
                btn_theme3 = findViewById(R.id.btn_theme3)
                btn_theme3.setBackgroundColor(ContextCompat.getColor(this, R.color.mo_butt))
                btn_theme4 = findViewById(R.id.btn_theme4)
                btn_theme4.setBackgroundColor(ContextCompat.getColor(this, R.color.mo_butt))
            }
        }
    }
}
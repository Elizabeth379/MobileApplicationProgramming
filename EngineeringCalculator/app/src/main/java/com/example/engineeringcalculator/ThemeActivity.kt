package com.example.engineeringcalculator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.engineeringcalculator.R
import android.view.View
import android.widget.Toast
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
            setAndReturnAppTheme(1)
        }
        btn_theme2 = findViewById(R.id.btn_theme2)
        btn_theme2.setOnClickListener{
            setAndReturnAppTheme(2)

        }
        btn_theme3 = findViewById(R.id.btn_theme3)
        btn_theme3.setOnClickListener{
            setAndReturnAppTheme(3)
        }
        btn_theme4 = findViewById(R.id.btn_theme4)
        btn_theme4.setOnClickListener{
            setAndReturnAppTheme(4)
        }

        applySavedTheme()

    }

    private fun applySavedTheme() {

        db.collection("theme").document("Hz9l4wlRM1jtDmUQNS0p")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val dbThemeId = documentSnapshot.getLong("theme_id")?.toInt() ?: return@addOnSuccessListener
                changeTheme(dbThemeId)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@ThemeActivity, "Failed to read theme ID from Firestore!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
        super.onBackPressed()
    }

    private fun setAndReturnAppTheme(themeId: Int) {
        val themeData = hashMapOf("theme_id" to themeId)

        db.collection("theme")
            .document("Hz9l4wlRM1jtDmUQNS0p")
            .set(themeData)
            .addOnSuccessListener {
                Toast.makeText(this@ThemeActivity, "Theme ID successfully written to Firestore!", Toast.LENGTH_SHORT).show()
                changeTheme(themeId)
            }
            .addOnFailureListener {
                Toast.makeText(this@ThemeActivity, "Theme ID NOT successfully written to Firestore!", Toast.LENGTH_SHORT).show()
            }
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
                btn_theme1.setTextColor(ContextCompat.getColor(this, R.color.black))
                btn_theme2 = findViewById(R.id.btn_theme2)
                btn_theme2.setBackgroundColor(ContextCompat.getColor(this, R.color.mo_butt))
                btn_theme2.setTextColor(ContextCompat.getColor(this, R.color.black))
                btn_theme3 = findViewById(R.id.btn_theme3)
                btn_theme3.setBackgroundColor(ContextCompat.getColor(this, R.color.mo_butt))
                btn_theme3.setTextColor(ContextCompat.getColor(this, R.color.black))
                btn_theme4 = findViewById(R.id.btn_theme4)
                btn_theme4.setBackgroundColor(ContextCompat.getColor(this, R.color.mo_butt))
                btn_theme4.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }
    }
}
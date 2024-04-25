package com.example.engineeringcalculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HistoryActivity : AppCompatActivity() {

    val db = Firebase.firestore

    private lateinit var historyTV: TextView
    private lateinit var bClear: Button
    private lateinit var data: StringBuilder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)

        historyTV = findViewById(R.id.history_text_view)
        bClear = findViewById(R.id.btn_clear)

        loadFirestoreData()


        bClear.setOnClickListener {
            val collectionRef = db.collection("results")
            collectionRef.get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.delete()
                }
                Toast.makeText(this, "Documents cleared", Toast.LENGTH_SHORT).show()
            }
            loadFirestoreData()
        }

        applySavedTheme()


    }

    private fun loadFirestoreData() {
        val resultsCollection = db.collection("results")

        resultsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                data = StringBuilder()
                for (document in documents) {
                    val result = document.data["expression"].toString()

                    data.append(result + "\n")
                }

                historyTV.text = data.toString()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@HistoryActivity,
                    "Collection is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
        super.onBackPressed()
    }

    private fun applySavedTheme() {

        db.collection("theme").document("Hz9l4wlRM1jtDmUQNS0p")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val dbThemeId = documentSnapshot.getLong("theme_id")?.toInt() ?: return@addOnSuccessListener
                changeTheme(dbThemeId)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@HistoryActivity, "Failed to read theme ID from Firestore!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun changeTheme(themeId: Int) {
        when (themeId) {
            1 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.base)
                val relativeLayout: RelativeLayout = findViewById(R.id.hayleys)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.cool_gradient)
                relativeLayout.background = gradientDrawable
            }

            2 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.green)
                val relativeLayout: RelativeLayout = findViewById(R.id.hayleys)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.green_gradient)
                relativeLayout.background = gradientDrawable

            }

            3 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.breeze)
                val relativeLayout: RelativeLayout = findViewById(R.id.hayleys)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.blue_gradient)
                relativeLayout.background = gradientDrawable

            }

            4 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.moon)
                val relativeLayout: RelativeLayout = findViewById(R.id.hayleys)
                val gradientDrawable = ContextCompat.getDrawable(this, R.drawable.yellow_gradient)
                relativeLayout.background = gradientDrawable


            }
        }
    }
}
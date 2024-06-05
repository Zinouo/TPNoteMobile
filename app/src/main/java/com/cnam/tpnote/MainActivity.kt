package com.cnam.tpnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var database: QuizDatabase
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = QuizDatabase.getDatabase(this)
        username = intent.getStringExtra("USERNAME") ?: "Unknown"

        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)
        welcomeTextView.text = "Welcome, $username!"

        loadCategories()

        val leaderboardButton = findViewById<Button>(R.id.leaderboardButton)
        leaderboardButton.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }
    }

    private fun loadCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val categories = database.quizDao().getCategories()
            withContext(Dispatchers.Main) {
                displayCategories(categories)
            }
        }
    }

    private fun displayCategories(categories: List<Category>) {
        val buttonContainer = findViewById<LinearLayout>(R.id.buttonContainer)
        categories.forEach { category ->
            val button = Button(this).apply {
                text = category.name
                setOnClickListener {
                    val intent = Intent(this@MainActivity, QuizActivity::class.java)
                    intent.putExtra("CATEGORY_ID", category.id)
                    intent.putExtra("USERNAME", username)
                    startActivity(intent)
                }
            }
            buttonContainer.addView(button)
        }
    }
}

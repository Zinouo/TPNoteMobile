package com.cnam.tpnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var database: QuizDatabase
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        database = QuizDatabase.getDatabase(this)
        username = intent.getStringExtra("USERNAME") ?: "Unknown"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadLeaderboard(recyclerView)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USERNAME", username) // Pass the username back to MainActivity
            startActivity(intent)
            finish()
        }
    }

    private fun loadLeaderboard(recyclerView: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val leaderboardEntries = database.quizDao().getLeaderboard()
                Log.d("LeaderboardActivity", "Loaded ${leaderboardEntries.size} entries")
                withContext(Dispatchers.Main) {
                    if (leaderboardEntries.isEmpty()) {
                        Log.d("LeaderboardActivity", "No entries found in the leaderboard")
                    } else {
                        recyclerView.adapter = LeaderboardAdapter(leaderboardEntries)
                    }
                }
            } catch (e: Exception) {
                Log.e("LeaderboardActivity", "Error loading leaderboard", e)
            }
        }
    }
}

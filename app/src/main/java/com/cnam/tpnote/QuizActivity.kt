package com.cnam.tpnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizActivity : AppCompatActivity() {

    private lateinit var database: QuizDatabase
    private var categoryId: Int = 0
    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex: Int = 0
    private var score: Int = 0
    private lateinit var username: String
    private lateinit var categoryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        database = QuizDatabase.getDatabase(this)
        categoryId = intent.getIntExtra("CATEGORY_ID", 0)
        username = intent.getStringExtra("USERNAME") ?: "Unknown"

        loadCategoryName()
        loadQuestions()
    }

    private fun loadCategoryName() {
        CoroutineScope(Dispatchers.IO).launch {
            categoryName = database.quizDao().getCategories().find { it.id == categoryId }?.name ?: "Unknown"
        }
    }

    private fun loadQuestions() {
        CoroutineScope(Dispatchers.IO).launch {
            questions = database.quizDao().getQuestionsForCategory(categoryId)
            withContext(Dispatchers.Main) {
                displayQuestion(questions.firstOrNull())
            }
        }
    }

    private fun displayQuestion(question: Question?) {
        val questionTextView = findViewById<TextView>(R.id.questionTextView)
        val answersContainer = findViewById<LinearLayout>(R.id.answersContainer)

        if (question != null) {
            questionTextView.text = question.text
            answersContainer.removeAllViews()

            question.options.forEachIndexed { index, option ->
                val button = Button(this).apply {
                    text = option
                    setOnClickListener {
                        checkAnswer(question, index)
                    }
                }
                answersContainer.addView(button)
            }
        } else {
            questionTextView.text = "No questions available"
        }
    }

    private fun checkAnswer(question: Question, selectedAnswerIndex: Int) {
        val isCorrect = question.correctAnswer == selectedAnswerIndex
        val message = if (isCorrect) {
            score += 10
            "Correct! Your score: $score"
        } else {
            "Wrong! Your score: $score"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // Load the next question or show the score screen
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayQuestion(questions[currentQuestionIndex])
        } else {
            // Save the score to the leaderboard and show the score screen
            CoroutineScope(Dispatchers.IO).launch {
                database.quizDao().insertLeaderboardEntry(
                    LeaderboardEntry(userName = username, category = categoryName, score = score)
                )
            }
            Toast.makeText(this, "Quiz finished! Your final score: $score", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("SCORE", score)
            intent.putExtra("USERNAME", username) // Pass the username to ScoreActivity
            startActivity(intent)
            finish()
        }
    }
}

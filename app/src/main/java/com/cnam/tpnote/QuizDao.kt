package com.cnam.tpnote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface QuizDao {

    @Insert
    fun insertCategory(category: Category): Long

    @Insert
    fun insertQuestion(question: Question): Long

    @Query("SELECT * FROM categories")
    fun getCategories(): List<Category>

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    fun getQuestionsForCategory(categoryId: Int): List<Question>

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): List<Question>

    @Query("DELETE FROM categories")
    fun clearCategories()

    @Query("DELETE FROM questions")
    fun clearQuestions()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'categories' OR name = 'questions'")
    fun clearSequence()

    @Insert
    fun insertLeaderboardEntry(entry: LeaderboardEntry): Long

    @Query("SELECT * FROM leaderboard ORDER BY score DESC")
    fun getLeaderboard(): List<LeaderboardEntry>

    @Transaction
    suspend fun clearAll() {
        clearCategories()
        clearQuestions()
        clearSequence()
    }
}

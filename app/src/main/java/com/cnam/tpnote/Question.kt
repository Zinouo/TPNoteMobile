package com.cnam.tpnote

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["categoryId"])]  // Ajouter un index pour categoryId
)
@TypeConverters(Converters::class)
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val options: List<String>,
    val correctAnswer: Int,
    val categoryId: Int
)

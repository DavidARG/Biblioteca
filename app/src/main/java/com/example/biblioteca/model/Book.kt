package com.example.biblioteca.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "book_table")
data class Book (
    @ColumnInfo(name = "image")
    val image: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "publisher")
    val publisher: String,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "genre")
    val genre: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
): Serializable
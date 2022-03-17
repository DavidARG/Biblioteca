package com.example.biblioteca.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.biblioteca.model.Book

@Dao
interface BookDAO {

    @Query("SELECT * FROM book_table ORDER BY title ASC")
    fun readAllData(): LiveData<List<Book>>

    @Insert
    fun addBook(vararg contact: Book):List<Long>

    @Query("SELECT * FROM book_table WHERE id = :id")
    fun getById(id: Int): LiveData<Book>

    @Update
    fun updateBook(contact: Book)

    @Query("SELECT * FROM book_table WHERE title LIKE '%' || :searchQuery || '%' OR author LIKE '%' || :searchQuery || '%' OR publisher LIKE '%' || :searchQuery || '%' OR year LIKE '%' || :searchQuery || '%'")
    fun searchDatabase(searchQuery: String): LiveData<List<Book>>

    @Delete
    fun delete(contact: Book)
}
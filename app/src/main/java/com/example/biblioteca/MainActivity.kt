package com.example.biblioteca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.biblioteca.data.BookDatabase
import com.example.biblioteca.model.Book

class MainActivity : AppCompatActivity() {
    private lateinit var database: BookDatabase
    private var bookList = emptyList<Book>()
    private var addBookButton : Button? = null
    private var listView: ListView? = null
    private var searchV: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.Theme_Biblioteca)
        addBookButton = findViewById(R.id.addmain_btn)
        listView= findViewById(R.id.book_lv)
        searchV = findViewById(R.id.book_sv)

        database = BookDatabase.getDatabase(this)

        addBookButton!!.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }

        listView!!.setOnItemClickListener  { parent, view, position, id ->
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("id", bookList[position].id)
            startActivity(intent)
        }

        initList()
        searchDatabase()
    }

    private fun initList() {
        database.bookDAO().readAllData().observe(this) {
            bookList = it
            val adapter = ListAdapter(this, bookList)
            listView!!.adapter = adapter
        }
    }

    private fun searchDatabase() {
        searchV?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(database.bookDAO().searchDatabase(query))
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText == ""){
                    search(database.bookDAO().searchDatabase(newText))
                }
                return false
            }
        })
    }

    private fun search(bookLiveData: LiveData<List<Book>>) {
        bookLiveData.observe(this@MainActivity) {
            if (it.isEmpty()) {
                Toast.makeText(this@MainActivity, "Sin resultados", Toast.LENGTH_SHORT).show()
            }
            else {
                bookList = it
                val adapter = ListAdapter(this@MainActivity, bookList)
                listView!!.adapter = adapter
            }
        }
    }

}
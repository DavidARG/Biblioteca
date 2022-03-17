package com.example.biblioteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.biblioteca.data.BookDatabase
import com.example.biblioteca.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private lateinit var database:BookDatabase
    private lateinit var bookLiveData: LiveData<Book>
    private lateinit var book: Book
    private var edit = false

    var editImageIV : ImageView? = null
    var editTitleTV : TextView? = null
    var editAuthorTV : TextView? = null
    var editPublisherTV : TextView? = null
    var editYearTV : TextView? = null
    var editPriceTV : TextView? = null
    var editGenreTV : TextView? = null
    var editBookButton : Button? = null
    var addImageButton : Button? = null

    private var uriImage: Uri? = null
    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        database = BookDatabase.getDatabase(this)
        editImageIV = findViewById(R.id.book_iv)
        editTitleTV = findViewById(R.id.add_title_tv)
        editAuthorTV = findViewById(R.id.add_author_tv)
        editPublisherTV = findViewById(R.id.add_publisher_tv)
        editYearTV = findViewById(R.id.add_year_tv)
        editPriceTV = findViewById(R.id.add_price_tv)
        editGenreTV = findViewById(R.id.add_genre_tv)

        editBookButton = findViewById(R.id.add_btn)
        addImageButton = findViewById(R.id.addImage_Btn)


        val bookId = getBook()

        if(edit) {
            editBookButton!!.text = "Editar contacto"
        }

        addImageButton!!.setOnClickListener {
            pickImageGallery()
        }

        editBookButton!!.setOnClickListener {
            if (edit) {
                editBook(bookId)
            }
            else {
                createBook()
            }
        }
    }


    private fun getBook(): Int {

        val bookId = intent.getIntExtra("id", 0)
        if (bookId != 0) {
            bookLiveData = database.bookDAO().getById(bookId)
            bookLiveData.observe(this, Observer {
                book = it
                editTitleTV?.text = book.title
                editAuthorTV?.text = book.author
                editPublisherTV?.text = book.publisher
                editYearTV?.text = book.year.toString()
                editPriceTV?.text = book.price.toString()
                editGenreTV?.text = book.genre

                if (book.image != 0) {
                    val uriImage = ControllerImage.getUri(this, book.id.toLong())
                    editImageIV?.setImageURI(uriImage)
                }
            })
            edit = true
        }
        return bookId
    }

    private fun pickImageGallery() {
        ControllerImage.selectForGallery(this, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            uriImage = data!!.data
            editImageIV?.setImageURI(uriImage)
        }
    }

    private fun createBook() {
        val title = editTitleTV?.text.toString()
        val author = editAuthorTV?.text.toString()
        val publisher = editPublisherTV?.text.toString()
        val year = editYearTV?.text.toString()
        val price = editPriceTV?.text.toString()
        val genre = editGenreTV?.text.toString()
        val book = Book(R.drawable.missing, title , author, publisher, year.toInt(), price.toDouble(), genre)
        CoroutineScope(Dispatchers.IO).launch {
            val id = database.bookDAO().addBook(book)[0]
            uriImage?.let {
                ControllerImage.save(this@EditActivity, id, it)
            }
        }
        this@EditActivity.finish()
    }

    private fun editBook(id: Int) {
        val title = editTitleTV?.text.toString()
        val author = editAuthorTV?.text.toString()
        val publisher = editPublisherTV?.text.toString()
        val year = editYearTV?.text.toString()
        val price = editPriceTV?.text.toString()
        val genre = editGenreTV?.text.toString()
        val book = Book(R.drawable.missing, title , author, publisher, year.toInt(), price.toDouble(), genre, id)
        CoroutineScope(Dispatchers.IO).launch {
            database.bookDAO().updateBook(book)
            uriImage?.let {
                ControllerImage.save(this@EditActivity, id.toLong(), it)
            }
        }
        this@EditActivity.finish()
    }
}
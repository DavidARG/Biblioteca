package com.example.biblioteca

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.biblioteca.data.BookDatabase
import com.example.biblioteca.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListAdapter(private val mContext: Context, private val bookList: List<Book>):
    ArrayAdapter<Book>(mContext, 0, bookList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false)
        val book = bookList[position]

        layout.findViewById<TextView>(R.id.title_tv).text = book.title
        val uriImage = ControllerImage.getUri(context, book.id.toLong())
        layout.findViewById<ImageView>(R.id.imageView).setImageURI(uriImage)

        layout.findViewById<Button>(R.id.delete_btn).setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(mContext)
            dialogBuilder.setMessage("¿Está seguro que desea eliminar el libro: ${book.title} ?").setCancelable(false)
                .setPositiveButton("Sí", DialogInterface.OnClickListener {
                        dialog, id ->
                    val database = BookDatabase.getDatabase(mContext)
                    val book = Book(book.image, book.title, book.author, book.publisher, book.year, book.id)
                    CoroutineScope(Dispatchers.IO).launch {
                        database.bookDAO().delete(book)
                        uriImage?.let {
                            ControllerImage.delete(mContext, book.id.toLong())
                        }
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Alerta")
            alert.show()
        }

        return layout
    }
}
package com.adragon.filmslist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.DialogInterface
import android.database.MatrixCursor
import android.graphics.Canvas
import android.os.Bundle
import android.provider.BaseColumns
import android.text.InputType
import android.util.Log
import android.widget.CursorAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adragon.filmslist.database.MovieDatabase
import com.adragon.filmslist.helpres.MovieAdapter
import com.adragon.filmslist.helpres.Request
import com.adragon.filmslist.helpres.Utility
import com.adragon.filmslist.movie.MovieViewModel
import com.adragon.filmslist.movie.info.Movie
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var filmsListRecyclerView: RecyclerView
    private lateinit var listSizeTextView: TextView

    private var suggestions = emptyList<Movie>()

    private val req = Request()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var db: MovieDatabase

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        listSizeTextView = findViewById(R.id.listSizeTextView)

        filmsListRecyclerView = findViewById(R.id.filmsRecyclerView)
        filmsListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

        val icon1 = SearchManager.SUGGEST_COLUMN_ICON_1
        val text1 = SearchManager.SUGGEST_COLUMN_TEXT_1

        val from = arrayOf(icon1, text1)
        val to = intArrayOf(R.id.filmImageImageView, R.id.filmNameTextView)
        val cursorAdapter = SimpleCursorAdapter(
            applicationContext, R.layout.movie_item,
            null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        searchView.suggestionsAdapter = cursorAdapter

        val movieAdapter = MovieAdapter(applicationContext)
        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        movieViewModel.movies.observe(this) { movies ->
            movieAdapter.fillData(movies)
            filmsListRecyclerView.adapter = movieAdapter
            listSizeTextView.text = "Добавлено фильмов/сериалов: ${movies.size}"
        }

        movieAdapter.setOnItemClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Enter rating")
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)
            builder.setPositiveButton("OK") { _, _ ->

                val newRating = input.text.toString().toInt().coerceIn(0..10)

                Log.d("mytag", "input text - $newRating")
                movieViewModel.changeRating(it, newRating)
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            builder.show()
        }

        lifecycleScope.launch {
            setIemTouchHelper(movieAdapter).attachToRecyclerView(filmsListRecyclerView)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false

                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, icon1, text1))

                suggestions.forEachIndexed { i, s ->
                    if (s.title.contains(query, true)) {
                        cursor.addRow(
                            arrayOf(i, R.drawable.decorator_delete_asset, s.title)
                        )
                        Log.d("mytag", "found one more mathch - ${s.title}")
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }

            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text.isNullOrEmpty()) {
                    suggestions = emptyList()
                    return false
                }

                lifecycleScope.launch {
                    suggestions = Utility().getMovie(req, text)
                    Log.d("mytag", "suggestions:")
                    suggestions.forEach {
                        Log.d("mytag", "${it.title} - ${it.rating}")
                    }
                    onQueryTextChange(text)
                }
                return false
            }
        })
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val clickedMovie = suggestions[position]
                Log.d("mytag", "clicked $position sug - $clickedMovie")
                movieViewModel.insert(clickedMovie)
                return true
            }
        })
    }


    private fun setIemTouchHelper(movieAdapter: MovieAdapter) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = movieAdapter.getItem(viewHolder.bindingAdapterPosition) ?: return
                movieViewModel.delete(item)
            }

            override fun onChildDraw(
                c: Canvas, rView: RecyclerView, vh: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isActive: Boolean
            ) {
                val deleteColor = ContextCompat.getColor(
                    applicationContext,
                    R.color.delete_red
                )
                RecyclerViewSwipeDecorator.Builder(c, rView, vh, dX, dY, actionState, isActive)
                    .apply {
                        addBackgroundColor(deleteColor)
                        addActionIcon(R.drawable.decorator_delete_asset)
                    }
                    .create()
                    .decorate()

                super.onChildDraw(
                    c, rView,
                    vh, dX, dY, actionState, isActive
                )
            }
        })
}
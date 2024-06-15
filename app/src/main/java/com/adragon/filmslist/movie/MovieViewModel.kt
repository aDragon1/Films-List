package com.adragon.filmslist.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.adragon.filmslist.database.MovieDatabase
import com.adragon.filmslist.database.MovieRepository
import com.adragon.filmslist.movie.info.Movie
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    val movies: LiveData<List<Movie>>

    init {
        val movieDao = MovieDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao)
        movies = repository.getAllMovies()
    }

    fun getAllMovies() = repository.getAllMovies()

    fun insert(movie: Movie) = viewModelScope.launch {
        val founded = repository.getMovieByKey(movie.uniqueKey)
        if (founded.isEmpty())
            repository.insert(movie)
    }

    fun delete(movie: Movie) = viewModelScope.launch {
        repository.delete(movie)
    }
}

package com.adragon.filmslist.database

import androidx.lifecycle.LiveData
import com.adragon.filmslist.movie.info.Movie

class MovieRepository(private val movieDao: MovieDao?) {
    fun getAllMovies(): LiveData<List<Movie>> =
        movieDao!!.getAllMovies()

    suspend fun insert(movie: Movie) =
        movieDao!!.insert(movie)

    suspend fun update(movie: Movie) =
        movieDao!!.update(movie)

    suspend fun delete(movie: Movie) =
        movieDao!!.delete(movie.id)

    suspend fun getMovieByKey(keyToSearch: String) =
        movieDao!!.getMovieByKey(keyToSearch)

    suspend fun changeRating(keyToSearch: String, newRating:Int) =
        movieDao!!.changeRating(keyToSearch, newRating)

}

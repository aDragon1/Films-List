package com.adragon.filmslist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adragon.filmslist.movie.info.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM MOVIE_TABLE")
    fun getAllMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Query("DELETE FROM movie_table WHERE id = :id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM movie_table WHERE uniqueKey = :key")
    suspend fun getMovieByKey(key: String): List<Movie>

    @Query("UPDATE movie_table SET rank = :newRating WHERE uniqueKey = :key")
    suspend fun changeRating(key: String, newRating: Int): Int
}

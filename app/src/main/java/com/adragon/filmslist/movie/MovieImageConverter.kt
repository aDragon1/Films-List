package com.adragon.filmslist.movie

import androidx.room.TypeConverter
import com.adragon.filmslist.movie.info.MovieImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MovieImageConverter {
    @TypeConverter
    fun fromMovieImage(movieImage: MovieImage): String =
        Json.encodeToString(movieImage)


    @TypeConverter
    fun toMovieImage(stringRepresentation: String): MovieImage =
        Json.decodeFromString(stringRepresentation)
}

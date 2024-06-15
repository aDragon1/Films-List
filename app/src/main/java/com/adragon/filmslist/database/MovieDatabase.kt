package com.adragon.filmslist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adragon.filmslist.movie.MovieImageConverter
import com.adragon.filmslist.movie.info.Movie


@Database(entities = [Movie::class], version = 9, exportSchema = false)
@TypeConverters(MovieImageConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null
        private const val DBNAME = "MOVIE_TABLE"
        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    DBNAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun movieDao(): MovieDao
}


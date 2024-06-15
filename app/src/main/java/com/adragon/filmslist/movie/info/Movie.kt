package com.adragon.filmslist.movie.info

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adragon.filmslist.R
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.security.MessageDigest

@Serializable
@Entity(tableName = "movie_table")
data class Movie(
    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "rank")
    val rank: Int,

    @ColumnInfo(name = "id")
    val id: String,

    val image:Int = R.drawable.decorator_delete_asset,
//    @ColumnInfo(name = "movie_bitmap")
//    val movieBitmapByteArray: ByteArray,
//
//    @ColumnInfo(name = "movie_bitmap_size")
//    val movieBitmapByteArraySize: Int,

    @Transient
    @PrimaryKey
    val uniqueKey: String = generateUniqueKey(title, rank, id)
) {
    companion object {
        fun generateUniqueKey(title: String, rank: Int, id: String): String {
            val data = "$title$rank$id"
            val bytes = MessageDigest.getInstance("MD5").digest(data.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }

//    fun decodeBitmap(): Bitmap? =
//        BitmapFactory.decodeByteArray(movieBitmapByteArray, 0, movieBitmapByteArraySize)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (title != other.title) return false
        if (rank != other.rank) return false
        if (id != other.id) return false
//        if (!movieBitmapByteArray.contentEquals(other.movieBitmapByteArray)) return false
        return uniqueKey == other.uniqueKey
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + rank
        result = 31 * result + id.hashCode()
//        result = 31 * result + movieBitmapByteArray.contentHashCode()
        result = 31 * result + uniqueKey.hashCode()
        return result
    }
}
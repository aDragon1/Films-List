package com.adragon.filmslist.movie.info

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.ByteArrayOutputStream


@Serializable
data class MovieImage(
    val url: String,
    @Transient val imageBitmap: Bitmap? = null,
    var imageBitmapBytearray: ByteArray = byteArrayOf(),
    val height: Int,
    val width: Int
) {
    init {
        imageBitmapBytearray = encodeBitmap()
    }

    private fun encodeBitmap(): ByteArray {
        val stream = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    fun decodeBitmap(): Bitmap? =
        BitmapFactory.decodeByteArray(imageBitmapBytearray, 0, imageBitmapBytearray.size)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieImage

        if (url != other.url) return false
        if (imageBitmap != other.imageBitmap) return false
        if (!imageBitmapBytearray.contentEquals(other.imageBitmapBytearray)) return false
        if (height != other.height) return false
        return width == other.width
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + (imageBitmap?.hashCode() ?: 0)
        result = 31 * result + imageBitmapBytearray.contentHashCode()
        result = 31 * result + height
        result = 31 * result + width
        return result
    }
}
package com.adragon.filmslist.helpres

import android.app.SearchManager
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.CursorAdapter
import android.widget.SimpleCursorAdapter
import com.adragon.filmslist.R
import com.adragon.filmslist.movie.info.Movie
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class Utility {
    private fun takeStringIf(obj: JSONObject, key: String, defValue: String): String =
        obj.takeIf { obj.has(key) }?.getString(key) ?: defValue

    private fun takeIntIf(obj: JSONObject, key: String, defValue: Int): Int =
        obj.takeIf { obj.has(key) }?.getInt(key) ?: defValue

    suspend fun getMovie(req: Request, filmName: String): List<Movie> {
        val response = req.get("https://imdb146.p.rapidapi.com/v1/find/", filmName)
        return parseResponse(req, response)
    }


    private suspend fun parseResponse(req: Request, response: String): List<Movie> {
        fun encodeBitmap(imageBitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            return stream.toByteArray()
        }

//        val imageDownloader = ImageDownloader(req.getClient())
        val obj = JSONObject(response)
        val results = obj.getJSONObject("titleResults").getJSONArray("results")


        return coroutineScope {
            (0 until results.length()).map { i ->
                async {
                    Log.d("mytag", "$i")
                    val curObject = results.getJSONObject(i)
                    val title = takeStringIf(curObject, "titleNameText", "err")
                    val id = takeStringIf(curObject, "id", "err")
                    val rank = takeIntIf(curObject, "rank", -1)

                    Movie(title, rank, id)
                }
            }.map { it.await() }
        }
    }
}
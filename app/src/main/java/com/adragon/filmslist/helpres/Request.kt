package com.adragon.filmslist.helpres

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.URLProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Request {

    private val ktorHttpClient = HttpClient(Android) {
    }

    fun getClient() = ktorHttpClient

    suspend fun get(url: String, movieTitle: String): String {
        return withContext(Dispatchers.IO) {
            val response = ktorHttpClient.request(url) {
                url {
                    protocol = URLProtocol.HTTPS // Используйте HTTPS
                    header("x-rapidapi-key", "d3afd2a0f0msh96c5b639c3545f0p11bd96jsn9f8bbba3bf6a")
                    header("x-rapidapi-host", "imdb146.p.rapidapi.com")
                    parameter("query", movieTitle) // Параметр для названия фильма
                }
            }
            response.body()
        }
    }
}

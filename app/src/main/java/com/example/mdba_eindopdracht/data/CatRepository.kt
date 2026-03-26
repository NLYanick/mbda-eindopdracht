package com.example.mdba_eindopdracht.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CatRepository(private val context: Context) {
    private val BASE_URL: String = "https://api.thecatapi.com/v1"
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    // suspend makes an asynchronous function
    suspend fun fetchAllCatBreeds(): List<CatData> =
        suspendCancellableCoroutine { continuation -> // `suspendCancellableCoroutine` makes it possible to return values
            val url = "$BASE_URL/breeds"

            val request = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        continuation.resume(parseCats(response))
                    } catch (parseError: Exception) {
                        continuation.resumeWithException(parseError)
                    }
                },
                { error ->
                    continuation.resumeWithException(error)
                }
            )

            queue.add(request)
            continuation.invokeOnCancellation {
                request.cancel()
            }
        }

    private fun parseCats(response: JSONArray): List<CatData> {
        val items = mutableListOf<CatData>()
        for (i in 0 until response.length()) {
            val item = response.getJSONObject(i)
            items.add(
                CatData(
                    id = item.optString("id"),
                    name = item.optString("name"),
                    origin = item.optString("origin"),
                    wikipedia_url = item.optString("wikipedia_url"),
                    reference_image_id = item.optString("reference_image_id")
                )
            )
        }
        return items
    }
}
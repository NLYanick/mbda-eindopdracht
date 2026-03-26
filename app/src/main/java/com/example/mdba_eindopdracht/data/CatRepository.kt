package com.example.mdba_eindopdracht.data

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class CatRepository {
    final val BASE_URL: String = "https://api.thecatapi.com/v1"
    val queue: RequestQueue = Volley.newRequestQueue(this)

    fun fetchAllCatBreeds(): List<CatData> {
        val url = "$BASE_URL/breeds"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    return parseCats(response)
                } catch (parseError: Exception) {
                    print(parseError)
                    return null
                }
            },
            { error ->
                print(error)
                return null
            }
        )

        queue.add(request)
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
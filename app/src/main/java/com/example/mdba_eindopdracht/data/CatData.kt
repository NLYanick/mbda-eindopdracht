package com.example.mdba_eindopdracht.data

data class CatData (
    val id: String,
    val name: String,
    val origin: String?,
    val wikipedia_url: String?,
    val reference_image_id: String?,

    var isFavourite: Boolean = false  // Not from API
)


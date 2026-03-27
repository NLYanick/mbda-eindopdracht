package com.example.mdba_eindopdracht.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.size
import androidx.glance.text.Text
import com.example.mdba_eindopdracht.data.CatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URL

class CatWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content(context)
        }
    }

    @Composable
    private fun Content(context: Context) {
        val repository = CatRepository(context)
        var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
        var favoritesList by remember { mutableStateOf<List<String>>(emptyList()) }

        LaunchedEffect(Unit) {
            val prefs = context.getSharedPreferences("favourite_cats", Context.MODE_PRIVATE)
            val savedFavorites = prefs.getStringSet("favorites", emptySet()) ?: emptySet()
            favoritesList = savedFavorites.toList()

            if (favoritesList.isEmpty()) {
                return@LaunchedEffect
            }

            // Rotate through favorites every 5 seconds
            while (true) {
                try {
                    val randomFavoriteId = favoritesList.random()

                    val catImageUrl = repository.fetchCatImageUrl(randomFavoriteId)

                    catImageUrl?.let { url ->
                        imageBitmap = withContext(Dispatchers.IO) {
                            try {
                                val stream = URL(url).openStream()
                                BitmapFactory.decodeStream(stream)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                null
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                
                delay(5000)
            }
        }

        when {
            favoritesList.isEmpty() -> {
                Text(
                    text = "Add a favourite in the app to see an image",
                    modifier = GlanceModifier.background(Color.White)
                )
            }
            imageBitmap != null -> {
                Image(
                    provider = ImageProvider(imageBitmap!!),
                    contentDescription = "Random favorite cat",
                    modifier = GlanceModifier.size(150.dp)
                )
            }
            else -> {
                Text(
                    text = "Failed to load cat",
                    modifier = GlanceModifier.background(Color.White)
                )
            }
        }
    }
}
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
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.size
import androidx.glance.text.Text
import com.example.mdba_eindopdracht.data.CatRepository
import kotlinx.coroutines.Dispatchers
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
        var catImageUrl by remember { mutableStateOf<String?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(Unit) {
            isLoading = true
            try {
                catImageUrl = repository.fetchCatImageUrl("birm")
                catImageUrl?.let { url ->
                    imageBitmap = withContext(Dispatchers.IO) {
                        try {
                            val stream = URL(url).openStream()
                            BitmapFactory.decodeStream(stream)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error loading cat image: $e")
            } finally {
                isLoading = false
            }
        }

        if (isLoading) {
            Text(text = "Loading...")
        } else if (imageBitmap != null) {
            Image(
                provider = ImageProvider(imageBitmap!!),
                contentDescription = "Cat image",
                modifier = GlanceModifier.size(80.dp)
            )
        } else {
            Text(text = "Failed to load cat")
        }
    }
}
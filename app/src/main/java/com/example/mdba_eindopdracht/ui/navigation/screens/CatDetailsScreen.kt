package com.example.mdba_eindopdracht.ui.navigation.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.mdba_eindopdracht.ui.ViewModels.CatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun CatDetailsScreen(
    viewModel: CatViewModel,
) {

    val selectedCat by viewModel.selectedCat.collectAsState()
    val catImageUrl by viewModel.catImage.collectAsState()

    selectedCat?.let { cat ->
        Text(text = "Name: ${cat.name}")
    }

    if (catImageUrl != null) {
        // Load image directly from URL
        var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(catImageUrl) {
            bitmap = loadBitmapFromUrl(catImageUrl!!)
        }

        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "Cat image",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

suspend fun loadBitmapFromUrl(url: String): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val stream = URL(url).openStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}




package com.example.mdba_eindopdracht.ui.navigation.screens

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mdba_eindopdracht.data.CatData
import com.example.mdba_eindopdracht.ui.ViewModels.CatViewModel
import com.example.mdba_eindopdracht.ui.theme.StarColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun CatDetailsScreen(
    viewModel: CatViewModel,
    navController: NavController
) {

    val selectedCat by viewModel.selectedCat.collectAsState()
    val catImageUrl by viewModel.catImage.collectAsState()

    selectedCat?.let { cat ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailsTopRow(viewModel, navController, cat)

            Text(text = "Name: ${cat.name}")
            Text(text = "Origin: ${cat.origin}")
            cat.wikipedia_url?.let { url ->
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    navController.context.startActivity(intent)
                }) {
                    Text("Learn more")
                }
            }

            if (catImageUrl != null) {
                // Load image directly from URL
                var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

                LaunchedEffect(catImageUrl) {
                    catImageUrl?.let { url ->
                        bitmap = loadBitmapFromUrl(url)
                    }
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

@Composable
fun DetailsTopRow(
    viewModel: CatViewModel,
    navController: NavController,
    cat: CatData
)  {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = {
            navController.navigate(navController.previousBackStackEntry?.destination?.route ?: "list") {
                popUpTo("list") { inclusive = true }
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go Back",
                tint = Color.Gray
            )
        }

        Button(onClick = {
            viewModel.toggleFavourite()
        }) {
            Icon(
                imageVector = if (cat.isFavourite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (cat.isFavourite) "Unfavorite" else "Favorite",
                tint = if (cat.isFavourite) StarColor else Color.LightGray
            )
        }
    }
}


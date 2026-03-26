package com.example.mdba_eindopdracht.ui.navigation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mdba_eindopdracht.data.CatData
import com.example.mdba_eindopdracht.data.CatRepository

@Composable
fun CatListScreen(modifier: Modifier = Modifier,) {
    val repository = CatRepository(LocalContext.current)
    var cats by remember { mutableStateOf<List<CatData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            cats = repository.fetchAllCatBreeds()
        } catch (e: Exception) {
            print("Error in loading cats: $e")
        } finally {
            isLoading = false
        }
    }

    if(isLoading) {
        Text(text = "Loading cats...")
    } else {
        LazyColumn(modifier = modifier) {
            items(items = cats) { cat ->
                Row {
                    Text(text = cat.name)
                }
            }
        }
    }
}
package com.example.mdba_eindopdracht.ui.navigation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mdba_eindopdracht.data.CatData
import com.example.mdba_eindopdracht.data.CatRepository
import com.example.mdba_eindopdracht.ui.ViewModels.CatViewModel
import com.example.mdba_eindopdracht.ui.navigation.CatDetails
import com.example.mdba_eindopdracht.ui.theme.StarColor
import com.example.mdba_eindopdracht.ui.theme.isDarkMode
import com.example.mdba_eindopdracht.ui.theme.toggleDarkMode

@Composable
fun CatListScreen(
    modifier: Modifier = Modifier,
    viewModel: CatViewModel,
    navController: NavController
) {
    val repository = CatRepository(LocalContext.current)
    var cats by remember { mutableStateOf<List<CatData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    var filterFavourites by remember { mutableStateOf(false) }
    var filteredCats by remember { mutableStateOf<List<CatData>>(emptyList()) }

    LaunchedEffect(Unit) { // LaunchedEffect makes it possible to run suspend/async functions
        isLoading = true
        try {
            cats = repository.fetchAllCatBreeds()
            viewModel.restoreFavoritesToList(cats)

            filteredCats = cats
        } catch (e: Exception) {
            print("Error in loading cats: $e")
        } finally {
            isLoading = false
        }
    }

    if(isLoading) {
        Text(text = "Loading cats...")
    } else {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    filterFavourites = !filterFavourites

                    filteredCats = if(filterFavourites) {
                        cats.filter { it.isFavourite }
                    } else {
                        cats
                    }
                }) {
                    Text(text = if (filterFavourites) "Remove filter" else "Filter favorites")
                }

                Button(onClick = {
                    toggleDarkMode()
                }) {
                    Text(if (isDarkMode()) "Light" else "Dark")
                }
            }

            LazyColumn(modifier = modifier) {
                items(items = filteredCats) { cat ->
                    CatRow(cat, {
                        viewModel.selectCat(cat,repository);
                        navController.navigate(CatDetails.route)
                    })
                }
            }
        }
    }
}

@Composable
fun CatRow(cat: CatData, onClick: () -> Unit = {}) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color = Color.Gray)
            .padding(16.dp)
            .clickable(onClick = {onClick()}),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = cat.name)
        if(cat.isFavourite) {
            Box {
                Icon(imageVector = Icons.Filled.Star, tint = StarColor, contentDescription = "Favorite")
            }
        }
    }
}
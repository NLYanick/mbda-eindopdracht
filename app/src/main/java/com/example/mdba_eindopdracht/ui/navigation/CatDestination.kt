package com.example.mdba_eindopdracht.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mdba_eindopdracht.ui.navigation.screens.CatDetailsScreen
import com.example.mdba_eindopdracht.ui.navigation.screens.CatListScreen

interface CatDestination {
    val icon: ImageVector
    val route: String
}

object CatDetails : CatDestination {
    override val icon = Icons.Filled.Info
    override val route = "list"
}

object CatList : CatDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val route = "cat"
}

val catScreens = listOf(CatList, CatDetails)
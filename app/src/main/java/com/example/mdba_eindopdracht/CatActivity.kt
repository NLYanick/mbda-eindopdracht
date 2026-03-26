package com.example.mdba_eindopdracht

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mdba_eindopdracht.ui.ViewModels.CatViewModel
import com.example.mdba_eindopdracht.ui.navigation.CatDetails
import com.example.mdba_eindopdracht.ui.navigation.CatList
import com.example.mdba_eindopdracht.ui.navigation.screens.CatDetailsScreen
import com.example.mdba_eindopdracht.ui.navigation.screens.CatListScreen
import com.example.mdba_eindopdracht.ui.theme.MDBA_EindopdrachtTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatApp(modifier = Modifier)
        }
    }
}

@Composable
fun CatApp(modifier: Modifier = Modifier) {
    val viewModel: CatViewModel = viewModel()
    MDBA_EindopdrachtTheme {
        val navController = rememberNavController()

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = CatList.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = CatList.route) {
                    CatListScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable(route = CatDetails.route) {
                    CatDetailsScreen(
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatAppPreview() {
    MDBA_EindopdrachtTheme {
        CatApp()
    }
}
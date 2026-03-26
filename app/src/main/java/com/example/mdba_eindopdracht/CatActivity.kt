package com.example.mdba_eindopdracht

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mdba_eindopdracht.ui.navigation.CatDetails
import com.example.mdba_eindopdracht.ui.navigation.CatList
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
    MDBA_EindopdrachtTheme {
        val navController = rememberNavController()

        Scaffold(modifier = Modifier.fillMaxSize()
            , topBar = {
            Button(onClick = {navController.navigate(CatDetails.route) { launchSingleTop = true }}) { Text(text = "Details") }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = CatList.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = CatList.route) {
                    CatList.screen()
                }
                composable(route = CatDetails.route) {
                    CatDetails.screen()
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
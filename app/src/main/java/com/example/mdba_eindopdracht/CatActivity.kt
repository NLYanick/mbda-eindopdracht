package com.example.mdba_eindopdracht

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.mdba_eindopdracht.ui.theme.MDBA_EindopdrachtTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDBA_EindopdrachtTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CatApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CatApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Text(
        text = "Hello!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CatAppPreview() {
    MDBA_EindopdrachtTheme {
        CatApp()
    }
}
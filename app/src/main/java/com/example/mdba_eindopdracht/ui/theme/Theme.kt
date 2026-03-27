package com.example.mdba_eindopdracht.ui.theme

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext

private val _isDarkMode = mutableStateOf(true)
private var prefs: SharedPreferences? = null

fun initializeThemePreferences(context: Context) {
    prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    _isDarkMode.value = prefs?.getBoolean("dark_mode", true) ?: true
}

fun toggleDarkMode() {
    _isDarkMode.value = !_isDarkMode.value
    prefs?.edit()?.putBoolean("dark_mode", _isDarkMode.value)?.apply()
}

fun isDarkMode(): Boolean = _isDarkMode.value



private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = BackgroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = Cyan,
    secondary = PacificCyan,
    tertiary = FrozenLake,
    background = BackgroundWhite

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MDBA_EindopdrachtTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (_isDarkMode.value) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        _isDarkMode.value -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
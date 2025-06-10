package com.example.myhomemaxi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Coral,
    secondary = YellowSoft,
    background = LightGray,
    onPrimary = Color.White,
    onBackground = DarkText,
    onSecondary = Color.Black,
)

private val DarkColors = darkColorScheme(
    primary = Coral,
    secondary = YellowSoft,
    background = DarkBackground,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSecondary = Color.White,
)

@Composable
fun MyHomeMaxiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}

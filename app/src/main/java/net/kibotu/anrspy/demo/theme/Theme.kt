package net.kibotu.anrspy.demo.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val AppLightColorScheme = lightColorScheme(
    // M3 light Color parameters
)
val AppDarkColorScheme = darkColorScheme(
    // M3 dark Color parameters
)

val LocalCardElevation = staticCompositionLocalOf { Dp.Unspecified }

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val cardElevation = if (darkTheme) 4.dp else 0.dp
    CompositionLocalProvider(LocalCardElevation provides cardElevation) {
        val colorScheme = if (darkTheme) AppLightColorScheme else AppDarkColorScheme
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

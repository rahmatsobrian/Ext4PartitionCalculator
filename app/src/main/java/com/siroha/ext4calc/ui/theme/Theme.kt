package com.siroha.ext4calc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SirohaLightColors = lightColorScheme(
    primary = SirohaPrimary,
    onPrimary = SirohaOnPrimary,
    primaryContainer = SirohaPrimaryContainer,
    onPrimaryContainer = SirohaOnPrimaryContainer,
    secondary = SirohaSecondary,
    onSecondary = SirohaOnSecondary,
    secondaryContainer = SirohaSecondaryContainer,
    onSecondaryContainer = SirohaOnSecondaryContainer,
    background = SirohaBackground,
    onBackground = SirohaOnBackground,
    surface = SirohaSurface,
    onSurface = SirohaOnSurface,
    surfaceVariant = SirohaSurfaceVariant,
    onSurfaceVariant = SirohaOnSurfaceVariant,
    outline = SirohaOutline,
    error = SirohaError,
    onError = SirohaOnError,
)

private val SirohaDarkColors = darkColorScheme(
    primary = SirohaPrimaryDark,
    onPrimary = SirohaOnPrimaryDark,
    primaryContainer = SirohaPrimaryContainerDark,
    onPrimaryContainer = SirohaOnPrimaryContainerDark,
    secondary = SirohaSecondaryDark,
    onSecondary = SirohaOnSecondaryDark,
    secondaryContainer = SirohaSecondaryContainerDark,
    onSecondaryContainer = SirohaOnSecondaryContainerDark,
    background = SirohaBackgroundDark,
    onBackground = SirohaOnBackgroundDark,
    surface = SirohaSurfaceDark,
    onSurface = SirohaOnSurfaceDark,
    surfaceVariant = SirohaSurfaceVariantDark,
    onSurfaceVariant = SirohaOnSurfaceVariantDark,
    outline = SirohaOutlineDark,
    error = SirohaErrorDark,
    onError = SirohaOnErrorDark,
)

/**
 * Tema aplikasi: Material 3 + Material You (dynamic color).
 *
 * - Android 12+ (API 31+): pakai warna dinamis dari wallpaper user (dynamicColor).
 * - Android 10-11 (API 29-30): fallback ke palet "KernelSU Clean Light" (brand Siroha).
 * - Mendukung dark theme mengikuti sistem.
 */
@Composable
fun Ext4CalcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && supportsDynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> SirohaDarkColors
        else -> SirohaLightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SirohaTypography,
        content = content
    )
}

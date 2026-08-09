package com.siroha.ext4calc.ui.theme

import androidx.compose.ui.graphics.Color

// Palet dasar "KernelSU Clean Light" — dipakai sebagai fallback
// di perangkat yang belum mendukung dynamic color (Android 10-11).
val SirohaPrimary = Color(0xFF5672CD)
val SirohaOnPrimary = Color(0xFFFFFFFF)
val SirohaPrimaryContainer = Color(0xFFDCE2F7)
val SirohaOnPrimaryContainer = Color(0xFF17224B)

val SirohaSecondary = Color(0xFF5B5D6E)
val SirohaOnSecondary = Color(0xFFFFFFFF)
val SirohaSecondaryContainer = Color(0xFFEBEBEF)
val SirohaOnSecondaryContainer = Color(0xFF3C3C43)

val SirohaBackground = Color(0xFFFFFFFF)
val SirohaOnBackground = Color(0xFF3C3C43)
val SirohaSurface = Color(0xFFFFFFFF)
val SirohaOnSurface = Color(0xFF3C3C43)
val SirohaSurfaceVariant = Color(0xFFE5E7EB)
val SirohaOnSurfaceVariant = Color(0xFF6B6B73)
val SirohaOutline = Color(0xFFE5E7EB)

val SirohaError = Color(0xFFD92D20)
val SirohaOnError = Color(0xFFFFFFFF)

// Warna status (light) — dipakai lewat StatusColors, bukan langsung, supaya otomatis
// ganti ke varian dark saat dark theme aktif (lihat StatusColors.kt).
val StatusOkTextLight = Color(0xFF1A7F4E)
val StatusOkBgLight = Color(0xFFE6F4EC)
val StatusWarnTextLight = Color(0xFFB7791F)
val StatusWarnBgLight = Color(0xFFFDF3E1)
val EditedBgLight = Color(0xFFEAEEFB)

// Warna status (dark)
val StatusOkTextDark = Color(0xFF8FDDB0)
val StatusOkBgDark = Color(0xFF163B2C)
val StatusWarnTextDark = Color(0xFFF3C368)
val StatusWarnBgDark = Color(0xFF3D3116)
val EditedBgDark = Color(0xFF2E3A5C)

// Palet gelap
val SirohaPrimaryDark = Color(0xFFAEC0F5)
val SirohaOnPrimaryDark = Color(0xFF1B2A5E)
val SirohaPrimaryContainerDark = Color(0xFF34437A)
val SirohaOnPrimaryContainerDark = Color(0xFFDCE2F7)

val SirohaSecondaryDark = Color(0xFFC5C6D6)
val SirohaOnSecondaryDark = Color(0xFF2C2E3B)
val SirohaSecondaryContainerDark = Color(0xFF33343D)
val SirohaOnSecondaryContainerDark = Color(0xFFE6E6EA)

val SirohaBackgroundDark = Color(0xFF1C1B1F)
val SirohaOnBackgroundDark = Color(0xFFE6E6EA)
val SirohaSurfaceDark = Color(0xFF1C1B1F)
val SirohaOnSurfaceDark = Color(0xFFE6E6EA)
val SirohaSurfaceVariantDark = Color(0xFF3C3C43)
val SirohaOnSurfaceVariantDark = Color(0xFFB0B0B8)
val SirohaOutlineDark = Color(0xFF444450)

val SirohaErrorDark = Color(0xFFFFB4A9)
val SirohaOnErrorDark = Color(0xFF680003)

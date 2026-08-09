package com.siroha.ext4calc.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

/**
 * Kumpulan warna status (OK/Warn/Edited) yang otomatis mengikuti dark/light theme.
 * Dipakai supaya kartu partisi yang "diedit" atau chip status tidak selalu pakai
 * warna terang meskipun aplikasinya lagi dalam mode gelap.
 */
data class StatusColors(
    val okText: Color,
    val okBg: Color,
    val warnText: Color,
    val warnBg: Color,
    val editedBg: Color,
)

@Composable
@ReadOnlyComposable
fun statusColors(): StatusColors {
    return if (isSystemInDarkTheme()) {
        StatusColors(
            okText = StatusOkTextDark,
            okBg = StatusOkBgDark,
            warnText = StatusWarnTextDark,
            warnBg = StatusWarnBgDark,
            editedBg = EditedBgDark,
        )
    } else {
        StatusColors(
            okText = StatusOkTextLight,
            okBg = StatusOkBgLight,
            warnText = StatusWarnTextLight,
            warnBg = StatusWarnBgLight,
            editedBg = EditedBgLight,
        )
    }
}

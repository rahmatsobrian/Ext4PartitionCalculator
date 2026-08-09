package com.siroha.ext4calc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siroha.ext4calc.ui.theme.statusColors

enum class ChipTone { NEUTRAL, OK, WARN, EDITED }

/**
 * Chip status kecil dengan warna + ikon otomatis sesuai [tone].
 * Warna selalu diambil lewat [statusColors] supaya kebaca jelas di dark maupun light theme.
 */
@Composable
fun StatusChip(
    text: String,
    tone: ChipTone = ChipTone.NEUTRAL,
    modifier: Modifier = Modifier,
) {
    val status = statusColors()
    val (bg, fg) = when (tone) {
        ChipTone.NEUTRAL -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        ChipTone.OK -> status.okBg to status.okText
        ChipTone.WARN -> status.warnBg to status.warnText
        ChipTone.EDITED -> status.editedBg to MaterialTheme.colorScheme.primary
    }
    val icon: ImageVector? = when (tone) {
        ChipTone.OK -> Icons.Default.CheckCircle
        ChipTone.WARN -> Icons.Default.Warning
        ChipTone.EDITED -> Icons.Default.Edit
        ChipTone.NEUTRAL -> null
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = bg, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.size(4.dp))
        }
        Text(
            text = text,
            color = fg,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

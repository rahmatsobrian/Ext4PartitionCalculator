package com.siroha.ext4calc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siroha.ext4calc.ui.theme.statusColors

enum class ChipTone { NEUTRAL, OK, WARN, EDITED }

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
    Text(
        text = text,
        color = fg,
        fontSize = 12.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
        modifier = modifier
            .background(color = bg, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 5.dp),
    )
}

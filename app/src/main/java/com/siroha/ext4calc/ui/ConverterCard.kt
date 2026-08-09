package com.siroha.ext4calc.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.siroha.ext4calc.ui.components.SectionCard
import com.siroha.ext4calc.ui.theme.MonoFont
import com.siroha.ext4calc.util.ByteUnit
import com.siroha.ext4calc.util.ByteUtils

@Composable
fun ConverterCard(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bytes = ByteUtils.toBytes(value)
    val aligned = ByteUtils.alignUp4096(bytes)
    val diff = aligned - bytes
    val blocks = ByteUtils.blockCount(bytes)

    SectionCard(
        title = "Konversi Byte",
        subtitle = "Blok ext4 = 4096 byte",
        icon = Icons.Default.SwapHoriz,
        modifier = modifier,
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Nilai byte (paste langsung, boleh ada titik/koma/spasi)",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFont),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Contoh paste: 562.659.328 atau 562,659,328 — otomatis jadi 562659328 byte.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(14.dp))
        // Grid manual 2 kolom (bukan LazyVerticalGrid) supaya tinggi menyesuaikan
        // konten secara alami, tanpa risiko konten terpotong akibat tinggi tetap.
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ByteUtils.units.chunked(2).forEach { rowUnits ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    rowUnits.forEach { unit ->
                        ConversionResultItem(
                            unit = unit,
                            value = bytes / unit.factor,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (rowUnits.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Text(
            text = "Dibulatkan ke kelipatan 4096 byte: ${ByteUtils.formatThousands(aligned)} byte " +
                "(+${ByteUtils.formatThousands(diff)} byte padding, ${ByteUtils.formatThousands(blocks)} blok)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ConversionResultItem(
    unit: ByteUnit,
    value: Double,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = MaterialTheme.shapes.small,
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = unit.label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Text(
                text = ByteUtils.formatDecimal(value, unit.decimals),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = MonoFont,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

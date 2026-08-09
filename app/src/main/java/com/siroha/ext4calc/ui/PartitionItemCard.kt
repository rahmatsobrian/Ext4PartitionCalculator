package com.siroha.ext4calc.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.siroha.ext4calc.model.Partition
import com.siroha.ext4calc.ui.components.ChipTone
import com.siroha.ext4calc.ui.components.StatusChip
import com.siroha.ext4calc.ui.theme.MonoFont
import com.siroha.ext4calc.ui.theme.statusColors
import com.siroha.ext4calc.util.ByteUtils

@Composable
fun PartitionItemCard(
    partition: Partition,
    sizeBytes: Long,
    onNameChange: (String) -> Unit,
    onBytesChange: (String) -> Unit,
    onAddMb: (Long) -> Unit,
    onSubtractMb: (Long) -> Unit,
    onIncludeInSplitChange: (Boolean) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var addMbInput by remember { mutableStateOf("") }
    val status = statusColors()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (partition.edited) status.editedBg else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (partition.edited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: nama + status + hapus
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = partition.name,
                    onValueChange = onNameChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = MonoFont,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                if (partition.edited) {
                    Spacer(Modifier.width(8.dp))
                    StatusChip(text = "diedit", tone = ChipTone.EDITED)
                }
                Spacer(Modifier.width(4.dp))
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Hapus partisi",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Ukuran byte + readout
            Text(
                text = "Ukuran (byte)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = partition.bytes.toString(),
                onValueChange = onBytesChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFont),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(6.dp))
            val mb = sizeBytes / 1_000_000.0
            val gib = sizeBytes / 1_073_741_824.0
            Text(
                text = "${ByteUtils.formatThousands(sizeBytes)} B\n" +
                    "${ByteUtils.formatDecimal(mb, 2)} MB · ${ByteUtils.formatDecimal(gib, 3)} GiB",
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFont),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = partition.includeInSplit, onCheckedChange = onIncludeInSplitChange)
                Text(
                    text = "Sertakan di alokasi",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(10.dp))
            // Tambah / kurangi ukuran (MB)
            Text(
                text = "Tambah / kurangi ukuran (MB)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = addMbInput,
                onValueChange = { addMbInput = ByteUtils.cleanDigits(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("cth: 50") },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFont),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = {
                        val mbVal = addMbInput.toLongOrNull() ?: 0L
                        if (mbVal > 0) onAddMb(mbVal)
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("+ Tambah", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    onClick = {
                        val mbVal = addMbInput.toLongOrNull() ?: 0L
                        if (mbVal > 0) onSubtractMb(mbVal)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Text("− Kurangi", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

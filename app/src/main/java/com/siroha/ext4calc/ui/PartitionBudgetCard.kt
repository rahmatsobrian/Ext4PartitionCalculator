@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.siroha.ext4calc.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.siroha.ext4calc.model.ALLOCATE_SPLIT
import com.siroha.ext4calc.model.CalculatorState
import com.siroha.ext4calc.model.Presets
import com.siroha.ext4calc.ui.components.ChipTone
import com.siroha.ext4calc.ui.components.SectionCard
import com.siroha.ext4calc.ui.components.StatusChip
import com.siroha.ext4calc.ui.theme.MonoFont
import com.siroha.ext4calc.util.ByteUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartitionBudgetCard(
    state: CalculatorState,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Budget Dynamic Partition Group",
        subtitle = "Boleh melebihi maksimal, tetap dihitung",
        modifier = modifier,
    ) {
        Spacer(Modifier.height(14.dp))

        // ---- Preset ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Presets.all.forEach { preset ->
                FilterChip(
                    selected = state.activePresetId == preset.id,
                    onClick = { state.applyPreset(preset) },
                    label = { Text(preset.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---- Nama group & maksimal ukuran ----
        OutlinedTextField(
            value = state.groupName,
            onValueChange = state::onGroupNameChange,
            label = { Text("Nama Group") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFont),
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = state.groupMaxInput,
            onValueChange = state::onGroupMaxChange,
            label = { Text("Ukuran Fisik Maksimal Group (byte)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFont),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { state.onAlignAllChange(!state.alignAll) },
        ) {
            Checkbox(checked = state.alignAll, onCheckedChange = state::onAlignAllChange)
            Text(
                text = "Align semua ke 4096",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(Modifier.height(14.dp))

        // ---- Status edit ----
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Status edit:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(6.dp))
        val editedNames = state.partitions.filter { it.edited }.map { it.name }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (editedNames.isEmpty()) {
                StatusChip(text = "belum ada yang diedit", tone = ChipTone.NEUTRAL)
            } else {
                editedNames.forEach { name ->
                    StatusChip(text = name, tone = ChipTone.EDITED)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---- Daftar partisi ----
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            state.partitions.forEachIndexed { index, partition ->
                PartitionItemCard(
                    partition = partition,
                    sizeBytes = state.sizeOf(partition),
                    onNameChange = { state.updatePartitionName(index, it) },
                    onBytesChange = { state.updatePartitionBytes(index, it) },
                    onAddMb = { state.addMbToPartition(index, it) },
                    onSubtractMb = { state.subtractMbFromPartition(index, it) },
                    onIncludeInSplitChange = { state.setIncludeInSplit(index, it) },
                    onRemove = { state.removePartition(index) },
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = state::addPartition) {
                Text("+ Tambah partisi")
            }
            OutlinedButton(onClick = state::resetEditedFlags) {
                Text("Reset tanda \"diedit\"")
            }
        }

        Spacer(Modifier.height(18.dp))
        AllocatePanel(state = state)

        Spacer(Modifier.height(18.dp))
        SummarySection(state = state)
    }
}

@Composable
private fun AllocatePanel(state: CalculatorState) {
    val options = remember(state.partitions) {
        buildList {
            add(ALLOCATE_SPLIT to "Bagi rata ke partisi yang dicentang \"sertakan di alokasi\"")
            state.partitions.forEachIndexed { index, p ->
                add(index.toString() to "Semua ke: ${p.name}")
            }
        }
    }
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.first == state.allocateTarget }?.second
        ?: options.first().second

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
            .padding(16.dp),
    ) {
        Column {
            Text(
                text = "Alokasikan sisa ruang (dari maksimal group)",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                OutlinedTextField(
                    value = selectedLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kemana sisa ruang dialokasikan?") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                state.onAllocateTargetChange(value)
                                expanded = false
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = state::allocateRemaining,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Alokasikan sisa")
            }

            if (state.allocateHint.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.allocateHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SummarySection(state: CalculatorState) {
    val total = state.totalUsedBytes
    val remaining = state.remainingBytes
    val over = state.isOverBudget

    Column {
        SummaryRow(
            label = "Total terpakai",
            value = "${ByteUtils.formatThousands(total)} B (${
                ByteUtils.formatDecimal(total / 1_000_000.0, 2)
            } MB)",
        )
        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { state.usagePercent },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50)),
            color = if (over) com.siroha.ext4calc.ui.theme.StatusWarnText else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.secondaryContainer,
        )
        Spacer(Modifier.height(10.dp))
        SummaryRow(
            label = "Sisa / kelebihan ruang",
            value = "${ByteUtils.formatThousands(remaining)} B (${
                ByteUtils.formatDecimal(remaining / 1_000_000.0, 2)
            } MB)",
            valueColor = if (over) com.siroha.ext4calc.ui.theme.StatusWarnText else MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Status",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatusChip(
                text = if (over) "MELEBIHI BATAS" else "OK",
                tone = if (over) ChipTone.WARN else ChipTone.OK,
            )
        }

        if (over) {
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(com.siroha.ext4calc.ui.theme.StatusWarnBg)
                    .padding(14.dp),
            ) {
                Text(
                    text = "⚠ Melebihi batas fisik group. Tetap dihitung dan tetap bisa di-export, " +
                        "tapi kemungkinan besar super partition tidak akan cukup — pertimbangkan " +
                        "repartisi atau kecilkan salah satu partisi.",
                    style = MaterialTheme.typography.bodySmall,
                    color = com.siroha.ext4calc.ui.theme.StatusWarnText,
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFont, fontWeight = FontWeight.SemiBold),
            color = valueColor,
        )
    }
}

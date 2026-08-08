package com.siroha.ext4calc.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.siroha.ext4calc.util.ByteUtils

/** Target alokasi sisa ruang: "__split__" (bagi rata) atau index partisi sebagai String. */
const val ALLOCATE_SPLIT = "__split__"

/**
 * Menyimpan seluruh state kalkulator sekaligus logikanya, terpisah dari Composable
 * supaya mudah dibaca & di-test. Instance-nya dibuat sekali lewat `remember { CalculatorState() }`.
 */
class CalculatorState {

    // ---------- Konversi byte ----------
    var converterInput by mutableStateOf("562659328")
        private set

    fun onConverterInputChange(raw: String) {
        converterInput = ByteUtils.cleanDigits(raw)
    }

    // ---------- Budget dynamic partition group ----------
    var activePresetId by mutableStateOf(Presets.mi8937.id)
        private set

    var groupName by mutableStateOf(Presets.mi8937.groupName)
        private set

    var groupMaxInput by mutableStateOf(Presets.mi8937.groupMaxBytes.toString())
        private set

    var alignAll by mutableStateOf(true)
        private set

    var partitions by mutableStateOf(Presets.mi8937.partitions)
        private set

    var allocateTarget by mutableStateOf(ALLOCATE_SPLIT)
        private set

    var allocateHint by mutableStateOf("")
        private set

    val groupMaxBytes: Long
        get() = ByteUtils.toBytes(groupMaxInput)

    val totalUsedBytes: Long
        get() = partitions.sumOf { sizeOf(it) }

    val remainingBytes: Long
        get() = groupMaxBytes - totalUsedBytes

    val isOverBudget: Boolean
        get() = totalUsedBytes > groupMaxBytes

    val usagePercent: Float
        get() {
            if (groupMaxBytes <= 0L) return 0f
            return (totalUsedBytes.toDouble() / groupMaxBytes.toDouble())
                .toFloat()
                .coerceIn(0f, 1f)
        }

    fun sizeOf(p: Partition): Long = if (alignAll) ByteUtils.alignUp4096(p.bytes) else p.bytes

    fun applyPreset(preset: PartitionPreset) {
        activePresetId = preset.id
        groupName = preset.groupName
        groupMaxInput = preset.groupMaxBytes.toString()
        partitions = preset.partitions
        allocateTarget = ALLOCATE_SPLIT
        allocateHint = ""
    }

    fun onGroupNameChange(value: String) {
        groupName = value
    }

    fun onGroupMaxChange(raw: String) {
        groupMaxInput = ByteUtils.cleanDigits(raw)
    }

    fun onAlignAllChange(value: Boolean) {
        alignAll = value
    }

    fun onAllocateTargetChange(value: String) {
        allocateTarget = value
    }

    fun addPartition() {
        partitions = partitions + Partition(name = "new_partition", bytes = 0L)
    }

    fun removePartition(index: Int) {
        if (index !in partitions.indices) return
        partitions = partitions.toMutableList().also { it.removeAt(index) }
        // reset target alokasi kalau sempat menunjuk index yang barusan dihapus / bergeser
        if (allocateTarget != ALLOCATE_SPLIT) {
            val idx = allocateTarget.toIntOrNull()
            if (idx == null || idx !in partitions.indices) {
                allocateTarget = ALLOCATE_SPLIT
            }
        }
    }

    fun resetEditedFlags() {
        partitions = partitions.map { it.copy(edited = false) }
    }

    fun updatePartitionName(index: Int, name: String) {
        if (index !in partitions.indices) return
        partitions = partitions.toMutableList().also {
            it[index] = it[index].copy(name = name)
        }
    }

    fun updatePartitionBytes(index: Int, rawInput: String) {
        if (index !in partitions.indices) return
        val newBytes = ByteUtils.toBytes(rawInput)
        partitions = partitions.toMutableList().also {
            val current = it[index]
            val edited = current.edited || newBytes != current.bytes
            it[index] = current.copy(bytes = newBytes, edited = edited)
        }
    }

    fun addMbToPartition(index: Int, mb: Long) {
        if (index !in partitions.indices || mb <= 0) return
        partitions = partitions.toMutableList().also {
            val current = it[index]
            it[index] = current.copy(
                bytes = current.bytes + ByteUtils.mbToBytes(mb),
                edited = true
            )
        }
    }

    fun subtractMbFromPartition(index: Int, mb: Long) {
        if (index !in partitions.indices || mb <= 0) return
        partitions = partitions.toMutableList().also {
            val current = it[index]
            it[index] = current.copy(
                bytes = (current.bytes - ByteUtils.mbToBytes(mb)).coerceAtLeast(0L),
                edited = true
            )
        }
    }

    fun setIncludeInSplit(index: Int, include: Boolean) {
        if (index !in partitions.indices) return
        partitions = partitions.toMutableList().also {
            it[index] = it[index].copy(includeInSplit = include)
        }
    }

    /** Alokasikan sisa ruang (groupMax - total terpakai) ke target yang dipilih. */
    fun allocateRemaining() {
        val remaining = remainingBytes
        if (remaining <= 0L) {
            allocateHint = if (remaining == 0L) {
                "Tidak ada sisa ruang — total sudah pas dengan maksimal."
            } else {
                "Tidak ada sisa ruang untuk dialokasikan — total sudah melebihi maksimal sebesar ${
                    ByteUtils.formatThousands(-remaining)
                } byte."
            }
            return
        }

        if (allocateTarget == ALLOCATE_SPLIT) {
            val targets = partitions.withIndex().filter { it.value.includeInSplit }
            if (targets.isEmpty()) {
                allocateHint = "Centang dulu minimal satu partisi di \"sertakan di alokasi\" sebelum bagi rata."
                return
            }
            val share = remaining / targets.size
            val leftover = remaining - share * targets.size
            val mutable = partitions.toMutableList()
            targets.forEachIndexed { i, indexed ->
                val extra = share + if (i == 0) leftover else 0L
                mutable[indexed.index] = indexed.value.copy(
                    bytes = indexed.value.bytes + extra,
                    edited = true
                )
            }
            partitions = mutable
            allocateHint = "${ByteUtils.formatThousands(remaining)} byte dibagi rata ke ${targets.size} " +
                "partisi (${targets.joinToString(", ") { it.value.name }})."
        } else {
            val idx = allocateTarget.toIntOrNull()
            if (idx == null || idx !in partitions.indices) {
                allocateTarget = ALLOCATE_SPLIT
                return
            }
            val mutable = partitions.toMutableList()
            mutable[idx] = mutable[idx].copy(bytes = mutable[idx].bytes + remaining, edited = true)
            partitions = mutable
            allocateHint = "${ByteUtils.formatThousands(remaining)} byte dialokasikan semua ke partisi \"${
                partitions[idx].name
            }\"."
        }
    }

    /** Bangun teks dynamic_partitions_op_list siap tempel. */
    fun buildOpList(): String {
        val gName = groupName.ifBlank { "my_dynpart" }
        val lines = mutableListOf<String>()
        lines += "#Remove all existing dynamic partitions and groups before applying full OTA"
        lines += "remove_all_groups"
        lines += "#Add group $gName with maximum size ${groupMaxBytes}"
        lines += "add_group $gName ${groupMaxBytes}"
        partitions.forEach { p ->
            lines += "#Add partition ${p.name} to group $gName"
            lines += "add ${p.name} $gName"
        }
        partitions.forEach { p ->
            val size = sizeOf(p)
            lines += "#Grow partition ${p.name} from 0 to $size"
            lines += "resize ${p.name} $size"
        }
        return lines.joinToString("\n")
    }
}

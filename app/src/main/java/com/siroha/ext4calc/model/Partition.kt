package com.siroha.ext4calc.model

/**
 * Merepresentasikan satu partisi dynamic (system, vendor, product, dst).
 *
 * @param name nama partisi, misalnya "system" atau "vendor".
 * @param bytes ukuran mentah dalam byte (belum di-align).
 * @param edited true kalau ukurannya sudah pernah diubah manual oleh user.
 * @param includeInSplit true kalau partisi ini ikut dihitung saat "bagi rata sisa ruang".
 */
data class Partition(
    val name: String,
    val bytes: Long,
    val edited: Boolean = false,
    val includeInSplit: Boolean = false,
)

/** Preset kumpulan partisi + konfigurasi group siap pakai. */
data class PartitionPreset(
    val id: String,
    val label: String,
    val groupName: String,
    val groupMaxBytes: Long,
    val partitions: List<Partition>,
)

object Presets {

    val mi8937 = PartitionPreset(
        id = "mi8937",
        label = "Preset mi8937 (default)",
        groupName = "mi8937_dynpart",
        groupMaxBytes = 3_753_902_080L,
        partitions = listOf(
            Partition(name = "system", bytes = 1_277_747_200L),
            Partition(name = "vendor", bytes = 217_710_592L),
            Partition(name = "product", bytes = 1_164_374_016L),
            Partition(name = "odm", bytes = 72_712_192L),
            Partition(name = "system_ext", bytes = 950_329_344L),
        )
    )

    val kosong = PartitionPreset(
        id = "kosong",
        label = "Kosongkan",
        groupName = "my_dynpart",
        groupMaxBytes = 3_753_902_080L,
        partitions = listOf(
            Partition(name = "system", bytes = 0L),
            Partition(name = "vendor", bytes = 0L),
        )
    )

    val all = listOf(mi8937, kosong)
}

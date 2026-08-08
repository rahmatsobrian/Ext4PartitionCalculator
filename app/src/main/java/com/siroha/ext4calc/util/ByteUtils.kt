package com.siroha.ext4calc.util

import java.util.Locale
import kotlin.math.ceil

/** Definisi satuan konversi byte untuk kartu "Konversi Byte". */
data class ByteUnit(val label: String, val factor: Double, val decimals: Int)

object ByteUtils {

    const val BLOCK_SIZE = 4096L

    val units = listOf(
        ByteUnit("Byte", 1.0, 0),
        ByteUnit("Bit", 0.125, 0),
        ByteUnit("KB", 1_000.0, 3),
        ByteUnit("KiB", 1_024.0, 3),
        ByteUnit("MB", 1_000_000.0, 3),
        ByteUnit("MiB", 1_048_576.0, 3),
        ByteUnit("GB", 1_000_000_000.0, 4),
        ByteUnit("GiB", 1_073_741_824.0, 4),
    )

    /** Buang semua karakter selain digit, supaya paste "562.659.328" jadi "562659328". */
    fun cleanDigits(input: String): String = input.filter { it.isDigit() }

    /** Ubah string mentah (boleh ada titik/koma/spasi) jadi Long byte. Aman dari overflow. */
    fun toBytes(input: String): Long {
        val cleaned = cleanDigits(input)
        if (cleaned.isEmpty()) return 0L
        // Batasi panjang digit supaya tidak overflow Long (maks ~19 digit).
        val trimmed = if (cleaned.length > 18) cleaned.substring(0, 18) else cleaned
        return trimmed.toLongOrNull() ?: Long.MAX_VALUE
    }

    /** Bulatkan ke atas ke kelipatan 4096 byte (ukuran blok ext4). */
    fun alignUp4096(bytes: Long): Long {
        if (bytes <= 0L) return 0L
        return ceil(bytes / BLOCK_SIZE.toDouble()).toLong() * BLOCK_SIZE
    }

    fun blockCount(bytes: Long): Long {
        if (bytes <= 0L) return 0L
        return ceil(bytes / BLOCK_SIZE.toDouble()).toLong()
    }

    fun mbToBytes(mb: Long): Long = mb * 1_000_000L

    /** Format angka Long dengan pemisah ribuan titik, gaya Indonesia. Contoh: 1.234.567 */
    fun formatThousands(n: Long): String {
        val isNegative = n < 0
        val s = kotlin.math.abs(n).toString()
        val sb = StringBuilder()
        for ((index, c) in s.reversed().withIndex()) {
            if (index != 0 && index % 3 == 0) sb.append('.')
            sb.append(c)
        }
        return (if (isNegative) "-" else "") + sb.reverse().toString()
    }

    /** Format angka Double dengan sejumlah desimal tetap, locale-independent (pakai titik). */
    fun formatDecimal(n: Double, decimals: Int): String {
        if (n.isNaN() || n.isInfinite()) return "0"
        return if (decimals <= 0) {
            Math.round(n).toString()
        } else {
            String.format(Locale.US, "%,.${decimals}f", n)
        }
    }
}

package com.siroha.ext4calc.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.siroha.ext4calc.ui.components.SectionCard

/** Versi aplikasi, sengaja hardcode di sini (samakan manual dengan versionName di build.gradle.kts). */
private const val APP_VERSION = "1.0.0"

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { AboutHeader() }
        item { AboutDescriptionCard() }
        item { AboutFeatureCard() }
        item { AboutCreditCard() }
    }
}

@Composable
private fun AboutHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp),
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = "Kalkulator Partisi ext4",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Versi $APP_VERSION",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AboutDescriptionCard() {
    SectionCard(title = "Tentang aplikasi") {
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Aplikasi bantu buat ngitung ukuran partisi ext4 & dynamic partition group " +
                "sebelum build ROM/kernel — konversi satuan byte, susun budget tiap partisi " +
                "(system, vendor, product, dst), dan langsung export ke format " +
                "dynamic_partitions_op_list yang siap dipakai.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Port native Android (Kotlin + Jetpack Compose) dari versi web sebelumnya, " +
                "dibuat supaya bisa dipakai langsung dari HP tanpa buka browser.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AboutFeatureCard() {
    SectionCard(title = "Teknologi yang dipakai") {
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            FeatureRow(
                icon = Icons.Default.Palette,
                title = "Material 3 · Material You",
                description = "Warna otomatis ikut wallpaper di Android 12+, tetap enak dilihat di Android 10-11.",
            )
            FeatureRow(
                icon = Icons.Default.Code,
                title = "Jetpack Compose · Kotlin DSL",
                description = "UI dibangun deklaratif, seluruh konfigurasi Gradle pakai Kotlin (bukan Groovy).",
            )
            FeatureRow(
                icon = Icons.Default.Bolt,
                title = "Ringan, tanpa dependency berat",
                description = "Nggak ada library native/NDK atau font kustom, build tetap cepat & minim celah gagal.",
            )
            FeatureRow(
                icon = Icons.Default.Security,
                title = "Semua hitungan lokal di HP",
                description = "Nggak ada data yang dikirim ke server mana pun — semua proses di perangkat kamu.",
            )
        }
    }
}

@Composable
private fun FeatureRow(icon: ImageVector, title: String, description: String) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AboutCreditCard() {
    SectionCard(title = "Dibuat oleh") {
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Siroha",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Android modding · custom ROM & kernel development",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = "Hitung dulu sebelum flash — aplikasi ini cuma alat bantu hitung, " +
                "bukan pengganti backup partisi kamu. 🙂",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

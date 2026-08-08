package com.siroha.ext4calc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.siroha.ext4calc.model.CalculatorState
import com.siroha.ext4calc.ui.ConverterCard
import com.siroha.ext4calc.ui.ExportCard
import com.siroha.ext4calc.ui.PartitionBudgetCard
import com.siroha.ext4calc.ui.components.ChipTone
import com.siroha.ext4calc.ui.components.StatusChip
import com.siroha.ext4calc.ui.theme.Ext4CalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ext4CalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Ext4CalcApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ext4CalcApp() {
    val state = remember { CalculatorState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kalkulator Partisi ext4") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        // Semua section dirender sebagai item LazyColumn (bukan Column + verticalScroll)
        // supaya list partisi yang panjang tidak menimbulkan masalah performa/ukuran.
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 4.dp,
                bottom = innerPadding.calculateBottomPadding() + 32.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { HeaderSection() }
            item {
                ConverterCard(
                    value = state.converterInput,
                    onValueChange = state::onConverterInputChange,
                )
            }
            item { PartitionBudgetCard(state = state) }
            item { ExportCard(opListText = state.buildOpList()) }
            item { FooterSection() }
        }
    }
}

@Composable
private fun HeaderSection() {
    Column {
        StatusChip(text = "ext4 · dynamic partitions", tone = ChipTone.NEUTRAL)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Tempel angka byte langsung dari Windows Properties (titik/koma/spasi otomatis " +
                "dibuang), tambah ukuran pakai MB per partisi, dan lihat partisi mana saja yang sudah diedit.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun FooterSection() {
    Text(
        text = "Dibuat untuk keperluan build ROM/kernel — hitung dulu sebelum flash.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center,
    )
}

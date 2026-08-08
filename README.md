# Kalkulator Partisi ext4 (Android)

Port native Kotlin + Jetpack Compose dari `EXT4_Partition_Calculator.html`.
Fungsinya identik dengan versi web:

1. **Konversi Byte** — paste angka byte (boleh ada titik/koma/spasi), langsung
   dikonversi ke Bit/KB/KiB/MB/MiB/GB/GiB + info alignment ke 4096 byte.
2. **Budget Dynamic Partition Group** — preset `mi8937` & kosong, tambah/hapus
   partisi, edit ukuran per byte atau nambah/kurangi per MB, tandai partisi
   yang sudah diedit, alokasikan sisa ruang (bagi rata atau ke satu partisi),
   progress bar + status OK/MELEBIHI BATAS.
3. **Export** `dynamic_partitions_op_list` — salin ke clipboard atau simpan
   sebagai `.txt` lewat Storage Access Framework (tidak perlu izin storage).

## Stack teknis

- Kotlin DSL (`build.gradle.kts`) sepenuhnya, tanpa Groovy.
- Jetpack Compose + **Material 3 / Material You** (dynamic color otomatis di
  Android 12+, fallback ke palet brand "KernelSU Clean Light" `#5672CD` di
  Android 10–11).
- `minSdk 29` (Android 10) — `targetSdk`/`compileSdk 36` (Android 16).
- AGP `8.10.0`, Kotlin `2.0.21`, Compose BOM `2024.12.01`.
- Tidak ada dependency native/NDK, tidak ada font kustom (pakai font sistem)
  supaya build tetap ringan dan minim titik kegagalan.

## Build lokal

Repo ini **sengaja tidak menyertakan `gradle-wrapper.jar`** (file biner) supaya
tidak ada blob biner yang ikut ter-commit. Cara build lokal:

```bash
# sekali saja, kalau belum punya Gradle terpasang di PATH:
gradle wrapper --gradle-version 8.10.2 --distribution-type bin

./gradlew assembleDebug
```

Atau langsung buka folder ini di Android Studio — Android Studio otomatis
menawarkan untuk membuatkan wrapper-nya.

## Build otomatis (CI)

Lihat `.github/workflows/android-build.yml`. Workflow ini:

- **Tidak butuh repository secret apa pun** — APK debug & release
  ditandatangani otomatis pakai debug keystore bawaan AGP.
- **Generate `gradle-wrapper.jar` sendiri** di runner lewat `gradle wrapper`
  (bukan dari file yang di-commit), lalu build selanjutnya pakai `./gradlew`.
- Logging lengkap: `--stacktrace --info --warning-mode all`, hasilnya
  di-`tee` ke file log dan diupload sebagai artifact (`build-logs`), plus
  laporan lint/Gradle di artifact `gradle-reports` — mempermudah debug kalau
  build gagal.
- Upload APK debug & release sebagai artifact terpisah.

## Struktur

```
app/src/main/java/com/siroha/ext4calc/
├─ MainActivity.kt              # entry point + layout utama (LazyColumn)
├─ model/
│  ├─ Partition.kt              # data class Partition + Presets
│  └─ CalculatorState.kt        # semua state & logic kalkulator
├─ ui/
│  ├─ ConverterCard.kt
│  ├─ PartitionBudgetCard.kt
│  ├─ PartitionItemCard.kt
│  ├─ ExportCard.kt
│  ├─ components/               # SectionCard, StatusChip (reusable)
│  └─ theme/                    # Color, Theme (dynamic color), Type
└─ util/ByteUtils.kt            # semua logika konversi byte
```

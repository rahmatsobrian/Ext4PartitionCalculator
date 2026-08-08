# Add project specific ProGuard rules here.
# Jetpack Compose sudah menyediakan aturan consumer-rules sendiri,
# jadi file ini sengaja dibiarkan minimal.

-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# ── Modelos do Room (campos usados por reflexão) ──────────
-keep class br.com.projeto.elo.dominio.modelo.** { *; }
-keepclassmembers class br.com.projeto.elo.dominio.modelo.** { *; }

# ── Gson (serialização do retorno do Gemini) ──────────────
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-dontwarn com.google.gson.**

# ── Firebase ──────────────────────────────────────────────
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**

# ── Retrofit + OkHttp ─────────────────────────────────────
-keepattributes RuntimeVisibleAnnotations
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# ── Hilt ──────────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.hilt.**

# ── Coil (carregamento de imagens) ────────────────────────
-dontwarn coil.**

# ── Jetpack Compose ───────────────────────────────────────
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ── Proteção: manter nomes de Enum para o Room ────────────
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ── Nota de segurança: SQL Injection ──────────────────────
# Todas as queries Room usam parâmetros bindados (:uid, :busca)
# via @Query com placeholders — nunca concatenação de strings.
# Isso garante proteção nativa contra SQL Injection por design.

# Consumer ProGuard rules for Template Feature Library
# These rules are applied when apps consume this library

# Template Feature Module API Visibility Rules
# ============================================

# Keep only the public API package classes visible to consumers
-keep public class com.example.modules.template_feature.api.** { *; }

# Hide all internal implementation classes from external access
# But keep them for internal functionality
-keep,allowshrinking,allowoptimization class com.example.modules.template_feature.data.** { *; }
-keep,allowshrinking,allowoptimization class com.example.modules.template_feature.domain.** { *; }
-keep,allowshrinking,allowoptimization class com.example.modules.template_feature.presentation.** { *; }
-keep,allowshrinking,allowoptimization class com.example.modules.template_feature.di.** { *; }

# Keep Hilt generated classes for dependency injection
-keep class * extends dagger.hilt.** { *; }
-keep class dagger.hilt.** { *; }
-keep class **_HiltComponents { *; }
-keep class **_HiltModules { *; }
-keep class **_Provide { *; }
-keep class **_Factory { *; }

# Keep Room generated classes
-keep class **Dao { *; }
-keep class **Database { *; }
-keep class **Entity { *; }

# Keep Retrofit service interfaces and data models
-keep interface com.example.modules.template_feature.data.remotedatasource.api.** { *; }
-keep class com.example.modules.template_feature.data.dto.** { *; }

# Preserve line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Keep Kotlin metadata for proper functioning
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
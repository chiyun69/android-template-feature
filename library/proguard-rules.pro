# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Template Feature Implementation Library Rules
# =============================================
# Note: These rules apply only to the implementation library module
# Consumer rules are defined in the API module's consumer-rules.pro

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
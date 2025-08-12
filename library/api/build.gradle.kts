plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    `maven-publish`
}

android {
    namespace = "com.example.modules.template_feature.api"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            consumerProguardFiles("consumer-rules.pro")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
    
    buildFeatures {
        compose = true
    }
    
    sourceSets {
        named("main") {
            java.srcDir("../src/main/java")
            res.srcDir("../src/main/res")
        }
    }
}

dependencies {
    // All necessary dependencies that the API consumers will need
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.compose)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.navigation)
    implementation(libs.hilt.navigation.compose)
    
    // Hilt for API implementation
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    
    // Networking (from library)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    
    // Room (from library)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    
    // Kotlin Serialization (from library)
    implementation(libs.kotlinx.serialization.json)
}

// Publishing configuration for API
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.example.modules"
            artifactId = "template-feature-api"
            version = "1.0.15"
            
            afterEvaluate {
                from(components["release"])
            }
            
            pom {
                name.set("Template Feature API")
                description.set("Public API for Template Feature Module")
            }
        }
    }
}
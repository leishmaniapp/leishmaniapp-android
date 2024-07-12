import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.squareup.wire)
    alias(libs.plugins.androidx.room)
}

/**
 * Load the application.properties file containing the remote connection secrets
 */
val applicationProperties = Properties().apply {
    load(FileInputStream(rootProject.file("application.properties")))
}

android {
    namespace = "com.leishmaniapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.leishmaniapp"
        minSdk = 27
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Apply field from application.properties into buildConfig
        applicationProperties.forEach { key, value ->
            buildConfigField(
                "String",
                key.toString().uppercase(Locale.US),
                "\"$value\""
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" //for compose
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

wire {
    sourcePath {
        srcDirs(
            // protobuf_schema from github submodules
            "src/main/proto",
            // grpc-proto files from https://github.com/grpc/grpc-proto
            "src/main/grpc"
        )
    }
    kotlin {
        android = true
        rpcRole = "client"
        rpcCallStyle = "suspending"
        singleMethodServices = false
    }
}

dependencies {

    /* Kotlin Core */
    implementation(platform(libs.kotlin.bom))
    implementation(libs.bundles.kotlin)

    /* Kotlinx */
    implementation(libs.bundles.kotlinx)

    /* Testing Libraries */
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)

    /* OkHttp (HTTP2 client for gRPC) */
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)

    /* Wire Protobuf & gRPC */
    implementation(libs.bundles.wire.grpc)

    /* Androidx Core */
    implementation(libs.bundles.androidx.core)

    /* Androidx Hilt */
    implementation(libs.bundles.androidx.hilt)

    /* Androidx Lifecycle */
    implementation(libs.bundles.androidx.lifecycle)

    /* Androidx Compose */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.bundles.androidx.compose.debug)

    /* Androidx Navigation */
    implementation(libs.bundles.androidx.navigation)

    /* Androidx DataStore */
    implementation(libs.bundles.androidx.datastore)

    /* Androidx Room */
    implementation(libs.bundles.androidx.room)

    /* Androidx AppStartup */
    implementation(libs.bundles.androidx.startup)

    /* Androidx CameraX */
    implementation(libs.bundles.androidx.camerax)

    /* Androidx WorkManager */
    implementation(libs.bundles.androidx.work)

    /* Other Libraries */
    implementation(libs.bundles.libs)

    /* Kotlin Symbol Processing */
    ksp(libs.bundles.ksp)
}
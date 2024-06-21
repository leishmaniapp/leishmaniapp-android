import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.squareup.wire)
    alias(libs.plugins.androidx.room)
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

    sourceSets.getByName("main") {
        kotlin.srcDir("build/generated/source/proto/main/kotlin")
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            )
        )
    }
}

wire {
    kotlin {
        android = true
        rpcRole = "client"
        rpcCallStyle = "suspending"
        singleMethodServices = true
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

    /* Protobuf & gRPC */
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
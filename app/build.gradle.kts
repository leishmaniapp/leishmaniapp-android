import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.squareup.wire)
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

    /* Protobuf & gRPC */
    implementation(libs.squareup.wire.runtime)
    implementation(libs.squareup.wire.grpc)

    /* Android core dependencies */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    /* Testing Libraries */
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit.ktx)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.espresso.core)

    /* Kotlinx extensions */
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.kotlinx)

    /* Jetpack Hilt */
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)

    /* Jetpack Compose */
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.compose.material)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.manifest)

    /* Jetpack Navigation */
    implementation(libs.bundles.androidx.navigation)

    /* Jetpack Startup */
    implementation(libs.androidx.startup)

    /* Jetpack ViewModel & Live Data */
    implementation(libs.bundles.androidx.lifecycle)

    /* Jetpack CameraX */
    implementation(libs.bundles.androidx.camera)

    /* Jetpack Room */
    implementation(libs.bundles.androidx.room)
    androidTestImplementation(libs.androidx.room.testing)
    ksp(libs.androidx.room.compiler)

    /* Jetpack WorkManager */
    implementation(libs.bundles.androidx.work)

    /* ArrowKt: Functional Programming */
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)

    /* Faker: Mock data generation */
    debugImplementation(libs.faker)

    /* Coil: Image manipulation and cropping */
    implementation(libs.coil)
    implementation(libs.coil.compose)

    /* iTextPDF: PDF Generation */
    implementation(libs.itextpdf.itext7)
}
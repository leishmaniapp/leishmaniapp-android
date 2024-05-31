import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")

    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

    id("kotlin-kapt")
    id("kotlin-parcelize")
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
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
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
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {

    /* Android core dependencies */
    implementation("androidx.core:core-ktx:1.13.1")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.24"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation("androidx.appcompat:appcompat:1.7.0")

    /* Testing Libraries */
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    /* Jetpack Compose */
    implementation("androidx.compose.ui:ui:1.6.7")
    implementation("androidx.compose.runtime:runtime:1.6.7")
    implementation("androidx.compose.foundation:foundation:1.6.7")
    implementation("androidx.compose.animation:animation:1.6.7")
    implementation("androidx.compose.material3:material3:1.2.1")

    // Google Icons for Jetpack Compose
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // Jetpack Compose Testing
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Jetpack Compose Previews
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /* Jetpack Glance */
    implementation("androidx.glance:glance-appwidget:1.0.0")

    /* Jetpack Startup */
    implementation("androidx.startup:startup-runtime:1.1.1")

    // Jetpack ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate")

    /* Jetpack Navigation */
    implementation("androidx.navigation:navigation-compose:2.7.7")

    /* Jetpack LiveData */
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    /* Jetpack CameraX */
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.camera:camera-view:1.3.3")

    /* Jetpack Room */
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Room testing
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    /* Jetpack WorkManager */
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    /* Jetpack Hilt */
    implementation("com.google.dagger:hilt-android:2.51.1")

    kapt("com.google.dagger:hilt-compiler:2.51.1")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.51.1")
    testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.51.1")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.51.1")

    implementation("androidx.hilt:hilt-work:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    /* Kotlinx extensions */
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Mock data generator
    debugImplementation("io.bloco:faker:2.0.2")

    /* Coil: Image manipulation and cropping */
    implementation("io.coil-kt:coil:2.4.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    /* AWS Amplify */
    implementation("com.amplifyframework:aws-storage-s3:1.31.3")
    implementation("com.amplifyframework:aws-auth-cognito:1.31.3")
    implementation("com.amplifyframework:aws-api:1.31.3")

    /* ArrowKt: Functional Programming */
    implementation("io.arrow-kt:arrow-core:1.2.4")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")

    /* iTextPDF: PDF Generation */
    implementation("com.itextpdf:itext7-core:8.0.1")
}

kapt {
    correctErrorTypes = true
}
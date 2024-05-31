plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.google.ksp) apply false

    alias(libs.plugins.google.dagger.hilt.android) apply false
    alias(libs.plugins.squareup.wire) apply false
}
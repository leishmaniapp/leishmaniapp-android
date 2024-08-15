package com.leishmaniapp.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Base color for the Leishmaniapp app theming
 */
private val LeishmaniappSeedColor = Color(0xFF7B2CBF)

private val primaryLight = Color(0xFF6403A9)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFF8B3FCF)
private val onPrimaryContainerLight = Color(0xFFFFFFFF)
private val secondaryLight = Color(0xFF71508E)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFE3C1FF)
private val onSecondaryContainerLight = Color(0xFF4C2D68)
private val tertiaryLight = Color(0xFF83005D)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFB42F85)
private val onTertiaryContainerLight = Color(0xFFFFFFFF)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFFFF7FE)
private val onBackgroundLight = Color(0xFF1F1A22)
private val surfaceLight = Color(0xFFFFF7FE)
private val onSurfaceLight = Color(0xFF1F1A22)
private val surfaceVariantLight = Color(0xFFECDEF1)
private val onSurfaceVariantLight = Color(0xFF4C4353)
private val outlineLight = Color(0xFF7E7384)
private val outlineVariantLight = Color(0xFFCFC2D5)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF342E38)
private val inverseOnSurfaceLight = Color(0xFFF8EDFA)
private val inversePrimaryLight = Color(0xFFDEB7FF)
private val surfaceDimLight = Color(0xFFE1D7E3)
private val surfaceBrightLight = Color(0xFFFFF7FE)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFFBF0FD)
private val surfaceContainerLight = Color(0xFFF5EAF7)
private val surfaceContainerHighLight = Color(0xFFEFE5F1)
private val surfaceContainerHighestLight = Color(0xFFEADFEC)

private val primaryDark = Color(0xFFDEB7FF)
private val onPrimaryDark = Color(0xFF4A007F)
private val primaryContainerDark = Color(0xFF721FB6)
private val onPrimaryContainerDark = Color(0xFFF9EAFF)
private val secondaryDark = Color(0xFFDEB8FE)
private val onSecondaryDark = Color(0xFF40215D)
private val secondaryContainerDark = Color(0xFF51326D)
private val onSecondaryContainerDark = Color(0xFFE8CAFF)
private val tertiaryDark = Color(0xFFFFAFD7)
private val onTertiaryDark = Color(0xFF610044)
private val tertiaryContainerDark = Color(0xFF96106C)
private val onTertiaryContainerDark = Color(0xFFFFEAF1)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF16121A)
private val onBackgroundDark = Color(0xFFEADFEC)
private val surfaceDark = Color(0xFF16121A)
private val onSurfaceDark = Color(0xFFEADFEC)
private val surfaceVariantDark = Color(0xFF4C4353)
private val onSurfaceVariantDark = Color(0xFFCFC2D5)
private val outlineDark = Color(0xFF988D9E)
private val outlineVariantDark = Color(0xFF4C4353)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFEADFEC)
private val inverseOnSurfaceDark = Color(0xFF342E38)
private val inversePrimaryDark = Color(0xFF8234C6)
private val surfaceDimDark = Color(0xFF16121A)
private val surfaceBrightDark = Color(0xFF3D3741)
private val surfaceContainerLowestDark = Color(0xFF110C15)
private val surfaceContainerLowDark = Color(0xFF1F1A22)
private val surfaceContainerDark = Color(0xFF231E26)
private val surfaceContainerHighDark = Color(0xFF2D2831)
private val surfaceContainerHighestDark = Color(0xFF38333C)

/**
 * Application light color scheme
 */
internal val LeishmaniappLightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

/**
 * Application dark color scheme
 */
internal val LeishmaniappDarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)
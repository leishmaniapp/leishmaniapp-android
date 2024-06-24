package com.leishmaniapp.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Base color for the Leishmaniapp app theming
 */
private val LeishmaniappSeedColor = Color(0xFF632269)

private val primaryLight = Color(0xFF4D0A55)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFF743279)
private val onPrimaryContainerLight = Color(0xFFFFE2FA)
private val secondaryLight = Color(0xFF735472)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFFFDCFB)
private val onSecondaryContainerLight = Color(0xFF5E405D)
private val tertiaryLight = Color(0xFF590823)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFF862D43)
private val onTertiaryContainerLight = Color(0xFFFFE4E7)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFFFF7FA)
private val onBackgroundLight = Color(0xFF201A1F)
private val surfaceLight = Color(0xFFFFF7FA)
private val onSurfaceLight = Color(0xFF201A1F)
private val surfaceVariantLight = Color(0xFFEFDEEA)
private val onSurfaceVariantLight = Color(0xFF4F434D)
private val outlineLight = Color(0xFF81737E)
private val outlineVariantLight = Color(0xFFD2C2CE)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF352E34)
private val inverseOnSurfaceLight = Color(0xFFFAEDF5)
private val inversePrimaryLight = Color(0xFFFBABFC)
private val surfaceDimLight = Color(0xFFE2D7DE)
private val surfaceBrightLight = Color(0xFFFFF7FA)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFFCF0F7)
private val surfaceContainerLight = Color(0xFFF7EBF2)
private val surfaceContainerHighLight = Color(0xFFF1E5EC)
private val surfaceContainerHighestLight = Color(0xFFEBDFE6)

private val primaryDark = Color(0xFFFBABFC)
private val onPrimaryDark = Color(0xFF53115A)
private val primaryContainerDark = Color(0xFF591860)
private val onPrimaryContainerDark = Color(0xFFFAAAFB)
private val secondaryDark = Color(0xFFE1BBDE)
private val onSecondaryDark = Color(0xFF422742)
private val secondaryContainerDark = Color(0xFF533653)
private val onSecondaryContainerDark = Color(0xFFF1C9ED)
private val tertiaryDark = Color(0xFFFFB2BE)
private val onTertiaryDark = Color(0xFF600F28)
private val tertiaryContainerDark = Color(0xFF67152D)
private val onTertiaryContainerDark = Color(0xFFFFB0BD)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF171216)
private val onBackgroundDark = Color(0xFFEBDFE6)
private val surfaceDark = Color(0xFF171216)
private val onSurfaceDark = Color(0xFFEBDFE6)
private val surfaceVariantDark = Color(0xFF4F434D)
private val onSurfaceVariantDark = Color(0xFFD2C2CE)
private val outlineDark = Color(0xFF9B8C98)
private val outlineVariantDark = Color(0xFF4F434D)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFEBDFE6)
private val inverseOnSurfaceDark = Color(0xFF352E34)
private val inversePrimaryDark = Color(0xFF88448C)
private val surfaceDimDark = Color(0xFF171216)
private val surfaceBrightDark = Color(0xFF3E373D)
private val surfaceContainerLowestDark = Color(0xFF120D11)
private val surfaceContainerLowDark = Color(0xFF201A1F)
private val surfaceContainerDark = Color(0xFF241E23)
private val surfaceContainerHighDark = Color(0xFF2E282D)
private val surfaceContainerHighestDark = Color(0xFF393338)

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
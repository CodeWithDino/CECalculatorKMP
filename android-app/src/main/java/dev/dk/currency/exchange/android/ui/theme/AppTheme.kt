package dev.dk.currency.exchange.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.dk.currency.exchange.android.ui.widgets.CECalculatorTypography

@Composable
fun CurrencyConversionAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = colorPrimary,
            primaryVariant = colorPrimaryVariant,
            secondary = colorSecondary,
            onSurface = textColor(true)
        )
    } else {
        lightColors(
            primary = colorPrimary,
            primaryVariant = colorPrimaryVariant,
            secondary = colorSecondary,
            onSurface = textColor(false),
        )
    }
    val typography = CECalculatorTypography

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
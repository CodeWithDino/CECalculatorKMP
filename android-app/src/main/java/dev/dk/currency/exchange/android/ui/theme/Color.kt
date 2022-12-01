package dev.dk.currency.exchange.android.ui.theme

import androidx.compose.ui.graphics.Color

val colorPrimary = Color(0xFFD9B9FF)
val colorPrimaryVariant = Color(0xFF6300BA)
val colorSecondary = Color(0xFFCFC1DA)
fun textColor(isDark: Boolean) = if(isDark) textDark else  textLight
val textDark = Color(0xFFF0ECEC)
val textLight = Color(0xC8000000)
val gray = Color(0xFF79838B)
fun color(isDark:Boolean) = if (isDark) colorPrimary else colorPrimaryVariant
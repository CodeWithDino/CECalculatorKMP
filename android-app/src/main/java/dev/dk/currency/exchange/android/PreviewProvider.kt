package dev.dk.currency.exchange.android

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BaseUnitPreviewProvider : PreviewParameterProvider<BaseUnitForPreview> {
    override val values = sequenceOf(BaseUnitForPreview(base = "USD", "United States Dollar") {})
}

data class BaseUnitForPreview(val base: String, val name: String, val onClick: () -> Unit)

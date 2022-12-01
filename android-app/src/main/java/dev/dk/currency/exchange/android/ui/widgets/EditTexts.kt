package dev.dk.currency.exchange.android.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.dk.currency.exchange.android.ui.theme.gray

@Preview(showBackground = true)
@Composable
fun OutLineEdittextNumberPreview() {

    var text by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = { txt ->
                text = txt
            },
            textStyle = TextStyle(textAlign = TextAlign.End),
            placeholder = { Text("sample hint", color = MaterialTheme.colors.onSurface) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(size2dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                unfocusedIndicatorColor = gray,
                textColor = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}



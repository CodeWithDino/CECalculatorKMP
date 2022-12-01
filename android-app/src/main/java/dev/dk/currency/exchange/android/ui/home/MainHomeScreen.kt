package dev.dk.currency.exchange.android.ui.home
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dk.currency.exchange.android.R
import dev.dk.currency.exchange.android.BaseUnitForPreview
import dev.dk.currency.exchange.android.BaseUnitPreviewProvider
import dev.dk.currency.exchange.android.ui.theme.colorPrimaryVariant
import dev.dk.currency.exchange.android.ui.theme.gray
import dev.dk.currency.exchange.android.ui.theme.textDark
import dev.dk.currency.exchange.android.ui.widgets.GenericListViewRenderer
import dev.dk.currency.exchange.android.ui.widgets.LargeBoldText
import dev.dk.currency.exchange.android.ui.widgets.MediumTextBold
import dev.dk.currency.exchange.android.ui.widgets.OutLineEdittextNumberPreview
import dev.dk.currency.exchange.android.ui.widgets.largePadding
import dev.dk.currency.exchange.android.ui.widgets.mediumPadding
import dev.dk.currency.exchange.android.ui.widgets.size10dp
import dev.dk.currency.exchange.android.ui.widgets.size1dot5dp
import dev.dk.currency.exchange.android.ui.widgets.size2dp
import dev.dk.currency.exchange.android.ui.widgets.smallPadding
import dev.dk.currency.exchange.android.ui.widgets.spacing4dp
import dev.dk.currency.exchange.android.ui.widgets.spacing6dp
import dev.dk.currency.exchange.android.utils.localeToEmoji
import dev.dk.currency.exchange.android.utils.parseAmount
import dev.dk.currency.exchange.android.utils.to2Dp
import dev.dk.currency.exchange.android.utils.toLocalCurrency
import dev.dk.currency.exchange.data.CurrencyRates
import dev.dk.currency.exchange.data.CurrencyRecord
import dev.dk.currency.exchange.data.model.OnResultObtained
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

enum class BottomSheetScreen {
    VIEW_SELECTED
}

@Composable
fun MainHomeRoute(
    viewModel: MainHomeViewModel = getViewModel()
) {
    val currencyRatesResult = viewModel.currencyRates.collectAsState()
    val baseRatesResult = viewModel.baseCurrency.collectAsState()
    viewModel.getCurrencyRates()
    MainHomeScreen(currencyRatesResult, baseRatesResult, onClick = { currencyRecord, value ->
        viewModel.setBaseCurrency(currencyRecord)
        viewModel.calculateConversion(
            if (value.isNotEmpty()) parseAmount(
                value
            ) else 0.0
        )
    }, onCalculateConversion = {
        viewModel.calculateConversion(it)
    })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainHomeScreen(
    currencyRatesResult: State<OnResultObtained<CurrencyRates>>,
    base: State<CurrencyRecord>,
    onClick: (CurrencyRecord, String) -> Unit,
    onCalculateConversion: (Double) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    var currencyText by remember { mutableStateOf(TextFieldValue(text = "")) }

    val coroutinesScope = rememberCoroutineScope()

    fun hideBottomSheet() {
        coroutinesScope.launch { modalBottomSheetState.hide() }
    }

    fun showBottomSheet() {
        coroutinesScope.launch { modalBottomSheetState.show() }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    var bottomSheetView by remember {
        mutableStateOf(BottomSheetScreen.VIEW_SELECTED)
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = size10dp, topEnd = size10dp),
        sheetContent = {
            /**
             * Bottom sheet currency list
             */
            if (bottomSheetView == BottomSheetScreen.VIEW_SELECTED) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(smallPadding)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(smallPadding))
                    {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            MediumTextBold(
                                text = stringResource(id = R.string.select_currency), // Select Base currency
                                modifier = Modifier.align(
                                    Alignment.CenterHorizontally
                                )
                            )
                        }
                        val filteredItems = currencyRatesResult.value.result?.rates
                        GenericListViewRenderer(
                            filteredItems,
                            loadComplete = currencyRatesResult.value.isLoaded,
                            emptyStateValue = stringResource(
                                id = R.string.no_items
                            )
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(spacing6dp)
                            ) {
                                items(filteredItems!!) { cur ->
                                    CurrencyNameViewItem(currency = cur) { baseSelectedCurrency ->
                                        onClick(baseSelectedCurrency, currencyText.text)
                                        hideBottomSheet()
                                        keyboardController?.hide()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        sheetState = modalBottomSheetState
    ) {
        Scaffold(
            topBar = {
                LargeBoldText(
                    text = stringResource(id = R.string.app_name), modifier = Modifier.padding(
                        largePadding
                    ), fontStyle = FontStyle.Italic
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { pd ->

            pd

            BackHandler(enabled = modalBottomSheetState.isVisible) {
                coroutinesScope.launch {
                    modalBottomSheetState.hide()
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {

                Column(
                    Modifier.padding(smallPadding),
                    verticalArrangement = Arrangement.spacedBy(mediumPadding)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(   //
                            value = currencyText,
                            onValueChange = { newTxt ->
                                val value = if (newTxt.text.isNotEmpty())
                                    newTxt.copy(
                                        newTxt.text.toLocalCurrency(),
                                        TextRange(newTxt.text.length + 1)
                                    )
                                else
                                    newTxt.copy("")
                                currencyText = value
                                onCalculateConversion(
                                    if (currencyText.text.trim()
                                            .isNotEmpty()
                                    ) parseAmount(newTxt.text) else 0.0
                                )

                            },
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            ),
                            label = {
                                Text(
                                    stringResource(
                                        id = R.string.enter_amount,
                                        base.value.code ?: ""
                                    ),
                                    color = MaterialTheme.colors.onSurface,
                                    modifier = Modifier.align(
                                        Alignment.CenterEnd
                                    )
                                )
                            },
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

                    val baseUnitForPreview = BaseUnitForPreview(
                        base.value.code!!,
                        currencyRatesResult.value.result?.rates?.firstOrNull { it.code == base.value.code }?.name
                            ?: stringResource(id = R.string.default_base)
                    ) {
                        bottomSheetView = BottomSheetScreen.VIEW_SELECTED
                        showBottomSheet()
                    }
                    CurrencyButtonView(baseUnitForPreview)

                    GenericListViewRenderer(
                        currencyRatesResult.value.result?.rates,
                        loadComplete = currencyRatesResult.value.isLoaded
                    ) {
                        // Conversion goes here
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(
                                mediumPadding
                            )
                        ) {
                            items(currencyRatesResult.value.result!!.rates) { currency ->
                                CurrencyItem(currency = currency)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun MainHomeScreenPreview() {
    BottomSheetScaffold(topBar = {
        MediumTextBold(
            text = stringResource(id = R.string.app_name), modifier = Modifier.padding(
                largePadding
            ), fontStyle = FontStyle.Italic
        )
    }, sheetContent = {
        Box(Modifier.fillMaxWidth()) {
        }
    }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.padding(smallPadding), verticalArrangement = Arrangement.spacedBy(
                    mediumPadding
                )
            ) {
                OutLineEdittextNumberPreview()
                val baseUnitForPreview = BaseUnitForPreview("US", "United States Dollar") {
                    // OnClick
                }
                CurrencyButtonView(baseUnitForPreview)
                // Conversion go here
                LazyColumn {
                }
            }
        }
    }
}

@Composable
@Preview
fun CurrencyButtonView(@PreviewParameter(BaseUnitPreviewProvider::class) baseUnitForPreview: BaseUnitForPreview) {
    Card(
        Modifier
            .wrapContentWidth()
            .clickable { baseUnitForPreview.onClick() },
        backgroundColor = colorPrimaryVariant
    ) {
        Row(
            Modifier
                .wrapContentWidth()
                .padding(largePadding),
            horizontalArrangement = Arrangement.spacedBy(spacing6dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            MediumTextBold(
                text = stringResource(
                    id = R.string.current_currency,
                    localeToEmoji(baseUnitForPreview.base.dropLast(1)),
                    baseUnitForPreview.name
                ),
                color = textDark,
                textAlign = TextAlign.Center
            )

            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "", tint = textDark)
        }
    }

}

@Composable
fun CurrencyItem(currency: CurrencyRecord) {
    Box(
        Modifier
            .fillMaxWidth()
            .border(
                size1dot5dp, gray, RoundedCornerShape(2.dp)
            )
    ) {
        Row(
            Modifier.padding(horizontal = largePadding, vertical = smallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediumTextBold(
                text = stringResource(
                    id = R.string.current_currency,
                    currency.code!!,
                    localeToEmoji(currency.code!!.dropLast(1))
                ), maxLines = 1
            )
            Spacer(Modifier.weight(1f))
            MediumTextBold(
                text = currency.conversion?.to2Dp(),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
    }
}

@Composable
fun CurrencyNameViewItem(currency: CurrencyRecord, onClick: (CurrencyRecord) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing6dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing4dp, horizontal = mediumPadding)
            .clickable { onClick(currency) }
    ) {
        MediumTextBold(
            text = stringResource(
                id = R.string.currency_with_logo,
                currency.code!!,
                localeToEmoji(currency.code!!.dropLast(1))
            ), maxLines = 1
        )
        MediumTextBold(text = currency.name)
    }
}
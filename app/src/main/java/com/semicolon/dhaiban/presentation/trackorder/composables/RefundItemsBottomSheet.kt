package com.semicolon.dhaiban.presentation.trackorder.composables

import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RefundItemsBottomSheet(
    orderNumber: String,
    productName: String,
    productImage: String,
    quantity: Int,
    totalPrice: Double,
    exchangeRate: Double,
    currencySymbol: String,
    errorMessage: String,
    loadingState: Boolean,
    onDismiss: () -> Unit,
    onClickNext: () -> Unit
) {
    val context = LocalContext.current
    var productCount by remember { mutableIntStateOf(1) }
    var showToast by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(Unit) { onDispose { coroutineScope.cancel() } }
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        val startColor = Theme.colors.transparent
        val endColor = Theme.colors.black.copy(alpha = 0.2f)

        val backgroundColor = remember { Animatable(startColor) }

        LaunchedEffect(Unit) {
            backgroundColor.animateTo(endColor, animationSpec = tween(1000))
        }

        Box(
            Modifier
                .noRippleEffect { onDismiss() }
                .weight(2f)
                .fillMaxWidth()
                .background(backgroundColor.value)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(backgroundColor.value),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(top = 24.dp, end = 16.dp, start = 16.dp)
                    .safeDrawingPadding(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = Theme.strings.refundItems,
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.black
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .height(1.dp)
                        .background(Theme.colors.silver.copy(alpha = 0.5f))
                )

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CoilImage(
                            modifier = Modifier
                                .height(80.dp)
                                .width(100.dp),
                            url = productImage,
                            contentDescription = "Product Image"
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = Theme.strings.orderNumber,
                            style = Theme.typography.body.copy(fontWeight = FontWeight.W400)
                        )
                        Text(text = orderNumber, color = Theme.colors.greyishBrown)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = Theme.strings.product,
                            style = Theme.typography.body.copy(fontWeight = FontWeight.W400)
                        )
                        Text(text = productName, color = Theme.colors.greyishBrown)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Theme.strings.qtyToRefund,
                            style = Theme.typography.body.copy(fontWeight = FontWeight.W400)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    if (productCount != 1) {
                                        productCount--
                                    }
                                },
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(30.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Theme.colors.buttonTextGray,
                                    containerColor = Theme.colors.antiqueLace
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 0.dp)

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.minus_icon),
                                    contentDescription = "Minus Button"
                                )
                            }

                            Text(
                                text = productCount.toString(),
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Button(
                                onClick = {
                                    if (productCount < quantity) {
                                        productCount++
                                    } else {
                                        showToast = true
                                        coroutineScope.launch {
                                            delay(2000)
                                            showToast = false
                                        }
                                    }
                                },
                                modifier = Modifier.size(30.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Theme.colors.buttonTextGray,
                                    containerColor = Theme.colors.antiqueLace
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 0.dp)

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.plus_icon),
                                    contentDescription = "Plus Button"
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = Theme.strings.paymentDetails,
                            style = Theme.typography.titleLarge
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = Theme.strings.price,
                            color = Theme.colors.greyishBrown,
                            style = Theme.typography.caption
                        )
                        Text(
                            text = String.format(
                                "%.2f",
                                totalPrice * quantity * exchangeRate
                            ) + " $currencySymbol",
                            color = Theme.colors.greyishBrown,
                            style = Theme.typography.caption
                        )
                    }

                    if (showToast) {
                        Toast.makeText(
                            context,
                            Theme.strings.notAvailable,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = errorMessage,
                                style = Theme.typography.body,
                                color = Theme.colors.error
                            )
                        }
                    }

                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = Theme.strings.next,
                        isLoading = loadingState,
                        onClick = {
                            onClickNext()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderInfoPreview() {
    DhaibanTheme {
        RefundItemsBottomSheet(
            orderNumber="" ,
            productName= "String",
            productImage ="",
            quantity =1,
            totalPrice =0.0,
            exchangeRate=0.0,
            currencySymbol="",
            errorMessage="",
            loadingState=false,
            onDismiss={},
        onClickNext={}
        )

    }
}
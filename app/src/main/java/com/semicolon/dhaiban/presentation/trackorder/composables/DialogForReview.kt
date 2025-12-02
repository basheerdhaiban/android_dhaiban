package com.semicolon.dhaiban.presentation.trackorder.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.presentation.orders.OrderProductUiState
import com.semicolon.dhaiban.presentation.product.composables.RatingBar
import com.semicolon.dhaiban.presentation.trackorder.TrackOrderScreenModel
import com.semicolon.dhaiban.presentation.trackorder.TrackOrderScreenUiState

@Composable
fun RatingDialog(
    onDismissRequest: () -> Unit, onPositive: () -> Unit, productName: String,currencySymbol: String,totalPrice: Double,
    productImage: String,state: TrackOrderScreenUiState ,model : TrackOrderScreenModel
) {
    Dialog(onDismissRequest =  onDismissRequest ) {
        Box(


            modifier = Modifier
                .background(Theme.colors.white, shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))

                .padding(16.dp),


            ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    CoilImage(
                        modifier = Modifier
                            .width(100.dp)

                            .height(120.dp),
                        url =productImage,
                        contentDescription = "Product Image",
                        shape = RoundedCornerShape(12.dp)
                    )
                    Column {
                        Text(
                            text = productName,
                            modifier = Modifier.padding(5.dp),
                            style = Theme.typography.otherHeading
                        )
                        Row(modifier = Modifier.padding(start = 5.dp)) {
                            Text(
                                text = totalPrice.toString(),
                                color = Theme.colors.primary,
                                style = Theme.typography.caption
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currencySymbol,
                                color = Theme.colors.primary,
                                style = Theme.typography.caption
                            )
                        }

                    }


                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(19.dp)
                ) {
                    var rating1 by remember {
                        mutableDoubleStateOf(0.0)
                    }
                    rating1=state.rate.toDouble()
                    Text(text = "Rate :")
                    RatingBar(
                        modifier = Modifier
                            .size(25.dp),
                        rating = rating1,
                        onRatingChanged = {model.onRateChange(it.toFloat())},
                        starsColor = Theme.colors.ratingBarcolor,

                    )



                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),

                    ) {
                    var text by remember {
                        mutableStateOf("")
                    }
                    Text(text = "Review :")

                    BasicTextField(
                        value = state.review,
                        onValueChange = {model.onReviewTextChange(it)},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .height(76.dp)
                            .background(Theme.colors.greyOfTextField)
                            .padding(start = 5.dp, top = 4.dp)
                    )


                }
                Spacer(modifier = Modifier.padding(12.dp))
                Button(

                    modifier = Modifier
                        .fillMaxWidth()


                        .padding(start = 55.dp),
                    onClick = { onPositive() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Theme.colors.mediumBrown
                    ),
                    shape = RoundedCornerShape(8.dp),

                    ) {
                    Text(
                        text = "save",
                        fontSize = 10.sp,
                        style = Theme.typography.otherHeading ,
                        color = Theme.colors.white,

                        )
                }

            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProductItem() {
//    val orderUstate = OrderProductUiState()
//    DhaibanTheme {
//        RatingDialog({}, {} , productImage = "", productName = "" , totalPrice = 0.0, currencySymbol = "", state = screenModel.state.collectAsState()
//        )
//    }
//}

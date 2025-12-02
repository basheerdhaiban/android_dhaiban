package com.semicolon.dhaiban.presentation.payment.composables

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.presentation.payment.PaymentScreenModel
import com.semicolon.dhaiban.presentation.payment.PaymentScreenUiState

@Composable
fun WalletDialog(amount :Double ,state:PaymentScreenUiState,model: PaymentScreenModel,
                 onDismissRequest: () -> Unit, onPositive: () -> Unit
) {
    val context =LocalContext.current
    val borderColor by animateColorAsState(
        targetValue = Color.LightGray
    )
    Dialog(onDismissRequest = onDismissRequest) {
        Box(


            modifier = Modifier
                .background(Theme.colors.white, shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))

                .padding(16.dp),


            ) {
            Column(modifier = Modifier.padding(0.dp)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.align(Alignment.Center)) {
                        Image(modifier = Modifier
                            .width(30.dp)
                            .height(22.dp)
                            .padding(top = 6.dp),
                            painter = painterResource(id = R.drawable.wallet),
                            contentDescription = ""
                        )
                        Text(
                            text = Theme.strings.useYourWallet,
                            textAlign = TextAlign.Center,
                            style = Theme.typography.title.copy(                    fontWeight = FontWeight(
                                650
                            )),
                            color = Theme.colors.black ,
                            modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                        )

                    }
                }
                Spacer(modifier = Modifier.height(9.dp))
                Divider(thickness = 1.5.dp)
                Spacer(modifier = Modifier.height(11.dp))

                Text(text = Theme.strings.availableInYourWallet ,style = Theme.typography.title.copy(
                    fontSize = 14.sp

                ))
                Text( text = String.format(
                    "%.1f",
                    state.balance * state.currency.exchangeRate
                ) + " " + state.currency.symbol, modifier = Modifier.padding(top = 6.dp) ,style = Theme.typography.body, color = Theme.colors.mediumBrown)
                Spacer(modifier = Modifier.height(15.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Row {

                        OutlinedTextField(
                            colors = TextFieldDefaults.colors().copy(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                            modifier = Modifier
//                                .border(
//                                    2.dp,
//                                    color = borderColor,
//                                    RoundedCornerShape(Theme.radius.medium)
//                                )
                                .width(210.dp),
                            value = state.paidFromBalance,
                            onValueChange = {
                                if (it.isEmpty()) {
                                    model.onWalletBalanceTextChange("")
                                } else {
                                    val numberPattern = Regex("^[0-9]*\\.?[0-9]*$")
                                    if (numberPattern.matches(it)) {
                                        model.onWalletBalanceTextChange(it)  }

                                }}
                            ,

                            )
                    }
                    Spacer(modifier = Modifier.width(6.dp))

                    Button(modifier = Modifier.padding(top = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            val amountOfPaid: Double = state.paidFromBalance.toDoubleOrNull() ?: 0.0

                            if (amountOfPaid > state.balance) {
                                Toast.makeText(context,
                                    context.getString(R.string.your_balance_is_not_allowed),Toast.LENGTH_LONG).show()
                            }
                            else if (amountOfPaid>state.total){
                                Toast.makeText(context,
                                    context.getString(R.string.the_price_of_product_is, state.total),Toast.LENGTH_LONG).show()

                            }

                            else if(amountOfPaid != 0.0) {
                                onPositive()
                            }


                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Theme.colors.mediumBrown
                        )
                    ) {

                        Text(
                            text = "USE", style = Theme.typography.otherHeading,
                            color = Theme.colors.white
                        )
                    }
                    

                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItem() {
    DhaibanTheme {
//        WalletDialog( 0.0, PaymentScreenUiState(),{}, {})
    }
}

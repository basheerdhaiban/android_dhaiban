package com.semicolon.dhaiban.presentation.payment.composables


import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
fun MomoPayDialog(state:PaymentScreenUiState,model: PaymentScreenModel,
                  onDismissRequest: () -> Unit, onPositive: () -> Unit
) {
    val context = LocalContext.current
    val borderColor by animateColorAsState(
        targetValue = Color.LightGray
    )
    Dialog(onDismissRequest = onDismissRequest) {
        Box(


            modifier = Modifier
                .background(Theme.colors.white, shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))

                .padding(10.dp),


            ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(15.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                        Image(modifier = Modifier.width(30.dp).height(22.dp).padding(0.dp),
                            painter = painterResource(id = R.drawable.momo),
                            contentDescription = ""
                        )
                        Text(
                            text = Theme.strings.momoPay,

                            textAlign = TextAlign.Center,
                            style = Theme.typography.title.copy(
                                fontWeight = FontWeight(
                                    650
                                )
                            ),
                            color = Theme.colors.black,
                            modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                        )

                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Box(
                    modifier = Modifier

                        .background(
                            Theme.colors.whiteTwo,
                            RoundedCornerShape(Theme.radius.small)
                        )
                ) {
                    Column (modifier = Modifier.padding(start = 2.dp)){
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = Theme.strings.mobileNumber,style = Theme.typography.title.copy(
                                fontSize = 14.sp

                            ))
                            Checkbox(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 5.dp),
                                checked = state.mobileNumberCheckBox
//                            state.cashCheckBox
                                ,
                                onCheckedChange = {

//                                listener.onCheckCash(it)
                                    model.handleCheckBoxOfMomo(isCheckMobileNumber  = true)}
                                ,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Theme.colors.mediumBrown,
                                    uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
                        }
                        Divider(thickness = 1.5.dp, color = Theme.colors.white, modifier = Modifier.padding(horizontal = 8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = Theme.strings.partyCode,style = Theme.typography.title.copy(
                                fontSize = 14.sp

                            ))
                            Checkbox(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 5.dp),
                                checked = state.partyCodeCheckBox
//                            state.cashCheckBox
                                ,
                                onCheckedChange = {model.handleCheckBoxOfMomo(isCheckPartyCode = true)},
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Theme.colors.mediumBrown,
                                    uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
                        }
                        Divider(thickness = 1.5.dp, color = Theme.colors.white, modifier = Modifier.padding(horizontal = 8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = Theme.strings.email,style = Theme.typography.title.copy(
                                fontSize = 14.sp

                            ))
                            Checkbox(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 5.dp),
                                checked = state.emailCheckBox
//                            state.cashCheckBox
                                ,
                                onCheckedChange = {
//                                listener.onCheckCash(it)
                                    model.handleCheckBoxOfMomo (isCheckEmail = true)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Theme.colors.mediumBrown,
                                    uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
                        }



                        Divider(thickness = 1.5.dp, color = Theme.colors.white, modifier = Modifier.padding(horizontal = 8.dp))
                        Spacer(modifier = Modifier.height(15.dp))

                        Row(horizontalArrangement = Arrangement.SpaceBetween , modifier = Modifier.padding(end = 5.dp)) {
                            Row {

                                OutlinedTextField(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(50.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp)
                                    ,
                                    value =  state.numberOfAccountMomoPay ,
                                    onValueChange = { model.onMomoPayAccountTextChange(it)
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Theme.colors.white,
                                        unfocusedIndicatorColor = Theme.colors.white,
                                        unfocusedContainerColor = Theme.colors.white,
                                        disabledContainerColor = Theme.colors.white,
                                        disabledIndicatorColor = Theme.colors.white,
                                        focusedContainerColor = Theme.colors.white,
                                        cursorColor = Theme.colors.mediumBrown,
                                        selectionColors = TextSelectionColors(
                                            handleColor = Theme.colors.mediumBrown,
                                            backgroundColor = Theme.colors.white
                                        )
                                    )
                                    ,
//                                    shape = RoundedCornerShape(16.dp),

                                )
                            }
                            Spacer(modifier = Modifier.width(7.dp))

                            Button(modifier = Modifier.padding(top = 2.dp),
                                shape = RoundedCornerShape(8.dp),
                                onClick = {
                                    if (!state.numberOfAccountMomoPay.startsWith("25") && (state.mobileNumberCheckBox)){
                                        Toast.makeText(context,"You Phone Should Start with 25",Toast.LENGTH_SHORT).show()
                                    }
                                else{
                                        onPositive()

                                    }


                                }, colors = ButtonDefaults.buttonColors(
                                    containerColor = Theme.colors.mediumBrown
                                )
                            ) {

                                Text(
                                    text = Theme.strings.send, style = Theme.typography.otherHeading,
                                    color = Theme.colors.white
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(25.dp))
                    }


                }
                Spacer(modifier = Modifier.height(25.dp))

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MomoPay() {
    DhaibanTheme {
//        MomoPayDialog(onDismissRequest = {  }) {
//
//        }
    }
}

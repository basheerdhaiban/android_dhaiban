package com.semicolon.dhaiban.presentation.authuntication.forgetPassword

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanMoblieTextField
import com.semicolon.dhaiban.designSystem.composables.DhaibanTextField
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.authuntication.composable.VerificationContainer
import com.semicolon.dhaiban.presentation.authuntication.otp.OtpScreen
import kotlinx.coroutines.flow.collectLatest
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS

class ForgetPasswordScreen () : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ForgetPasswordScreenModel>()
        val state: ForgetPasswordUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow


        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ForgetPasswordUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    is ForgetPasswordUiEffect.OnNavigateToOtpScreen -> {
                        navigator.push(
                            OtpScreen(
                                phoneNumber = effect.phoneNumber,
                                otpCode = effect.otpCode
                            )
                        )
                    }
                }
            }
        }

        ForgetPasswordScreenContent(state, screenModel)
    }


    @Composable
    fun ForgetPasswordScreenContent(
        state: ForgetPasswordUiState,
        listener: ForgetPasswordInteractionListener,
    ) {
        val context = LocalContext.current
        val phoneNumber = state.phoneNumber
        val email = state.email
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            VerificationContainer(
                title = Theme.strings.forgetPasswordAppBarTitle,
                description = Theme.strings.enterPhoneNumberToResetPassword,
                onClickBackIcon = listener::onNavigateBack,
            ) {


                AnimatedVisibility(visible = state.loginWithPhoneNum,
                    modifier = Modifier.padding(horizontal = 16.dp)) {
                    DhaibanTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        label = Theme.strings.email,
                        text = email,
                        onValueChange = listener::onEmailValueChanged,
                        hint = Theme.strings.emailHint,
                        leadingIcon = R.drawable.icon_email,
                        errorMessage = state.errorMessage
                    )
                }

                AnimatedVisibility(visible = !state.loginWithPhoneNum,
                    modifier = Modifier.padding(horizontal = 16.dp)) {
                    DhaibanMoblieTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        label = Theme.strings.mobileNumber,
                        text = phoneNumber,
                        onValueChange = listener::onPhoneValueChanged,
                        hint = Theme.strings.mobileNumber,
//                        hint = Theme.strings.phoneNumberHint,
                        leadingIcon = R.drawable.icon_phone,
                        keyboardType = KeyboardType.Phone,
                        errorMessage = state.errorMessage,
                        onCountryChange = {
                            state.countryCode = it.countryPhoneNumberCode
                            Log.d("countryCode", it.countryCode)
                            Log.d("countryPhoneNumberCode", it.countryPhoneNumberCode)
                        }
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = Theme.strings.loginBy,
                        color = Theme.colors.black87,
                        style = Theme.typography.body,
                    )

                    AnimatedVisibility(visible = state.loginWithPhoneNum) {
                        Text(
                            modifier = Modifier.noRippleEffect {
                                listener.onEmailValueChanged("")
                                listener.onPhoneValueChanged("")
                                listener.onChangeAuthOption()
                            },
                            text = stringResource(R.string.phone_number),
                            color = Theme.colors.primary,
                            style = Theme.typography.title
                        )
                    }

                    AnimatedVisibility(visible = !state.loginWithPhoneNum) {
                        Text(
                            modifier = Modifier.noRippleEffect {
                                listener.onEmailValueChanged("")
                                listener.onPhoneValueChanged("")
                                listener.onChangeAuthOption()
                            },
                            text = stringResource(R.string.email),
                            color = Theme.colors.primary,
                            style = Theme.typography.title
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .align(Alignment.Bottom),
                        text = Theme.strings.confirm,
                        isLoading = state.isLoading,
                        onClick = {
                            if (!state.loginWithPhoneNum) {
                                val validNumber = listener.validatePhoneNumber(
                                    countryCode = state.countryCode,
                                    phoneNumber = state.phoneNumber,
                                )
                                if (!validNumber.isNullOrEmpty()) {
                                    listener.onClickConfirmButton(
                                        phoneNumber = validNumber,
                                        email = state.email
                                    )
                                } else {
                                    Toast.makeText(context,
                                        context.getString(R.string.invalid_mobile_number),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                val validEmail = listener.validateEmail(
                                    email = state.email
                                )
                                if (validEmail.isNullOrEmpty()) {
//                                val validEmail = state.email.contains('@') && state.email.contains('.')
//                                if (validEmail) {
                                    listener.onClickConfirmButton(
                                        phoneNumber = "",
                                        email = state.email
                                    )
                                } else {
                                    Toast.makeText(context,
                                        context.getString(R.string.invalid_email),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForgetPasswordScreen() {
    val dummyListener = object : ForgetPasswordInteractionListener {
        override fun onClickConfirmButton(phoneNumber: String, email: String) {}
        override fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String? { return null }
        override fun validateEmail(email: String?): String? { return null }
        override fun onNavigateBack() {}
        override fun onMobileNumberValueChanged(mobileNumber: String) {}
        override fun onEmailValueChanged(email: String) {}
        override fun onPhoneValueChanged(phone: String) {}
        override fun onChangeAuthOption() {}
    }
    DhaibanTheme {
        ForgetPasswordScreen().ForgetPasswordScreenContent(
            state = ForgetPasswordUiState(),
            listener = dummyListener
        )
    }
}
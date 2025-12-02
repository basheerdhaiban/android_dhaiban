package com.semicolon.dhaiban.presentation.authuntication.otp

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.authuntication.composable.OtpTextField
import com.semicolon.dhaiban.presentation.authuntication.composable.VerificationContainer
import com.semicolon.dhaiban.presentation.authuntication.resetPassword.ResetPasswordScreen
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class OtpScreen(
    private val email: String = "",
    private val phoneNumber: String = "",
    private val username: String = "",
    private val isFromSignUpScreen: Boolean = false,
    private val otpCode: String = ""
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OtpScreenModel>(
            parameters = { parametersOf(email, phoneNumber, isFromSignUpScreen, otpCode,username) }
        )
        val state: OtpUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
val context = LocalContext.current
        LaunchedEffect(key1 = state.isotpError) {
            if (state.isotpError){
                Toast.makeText(context,"Otp is error",Toast.LENGTH_LONG).show()
            }
            state.isotpError=false
        }
        LaunchedEffect(key1 = state.messageForResend) {
            if (state.messageForResend=="Code send successfully"){
                Toast.makeText(context,"Code send successfully",Toast.LENGTH_LONG).show()
                state.messageForResend=""
            }

        }

        LaunchedEffect(Unit) {


            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    OtpScreenUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    is OtpScreenUiEffect.OnNavigateToResetPasswordScreen -> {
                        navigator.push(ResetPasswordScreen(effect.otpCode, effect.phoneNumber))
                    }

                    OtpScreenUiEffect.OnNavigateToHomeScreen -> {
                        navigator.replaceAll(MainContainer())
                    }
                }
            }
        }

        OtpScreenContent(state = state, listener = screenModel)

    }

    @Composable
    fun OtpScreenContent(state: OtpUiState, listener: OtpScreenInteractionListener) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {

            val screenTitle: String =
                if (isFromSignUpScreen) {
                    Theme.strings.accountVerification
                } else {
                    Theme.strings.forgetPasswordAppBarTitle
                }

            val screenDescription: String =
                if (isFromSignUpScreen || phoneNumber.isNotEmpty()) {
                    Theme.strings.otpPhoneDescription
                } else {
                    Theme.strings.otpEmailDescription
                }

            VerificationContainer(
                title = screenTitle,
                description = screenDescription,
                onClickBackIcon = { listener.onClickBackIcon() }
            ) {

                var firstOtpValue by remember { mutableStateOf("") }
                var secondOtpValue by remember { mutableStateOf("") }
                var thirdOtpValue by remember { mutableStateOf("") }
                var fourthOtpValue by remember { mutableStateOf("") }

                val otpCode = firstOtpValue + secondOtpValue + thirdOtpValue + fourthOtpValue

                val focusRequester1 = remember { FocusRequester() }
                val focusRequester2 = remember { FocusRequester() }
                val focusRequester3 = remember { FocusRequester() }
                val focusRequester4 = remember { FocusRequester() }

                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OtpTextField(
                            value = firstOtpValue,
                            onValueChanged = {
                                firstOtpValue = it
                                if (it.length == 1) {
                                    focusRequester2.requestFocus()
                                }
                            },
                            modifier = Modifier.focusRequester(focusRequester1),
                            errorMessage = state.errorMessage
                        )

                        OtpTextField(
                            value = secondOtpValue,
                            onValueChanged = {
                                secondOtpValue = it
                                if (it.length == 1) {
                                    focusRequester3.requestFocus()
                                }
                            },
                            modifier = Modifier.focusRequester(focusRequester2),
                            errorMessage = state.errorMessage
                        )

                        OtpTextField(
                            value = thirdOtpValue,
                            onValueChanged = {
                                thirdOtpValue = it
                                if (it.length == 1) {
                                    focusRequester4.requestFocus()
                                }
                            },
                            modifier = Modifier.focusRequester(focusRequester3),
                            errorMessage = state.errorMessage
                        )

                        OtpTextField(
                            value = fourthOtpValue,
                            onValueChanged = {
                                fourthOtpValue = it
                            },
                            modifier = Modifier.focusRequester(focusRequester4),
                            errorMessage = state.errorMessage
                        )

                    }
                    AnimatedVisibility(visible = state.errorMessage.isNotEmpty()) {
                        Text(
                            text = state.errorMessage,
                            style = Theme.typography.body,
                            color = Theme.colors.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    Text(
                        modifier = Modifier.noRippleEffect { listener.onClickResendOtp() },
                        text = Theme.strings.resendCode,
                        style = Theme.typography.body,
                        color = Theme.colors.primary,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 24.dp)
                    ) {
                        PrimaryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Bottom),
                            text = Theme.strings.confirm,
                            isLoading = state.isLoading,
                            onClick = { listener.onClickConfirm(otpCode) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewResetPasswordScreen() {
    val dummyListener = object : OtpScreenInteractionListener {
        override fun onClickBackIcon() {}

        override fun onClickResendOtp() {}

        override fun onClickConfirm(otpVerificationCode: String) {}
    }
    DhaibanTheme {
        OtpScreen().OtpScreenContent(
            state = OtpUiState(),
            listener = dummyListener
        )
    }
}
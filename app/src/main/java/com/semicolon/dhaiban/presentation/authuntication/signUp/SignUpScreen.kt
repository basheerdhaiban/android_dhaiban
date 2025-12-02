package com.semicolon.dhaiban.presentation.authuntication.signUp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanMoblieTextField
import com.semicolon.dhaiban.designSystem.composables.DhaibanTextField
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.WhatsAppButton
import com.semicolon.dhaiban.presentation.authuntication.SMSOption
import com.semicolon.dhaiban.presentation.authuntication.composable.AuthContainer
import com.semicolon.dhaiban.presentation.authuntication.composable.SMSOptionSelector
import com.semicolon.dhaiban.presentation.authuntication.login.LoginScreen
import com.semicolon.dhaiban.presentation.authuntication.otp.OtpScreen
import kotlinx.coroutines.flow.collectLatest

class SignUpScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {

        val screenModel = getScreenModel<SignUpScreenModel>()
        val state: SignUpScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    SignUpScreenUiEffect.OnNavigateToLoginScreen -> navigator.push(LoginScreen)
                    SignUpScreenUiEffect.OnNavigateToOtpScreen -> navigator.push(
                        OtpScreen(isFromSignUpScreen = true, username = state.username)
                    )

                    else -> {}
                }
            }
        }

        SignUpScreenContent(state, screenModel)

    }

    @Composable
    fun SignUpScreenContent(
        state: SignUpScreenUiState,
        listener: SignUpScreenInteractionListener,
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AuthContainer(
                modifier = Modifier.fillMaxSize(),
                logo = R.drawable.logo,
                headerText = Theme.strings.signup,
                actionText = Theme.strings.logIn,
                actionDescription = Theme.strings.alreadyHaveAnAccount,
                onClickAction = listener::onClickLogin
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {


                    DhaibanTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        label = Theme.strings.username,
                        text = state.username,
                        onValueChange = listener::onUsernameValueChanged,
                        keyboardType = KeyboardType.Text,
                        hint = Theme.strings.usernameHint,
                        leadingIcon = R.drawable.icon_person,
                        errorMessage = state.usernameErrorMessage
                    )



                    DhaibanMoblieTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        label = Theme.strings.mobileNumber,
                        text = state.phoneNumber,
                        onValueChange = listener::onMobileNumberValueChanged ,
                        hint = Theme.strings.phoneNumberHint,
                        leadingIcon = R.drawable.icon_phone,
                        keyboardType = KeyboardType.Phone,
                        errorMessage = state.phoneErrorMessage,
                        onCountryChange = {
                            state.countryCode=it.countryPhoneNumberCode
                            Log.d("countryCode","${it.countryCode}")
                            Log.d("countryPhoneNumberCode","${it.countryPhoneNumberCode}")
                        }
                    )

                    DhaibanTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        label = Theme.strings.email,
                        text = state.email,
                        onValueChange = listener::onEmailValueChanged,
                        keyboardType = KeyboardType.Email,
                        hint = Theme.strings.emailHint,
                        leadingIcon = R.drawable.icon_email,
                        errorMessage = state.emailErrorMessage
                    )

                    DhaibanTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        label = Theme.strings.password,
                        text = state.password,
                        onValueChange = listener::onPasswordValueChanged,
                        keyboardType = KeyboardType.Password,
                        hint = Theme.strings.newPasswordHint,
                        leadingIcon = R.drawable.icon_password,
                        errorMessage = state.passwordErrorMessage
                    )

                    var currentSelectedOption by remember { mutableStateOf(SMSOption.WhatsApp.value) }
                    SMSOptionSelector(
                        selectedOption = currentSelectedOption,
                        onOptionSelected = {
                            currentSelectedOption = it;
                        })

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        println("state loading  = ${state.isLoading}")
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = Theme.strings.signup,
                            isLoading = state.isLoading,
                            onClick = {
                                val validNumber = listener.validatePhoneNumber(
                                    countryCode = state.countryCode,
                                    phoneNumber = state.phoneNumber,
//                                    email = state.email
                                )
                                Log.d("SignUp", "PhoneNumber: ${validNumber}")
                                if (!validNumber.isNullOrEmpty()) {
                                    listener.onClickSignUp(
                                        username = state.username,
                                        email = state.email,
                                        phoneNumber = validNumber,
                                        password = state.password,
                                        verificationMethod = currentSelectedOption
                                    )
                                } else {
                                    Toast.makeText(context,
                                        context.getString(R.string.invalid_mobile_number),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }
                    WhatsAppButton(onClick = {
                            listener.openWhatsApp(context = context)
                        })
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    val dummyListener = object : SignUpScreenInteractionListener {
        override fun onClickLogin() {}

        override fun onClickSignUp(
            username: String,
            email: String,
            phoneNumber: String,
            password: String,
            verificationMethod: Int
        ) {}

        override fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String = ""

        override fun onUsernameValueChanged(userName: String) {}

        override fun onMobileNumberValueChanged(mobileNumber: String) {}

        override fun onEmailValueChanged(email: String) {}

        override fun onPasswordValueChanged(password: String) {}

        override fun openWhatsApp(context: Context) {}
    }
    DhaibanTheme {
        SignUpScreen().SignUpScreenContent(state = SignUpScreenUiState(), listener = dummyListener)
    }
}
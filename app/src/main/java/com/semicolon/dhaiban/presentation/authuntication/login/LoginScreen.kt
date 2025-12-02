package com.semicolon.dhaiban.presentation.authuntication.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
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
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.authuntication.composable.AuthContainer
import com.semicolon.dhaiban.presentation.authuntication.forgetPassword.ForgetPasswordScreen
import com.semicolon.dhaiban.presentation.authuntication.otp.OtpScreen
import com.semicolon.dhaiban.presentation.authuntication.signUp.SignUpScreen
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import kotlinx.coroutines.flow.collectLatest

object LoginScreen : Screen {
    private fun readResolve(): Any = LoginScreen
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<LoginScreenModel>()
        val state: LoginScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        // For Testing Purpose should works on debug only
//        state.email = "a.sadeq@grandmstg.com"
//        state.email = "dhaibantrading@icloud.com"
//        state.password = "Asa112233"
//        state.phoneNumber = "1008812862"
//        state.password = "ASDasd123"
//        state.email = "a@aa.com"
//        state.password = "123456789"

        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    LoginScreenUiEffect.OnNavigateToForgetPasswordScreen -> {
                        navigator.push(ForgetPasswordScreen())
                    }

                    LoginScreenUiEffect.OnNavigateToSignUpScreen -> {
                        navigator.push(SignUpScreen())
                    }

                    LoginScreenUiEffect.OnNavigateToHomeScreen -> {
                        navigator.replaceAll(MainContainer())
                    }

                    LoginScreenUiEffect.OnNavigateToOtpScreen -> {
                        navigator.push(OtpScreen(isFromSignUpScreen = true))
                    }

                    LoginScreenUiEffect.OnNavigateToForgetPasswordScreen -> {}
                    LoginScreenUiEffect.OnNavigateToHomeScreen -> {}
                    LoginScreenUiEffect.OnNavigateToOtpScreen -> {}
                    LoginScreenUiEffect.OnNavigateToSignUpScreen -> {}
//                    else -> {}
                }
            }
        }

        LoginScreenContent(state = state, listener = screenModel)

    }


    @Composable
    fun LoginScreenContent(state: LoginScreenUiState, listener: LoginScreenInteractionListener) {

        val context = LocalContext.current
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp)
        ) {


            AuthContainer(
                modifier = Modifier,
                onClickAction = { listener.onClickSignUp() },
                logo = R.drawable.logo,
                headerText = Theme.strings.loginScreenHeader,
                actionText = Theme.strings.signup,
                actionDescription = Theme.strings.donTHaveAnAccount
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    AnimatedVisibility(visible = state.loginWithPhoneNum) {
                        DhaibanTextField(
                            modifier = Modifier.padding(top = 16.dp),
                            label = Theme.strings.email,
                            text = state.email,
                            onValueChange = listener::onEmailValueChanged,
                            hint = Theme.strings.emailHint,
                            leadingIcon = R.drawable.icon_email,
                            errorMessage = state.emailErrorMessage
                        )
                    }

                    AnimatedVisibility(visible = !state.loginWithPhoneNum) {
                        DhaibanMoblieTextField(
                            modifier = Modifier.padding(top = 16.dp),
                            label = Theme.strings.mobileNumber,
                            text = state.phoneNumber,
                            onValueChange = { listener.onPhoneValueChanged(it) },
                            hint = Theme.strings.phoneNumberHint,
                            leadingIcon = R.drawable.icon_phone,
                            keyboardType = KeyboardType.Phone,
                            errorMessage = state.phoneErrorMessage,
                            onCountryChange = {
                                state.countryCode = it.countryPhoneNumberCode
                                Log.d("countryCode", "${it.countryCode}")
                                Log.d("countryPhoneNumberCode", "${it.countryPhoneNumberCode}")
                            }
                        )
                    }
                }


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
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 16.dp)
                        .align(Alignment.End)
                        .noRippleEffect { listener.onClickForgetPassword() },
                    text = Theme.strings.forgetPasswordAppBarTitle,
                    style = Theme.typography.body,
                    color = Theme.colors.black87,
                    textDecoration = TextDecoration.Underline
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.logIn,
                    onClick = {
                        /*val validNumber = listener.validatePhoneNumber(
                            countryCode = state.countryCode,
                            phoneNumber = state.phoneNumber,
//                            email = state.email
                        )

                        if (!validNumber.isNullOrEmpty()) {
                            listener.onClickLogin(
                                email = state.email,
                                phoneNumber = validNumber,
                                password = state.password
                            )
                        } else {
                            Toast.makeText(context,
                                context.getString(R.string.invalid_mobile_number),
                                Toast.LENGTH_SHORT
                            ).show()
                        }*/
                        if (!state.loginWithPhoneNum) {
                            val validNumber = listener.validatePhoneNumber(
                                countryCode = state.countryCode,
                                phoneNumber = state.phoneNumber,
                            )
                            if (!validNumber.isNullOrEmpty()) {
                                listener.onClickLogin(
                                    email = state.email,
                                    phoneNumber = validNumber,
                                    password = state.password
                                )
                            } else {
                                Toast.makeText(context,
                                    context.getString(R.string.invalid_mobile_number),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val validEmail = EMAIL_ADDRESS.matcher(state.email).matches()
//                            val validEmail = listener.validateEmail(
//                                email = state.email
//                            )
//                            if (!validEmail.isNullOrEmpty()) {
                            if (validEmail) {
                                listener.onClickLogin(
                                    email = state.email,
                                    phoneNumber = state.phoneNumber,
                                    password = state.password
                                )
                            } else {
                                Toast.makeText(context,
                                    context.getString(R.string.invalid_email),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    isLoading = state.isLoading
                )

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
                                listener.onSwitchAuthWay()
                            },
                            text = context.getString(R.string.phone_number),
//                            text = Theme.strings.phoneNumberHint,
                            color = Theme.colors.primary,
                            style = Theme.typography.title
                        )
                    }

                    AnimatedVisibility(visible = !state.loginWithPhoneNum) {
                        Text(
                            modifier = Modifier.noRippleEffect {
                                listener.onSwitchAuthWay()
                            },
                            text = Theme.strings.email,
                            color = Theme.colors.primary,
                            style = Theme.typography.title
                        )
                    }
                }
                WhatsAppButton(onClick = {
                    listener.openWhatsApp(context = context)
                })
            }
        }
    }

}

@Composable
//@Preview()
@Preview(showSystemUi = true)
//@Preview(showBackground = true)
fun PreviewLoginScreen() {
    val dummyListener = object : LoginScreenInteractionListener {
        override fun onClickLogin(email: String, phoneNumber: String, password: String) {}
        override fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String = ""
        override fun validateEmail(email: String?): String? = null
        override fun onClickSignUp() {}
        override fun onClickForgetPassword() {}
        override fun onChangeAuthOption() {}
        override fun onPhoneNumberTextChanged(phoneNumber: String) {}
        override fun onEmailTextChanged(email: String) {}
        override fun onPasswordTextChanged(password: String) {}
        override fun onSwitchAuthWay() {}
        override fun onPhoneValueChanged(phone: String) {}
        override fun onPasswordValueChanged(password: String) {}
        override fun onEmailValueChanged(email: String) {}
        override fun openWhatsApp(context: Context) {}
    }
    DhaibanTheme {
        LoginScreen.LoginScreenContent(state = LoginScreenUiState(), listener = dummyListener)
    }
}
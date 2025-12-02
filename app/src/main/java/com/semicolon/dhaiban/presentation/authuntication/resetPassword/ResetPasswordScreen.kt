package com.semicolon.dhaiban.presentation.authuntication.resetPassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.semicolon.dhaiban.designSystem.composables.DhaibanTextField
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.presentation.authuntication.composable.VerificationContainer
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class ResetPasswordScreen(private val otpCode: String, private val phoneOrEmail: String) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ResetPasswordScreenModel>(
            parameters = { parametersOf(phoneOrEmail, otpCode) }

        )
        val state: ResetPasswordUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ResetPasswordUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    ResetPasswordUiEffect.OnNavigateToHomeScreen -> navigator.replaceAll(
                        MainContainer()
                    )
                }
            }
        }

        ResetPasswordScreenContent(state = state, listener = screenModel)
    }


    @Composable
    fun ResetPasswordScreenContent(
        state: ResetPasswordUiState,
        listener: ResetPasswordInteractionListener
    ) {
        VerificationContainer(
            title = Theme.strings.resetPassword,
            onClickBackIcon = listener::onClickBackButton
        ) {
            DhaibanTextField(
                modifier = Modifier.padding(top = 8.dp, end = 16.dp, start = 16.dp),
                label = Theme.strings.newPasswordLabel,
                text = state.password,
                onValueChange = listener::onPasswordValueChanged,
                keyboardType = KeyboardType.Password,
                hint = Theme.strings.newPasswordHint,
                leadingIcon = R.drawable.icon_password,
                errorMessage = state.passwordMessage
            )

            DhaibanTextField(
                modifier = Modifier.padding(top = 8.dp, end = 16.dp, start = 16.dp),
                label = Theme.strings.reEnterNewPasswordLabel,
                text = state.reEnteredPassword,
                onValueChange = listener::onReEnteredPasswordValueChanged,
                keyboardType = KeyboardType.Password,
                hint = Theme.strings.newPasswordHint,
                leadingIcon = R.drawable.icon_password,
                errorMessage = state.reEnteredPasswordMessage
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.7f))

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = Theme.strings.reset,
                onClick = {
                    listener.onClickResetButton(
                        password = state.password,
                        reEnteredPassword = state.reEnteredPassword
                    )
                },
                isLoading = state.isLoading
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewResetPasswordScreen() {
    val dummyListener = object : ResetPasswordInteractionListener {
        override fun onClickResetButton(password: String, reEnteredPassword: String) {}
        override fun onClickBackButton() {}
        override fun onPasswordValueChanged(password: String) {}
        override fun onReEnteredPasswordValueChanged(reEnteredPassword: String) {}
    }
    DhaibanTheme {
        ResetPasswordScreen("", "").ResetPasswordScreenContent(
            state = ResetPasswordUiState(),
            listener = dummyListener
        )
    }
}
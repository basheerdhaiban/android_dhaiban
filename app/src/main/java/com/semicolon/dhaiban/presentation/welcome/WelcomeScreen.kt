package com.semicolon.dhaiban.presentation.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.authuntication.login.LoginScreen
import com.semicolon.dhaiban.presentation.authuntication.signUp.SignUpScreen
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<WelcomeScreenModel>()
        val state: WelcomeUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    WelcomeUiEffect.OnNavigateToHomeScreenScreen -> {
                        navigator.replaceAll(MainContainer())
                    }

                    WelcomeUiEffect.OnNavigateToLoginScreen -> navigator.push(LoginScreen)
                    WelcomeUiEffect.OnNavigateToSignUpScreen -> navigator.push(SignUpScreen())
                }
            }
        }

        WelcomeScreenContent(state = state, listener = screenModel)
    }


    @Composable
    fun WelcomeScreenContent(state: WelcomeUiState, listener: WelcomeScreenInteractionListener) {
        val coroutine = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.welcome_background),
                    contentScale = ContentScale.Crop
                )
                .safeDrawingPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.logIn.ifBlank { "Login " },
                    onClick = listener::onClickLogin
                )

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.signup.ifBlank { "Sign Up " },
                    borderWidth = 1.dp,
                    borderColor = Theme.colors.white,
                    colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.darkGray),
                    onClick = listener::onClickSignUp
                )

                Text(
                    modifier = Modifier.noRippleEffect {
                        coroutine.launch {
                            listener.onClickSkip()
                        }

                       },
                    text =Theme.strings.skip.lowercase().ifBlank { "Skip " } ,
                    style = Theme.typography.titleLarge,
                    color = Theme.colors.white,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewOnBoardingScreen() {
    val dummyListener = object : WelcomeScreenInteractionListener {
        override fun onClickLogin() {}
        override fun onClickSignUp() {}
        override suspend fun onClickSkip() {}
    }
    DhaibanTheme {
        WelcomeScreen().WelcomeScreenContent(state = WelcomeUiState(), listener = dummyListener)
    }
}
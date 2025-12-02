package com.semicolon.dhaiban.presentation.customer_service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.contactus.ContactUsScreenInteractionListener
import com.semicolon.dhaiban.presentation.contactus.ContactUsScreenModel
import com.semicolon.dhaiban.presentation.contactus.ContactUsUiEffect
import com.semicolon.dhaiban.presentation.contactus.ContactUsUiState
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.payment.PaymentScreenInteractionListener
import com.semicolon.dhaiban.presentation.payment.PaymentScreenUiState
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest

class CustomerServiceScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CustomerServiceScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val state: CustomerServiceUiState by screenModel.state.collectAsState()
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(Constants.CONTACT_US_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    CustomerServiceUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    CustomerServiceUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )

                    else -> {}
                }
            }
        }
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.colors.mediumBrown,

                    )
            }
        }
        else {
        CustomerServiceScreenContent(listener = screenModel,  state = state, model = screenModel)}
    }
}

@Composable
private fun CustomerServiceScreenContent(
    listener: CustomerServiceScreenInteractionListener,
    state: CustomerServiceUiState ,model: CustomerServiceScreenModel
) {
    val context = LocalContext.current
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    if (state.successSent){
        //TODO language
        Toast.makeText(context,"Your Message Sent Successfully",Toast.LENGTH_LONG).show()
        softwareKeyboardController?.hide()

        LaunchedEffect(key1 = state.successSent) {

            state.successSent=false
        }

    }
    Column {
        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage,
            title = Theme.strings.customerService,
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = Theme.strings.title, color = Theme.colors.black, style = Theme.typography.headline.copy(fontSize = 15.sp))

            OutlinedTextField(

                modifier = Modifier
                    .height(65.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                value = state.subject,
                onValueChange = {  listener.onSubjectChange(it)
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
                ),
                shape = RoundedCornerShape(16.dp),
                placeholder = {Text(text = "${Theme.strings.title_types_here}...",color = Theme.colors.greyishBrown, style = Theme.typography.caption.copy(fontSize=12.sp))}

                )

            Text(text = Theme.strings.message, color = Theme.colors.black, style = Theme.typography.headline.copy(fontSize = 15.sp))
            OutlinedTextField(

                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                value = state.content,
                onValueChange = {
                                listener.onContentChange(it)
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
                ),
                shape = RoundedCornerShape(16.dp),
                placeholder = {Text(text = "${Theme.strings.message}...",color = Theme.colors.greyishBrown, style = Theme.typography.caption.copy(fontSize=12.sp))})

Row (verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Absolute.Right, modifier = Modifier.fillMaxWidth()){
    Button(modifier = Modifier
        .padding(top = 2.dp)
        .width(150.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
                  model.contactWithCustomerService()


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

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyProfileScreenPreview() {
    val dummyListener = object : ContactUsScreenInteractionListener {
        override fun onClickUpButton() {
            TODO("Not yet implemented")
        }

        override fun onClickcustomerService() {
            TODO("Not yet implemented")
        }

        override fun onClickNotification() {
            TODO("Not yet implemented")
        }

    }
    DhaibanTheme {
//CustomerServiceScreenContent(
//            state = ContactUsUiState(), listener = dummyListener)
    }
}
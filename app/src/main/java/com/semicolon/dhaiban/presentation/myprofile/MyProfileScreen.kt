import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.DhaibanAlertDialog
import com.semicolon.dhaiban.designSystem.composables.DhaibanBottomDialog
import com.semicolon.dhaiban.designSystem.composables.DhaibanMoblieTextField
import com.semicolon.dhaiban.designSystem.composables.DhaibanTextField
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.authuntication.composable.OtpTextField
import com.semicolon.dhaiban.presentation.main.MainActivity
import com.semicolon.dhaiban.presentation.myprofile.MyProfileScreenInteractionListener
import com.semicolon.dhaiban.presentation.myprofile.MyProfileScreenModel
import com.semicolon.dhaiban.presentation.myprofile.MyProfileScreenState
import com.semicolon.dhaiban.presentation.myprofile.MyProfileUiEffect
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants.MY_PROFILE_SCREEN
import com.semicolon.dhaiban.presentation.utils.root
import com.semicolon.dhaiban.presentation.welcome.WelcomeScreen
import kotlinx.coroutines.flow.collectLatest

class MyProfileScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MyProfileScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: MyProfileScreenState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(MY_PROFILE_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    MyProfileUiEffect.OnNavigateBack -> if (navigator.canPop)  SafeNavigator.safePop(navigator, lifecycleOwner)
                    MyProfileUiEffect.OnDeleteAccountSuccess -> {
                        // Use a safer navigation approach for logout/delete account
                        (navigator.parent?.parent ?: navigator).replaceAll(
                            WelcomeScreen()
                        )
                    }

                    MyProfileUiEffect.OnNavigateToNotificationScreen -> SafeNavigator.safePush(
                        navigator,
                        NotificationScreen(),
                        lifecycleOwner = lifecycleOwner
                    )
                }
                if (state.isUpdated){
                    Log.d("isUpdatedChangeInfo","isUpdatedChangeInfo")
                }
            }
        }

        MyProfileScreenContent(context, state = state, listener = screenModel, model =screenModel )

        BackHandler {
            if (state.showPhoneBottomSheet or state.showPasswordBottomSheet or state.showOtpBottomSheet) {
                screenModel.onDismissAllBottomSheets()
            } else if (navigator.canPop) {
                SafeNavigator.safePop(navigator, lifecycleOwner)
            }
        }
    }
}

@Composable
private fun MyProfileScreenContent(
    context: Context,
    state: MyProfileScreenState,
    listener: MyProfileScreenInteractionListener,
    model: MyProfileScreenModel
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        selectedImageUri = uri
        Log.d("selectedImageUri",selectedImageUri.toString())
        listener.onClickChangeImage()

        val inputStream = selectedImageUri?.let { context.contentResolver.openInputStream(it) }
        val imageByteArray = inputStream?.readBytes()
        model.uploadProfileImage( "",imageByteArray ,context)
    }

Log.d("state.userImageUrl",state.userImageUrl)
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(context)
            .data(state.userImageUrl.ifEmpty { selectedImageUri })
            .placeholder(R.drawable.avater_img)
            .error(R.drawable.image_error_icon).build()
    )
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage,
            title = Theme.strings.myProfile,
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
        )
//        AnimatedVisibility(
//            visible = state.errorState.,
//            enter = slideInVertically { it },
//            exit = slideOutVertically { it },
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.BottomCenter
//            ) {
//                DhaibanAlertDialog(
//                    title = "Are you sure ?!",
//                    body = {
//                        Column(
//                            modifier = Modifier.padding(horizontal = 16.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.payment_success_icon),
//                                contentDescription = "Payment Success Image"
//                            )
//                        }
//                    },
//                    positiveText = "Exit",
//                    negativeText = "Stay",
//                    onDismiss = { screenModel.onDismissExit() },
//                    onPositive = {
//                        (context as? MainActivity)?.finish()
//                    }
//                )
//            }
//        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        painter = painter,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .noRippleEffect {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        painter = painterResource(id = R.drawable.edit_profile_icon),
                        contentDescription = "Edit Profile Icon",
                    )
                }
            }

            DhaibanTextField(
                label = Theme.strings.username,
                text = state.username,
                onValueChange = listener::onUsernameValueChanged,
                keyboardType = KeyboardType.Text,
                hint = Theme.strings.usernameHint,
                leadingIcon = R.drawable.icon_person,
                errorMessage = state.usernameErrorMessage
            )

            DhaibanTextField(
                label = Theme.strings.mobileNumber,
                text = state.phoneNumber,
                enabled = false,
                onValueChange = listener::onMobileNumberValueChanged,
                keyboardType = KeyboardType.Phone,
                hint = Theme.strings.phoneNumberHint,
                leadingIcon = R.drawable.icon_phone,
                errorMessage = state.phoneErrorMessage,
                trailingButton = true,
                onButtonClick = { listener.onClickChangePhone() }
            )

            DhaibanTextField(
                label = Theme.strings.email,
                text = state.email,
                onValueChange = listener::onEmailValueChanged,
                keyboardType = KeyboardType.Email,
                hint = Theme.strings.emailHint,
                leadingIcon = R.drawable.icon_email,
                errorMessage = state.emailErrorMessage
            )

            DhaibanTextField(
                label = Theme.strings.password,
                text = state.password,
                enabled = false,
                onValueChange = listener::onPasswordValueChanged,
                keyboardType = KeyboardType.Password,
                hint = "",
                leadingIcon = R.drawable.icon_password,
                trailingButton = true,
                errorMessage = state.passwordErrorMessage,
                onButtonClick = { listener.onClickChangePassword() }
            )


val context = LocalContext.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.save,
                    isLoading = state.isLoading,
                    onClick = {
                        listener.onClickSave(context)
                    }
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showPasswordBottomSheet,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            PasswordBottomSheet(
                currentPassword = state.currentPassword,
                newPassword = state.newPassword,
                reEnterPassword = state.reEnterNewPassword,
                errorMessage = state.passwordErrorMessage,
                loadingState = state.changePasswordLoading,
                onChangeCurrentPassword = { listener.onCurrentPasswordValueChanged(it) },
                onChangeNewPassword = { listener.onNewPasswordValueChanged(it) },
                onChangeReEnterPassword = { listener.onReEnterNewPasswordValueChanged(it) },
                onDismiss = { listener.onDismissPasswordBottomSheet() },
                onClickReset = { listener.onClickResetPassword() })
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showPhoneBottomSheet,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            ChangePhoneBottomSheet(
                phone = state.changePhoneNumber,
                state =state,
                password = state.changePhonePassword,
                errorMessage = state.changePhoneErrorMessage,
                loadingState = state.changePhoneLoading,
                onChangePhone = { listener.onChangePhoneNumberValueChanged(it) },
                onChangePassword = { listener.onChangePhonePasswordChanged(it) },
                onDismiss = { listener.onDismissPhoneBottomSheet() },
                onClickSend = { listener.onClickSendOtp() })
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showOtpBottomSheet,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            OtpBottomSheet(
                errorMessage = state.changePhoneErrorMessage,
                loadingState = state.changePhoneLoading,
                onDismiss = { listener.onDismissOtpBottomSheet() },
                onClickDone = {
                    listener.onSubmitChangePhone(it)
                }
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showDeleteAccountDialog,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            DhaibanBottomDialog(
                title = Theme.strings.areYouSureYouWantToDeleteAccount,
                positiveText = Theme.strings.delete,
                negativeText = Theme.strings.back,
                onDismiss = { listener.onDismissDeleteDialog() },
                onPositive = { listener.onConfirmDeleteAccount() }
            )
        }
    }
}


@Composable
private fun PasswordBottomSheet(
    currentPassword: String,
    newPassword: String,
    reEnterPassword: String,
    errorMessage: String,
    loadingState: Boolean,
    onChangeCurrentPassword: (String) -> Unit,
    onChangeNewPassword: (String) -> Unit,
    onChangeReEnterPassword: (String) -> Unit,
    onDismiss: () -> Unit,
    onClickReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        val startColor = Theme.colors.transparent
        val endColor = Theme.colors.black.copy(alpha = 0.2f)

        val backgroundColor = remember { Animatable(startColor) }

        LaunchedEffect(Unit) {
            backgroundColor.animateTo(endColor, animationSpec = tween(1000))
        }

        Box(
            Modifier
                .noRippleEffect { onDismiss() }
                .weight(2f)
                .fillMaxWidth()
                .background(backgroundColor.value)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .background(backgroundColor.value),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(top = 24.dp, end = 16.dp, start = 16.dp)
                    .safeDrawingPadding(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DhaibanTextField(
                    label = Theme.strings.currentPassword,
                    text = currentPassword,
                    onValueChange = { onChangeCurrentPassword(it) },
                    keyboardType = KeyboardType.Password,
                    hint = Theme.strings.currentPassword,
                    leadingIcon = R.drawable.icon_password,
                    errorMessage = ""
                )

                DhaibanTextField(
                    label = Theme.strings.newPasswordLabel,
                    text = newPassword,
                    onValueChange = { onChangeNewPassword(it) },
                    keyboardType = KeyboardType.Password,
                    hint = Theme.strings.newPassword,
                    leadingIcon = R.drawable.icon_password,
                    errorMessage = ""
                )

                DhaibanTextField(
                    label = Theme.strings.reEnterNewPasswordLabel,
                    text = reEnterPassword,
                    onValueChange = { onChangeReEnterPassword(it) },
                    keyboardType = KeyboardType.Password,
                    hint = Theme.strings.reEnterNewPassword,
                    leadingIcon = R.drawable.icon_password,
                    errorMessage = ""
                )
                AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                    Row {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = errorMessage,
                            style = Theme.typography.body,
                            color = Theme.colors.error
                        )
                    }
                }

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.reset,
                    isLoading = loadingState,
                    onClick = {
                        onClickReset()

                    }
                )
            }
        }
    }

}

@Composable
private fun ChangePhoneBottomSheet(
    phone: String,
    state: MyProfileScreenState,
    password: String,
    errorMessage: String,
    loadingState: Boolean,
    onChangePhone: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onDismiss: () -> Unit,
    onClickSend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        val startColor = Theme.colors.transparent
        val endColor = Theme.colors.black.copy(alpha = 0.2f)

        val backgroundColor = remember { Animatable(startColor) }

        LaunchedEffect(Unit) {
            backgroundColor.animateTo(endColor, animationSpec = tween(1000))
        }

        Box(
            Modifier
                .noRippleEffect { onDismiss() }
                .weight(2f)
                .fillMaxWidth()
                .background(backgroundColor.value)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(backgroundColor.value),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(top = 24.dp, end = 16.dp, start = 16.dp)
                    .safeDrawingPadding(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Change phone number",
                        style = Theme.typography.body,
                        color = Theme.colors.black
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .height(1.dp)
                        .background(Theme.colors.silver.copy(alpha = 0.5f))
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    DhaibanTextField(
//                        label = Theme.strings.contactNumber,
//                        text = phone,
//                        onValueChange = { onChangePhone(it) },
//                        keyboardType = KeyboardType.Phone,
//                        hint = Theme.strings.contactNumber,
//                        leadingIcon = R.drawable.icon_phone,
//                        errorMessage = ""
//                    )
                    DhaibanMoblieTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        label = Theme.strings.mobileNumber,
                        text = phone,
                        onValueChange = { onChangePhone(it) },
                        hint = Theme.strings.phoneNumberHint,
                        leadingIcon = R.drawable.icon_phone,
                        keyboardType = KeyboardType.Phone,
                        errorMessage = "" ,
                        onCountryChange = {

                            state.countryCode=it.countryPhoneNumberCode
                            Log.d("countryCode","${it.countryCode}")
                            Log.d("countryPhoneNumberCode","${it.countryPhoneNumberCode}")

                        }
                    )
                    DhaibanTextField(
                        label = Theme.strings.password,
                        text = password,
                        onValueChange = { onChangePassword(it) },
                        keyboardType = KeyboardType.Password,
                        hint = Theme.strings.password,
                        leadingIcon = R.drawable.icon_password,
                        errorMessage = ""
                    )

                    AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                        Row {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = errorMessage,
                                style = Theme.typography.body,
                                color = Theme.colors.error
                            )
                        }
                    }

                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = Theme.strings.send,
                        isLoading = loadingState,
                        onClick = {
                            onClickSend()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OtpBottomSheet(
    errorMessage: String,
    loadingState: Boolean,
    onDismiss: () -> Unit,
    onClickDone: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
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

        val startColor = Theme.colors.transparent
        val endColor = Theme.colors.black.copy(alpha = 0.2f)

        val backgroundColor = remember { Animatable(startColor) }

        LaunchedEffect(Unit) {
            backgroundColor.animateTo(endColor, animationSpec = tween(1000))
        }

        Box(
            Modifier
                .noRippleEffect { onDismiss() }
                .weight(2f)
                .fillMaxWidth()
                .background(backgroundColor.value)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(backgroundColor.value),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(top = 24.dp, end = 16.dp, start = 16.dp)
                    .safeDrawingPadding(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Change phone number",
                        style = Theme.typography.body,
                        color = Theme.colors.black
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .height(1.dp)
                        .background(Theme.colors.silver.copy(alpha = 0.5f))
                )
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "OTP",
                            style = Theme.typography.header.copy(fontWeight = FontWeight.W600),
                            color = Theme.colors.black
                        )
                    }
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
                            errorMessage = errorMessage
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
                            errorMessage = errorMessage
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
                            errorMessage = errorMessage
                        )

                        OtpTextField(
                            value = fourthOtpValue,
                            onValueChanged = {
                                fourthOtpValue = it
                            },
                            modifier = Modifier.focusRequester(focusRequester4),
                            errorMessage = errorMessage
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = Theme.strings.resendCode,
                            style = Theme.typography.caption,
                            color = Theme.colors.mediumBrown,
                            textDecoration = TextDecoration.Underline
                        )
                    }

                    AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                        Row {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = errorMessage,
                                style = Theme.typography.body,
                                color = Theme.colors.error
                            )
                        }
                    }

                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = Theme.strings.done,
                        isLoading = loadingState,
                        onClick = {
                            onClickDone(otpCode)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PasswordBottomSheetPreview() {
    DhaibanTheme {
        PasswordBottomSheet(
            "", "", "", "", false, {}, {}, {}, {}
        ) {}
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ChangePhoneBottomSheetPreview() {
//    DhaibanTheme {
//        ChangePhoneBottomSheet(
//            "", "", "", false, {}, {}, {}, {}
//        )
//    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun OtpBottomSheetPreview() {
    DhaibanTheme {
        OtpBottomSheet(errorMessage = "", loadingState = false, onDismiss = {}) {}
    }
}

@Preview(showBackground = true)
@Composable
fun MyProfileScreenPreview() {
    val dummyListener = object : MyProfileScreenInteractionListener {
        override fun onUsernameValueChanged(userName: String) {}
        override fun onMobileNumberValueChanged(mobileNumber: String) {}
        override fun onEmailValueChanged(email: String) {}
        override fun onPasswordValueChanged(password: String) {}
        override fun onClickUpButton() {}
        override fun onClickSave(context: Context) {}
        override fun onClickChangePassword() {}
        override fun onDismissPasswordBottomSheet() {}
        override fun onClickResetPassword() {}
        override fun onCurrentPasswordValueChanged(currentPassword: String) {}
        override fun onNewPasswordValueChanged(newPassword: String) {}
        override fun onReEnterNewPasswordValueChanged(reEnterNewPassword: String) {}
        override fun onChangePhoneNumberValueChanged(phone: String) {}
        override fun onChangePhonePasswordChanged(password: String) {}
        override fun onClickChangePhone() {}
        override fun onDismissPhoneBottomSheet() {}
        override fun onDismissAllBottomSheets() {}
        override fun onSubmitChangePhone(code: String) {}
        override fun onClickSendOtp() {}
        override fun onDismissOtpBottomSheet() {}
        override fun onClickDeleteAccount() {}
        override fun onDismissDeleteDialog() {}
        override fun onConfirmDeleteAccount() {}
        override fun onClickNotification() {}
        override fun onClickChangeImage() {}
    }
//    DhaibanTheme {
//        MyProfileScreenContent(
//            LocalContext.current,
//            state = MyProfileScreenState(),
//            listener = dummyListener , model = null
//        )
//    }
}
import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.chat.ChatScreenInteractioListener
import com.semicolon.dhaiban.presentation.chat.ChatScreenModel
import com.semicolon.dhaiban.presentation.chat.ChatScreenState
import com.semicolon.dhaiban.presentation.chat.ChatUiEffect
import com.semicolon.dhaiban.presentation.chat.ChatsState
import com.semicolon.dhaiban.presentation.chat.NotificationViewModel
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.trackorder.OldOrNewChat
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


class ChatScreen(

    private val inboxID: Int = 0,
    private val price: Double = 0.0,
    private val image: String = "",
    private val name: String = "",

    private val productOwner: String = "",
    private val productOwnerId: String = "",
    private val productOwnerImg: String = "",
    private val oldOrNewChat: OldOrNewChat = OldOrNewChat.OLD,
    private val productId: Int = 0,
    private val orderId: Int = 0,
    private val isComeFromNotification: Boolean = false,

    ) : Screen {
    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {
        Log.d("priceChatScreen", price.toString())
        val scope = rememberCoroutineScope()

        val screenModel =
            getScreenModel<ChatScreenModel>(parameters = {
                parametersOf(
                    oldOrNewChat,
                    productId,
                    orderId,
                    productOwnerId,
                    inboxID
                )
            })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: ChatScreenState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current
        Log.d("ChatScreendescriptionOfProduct", state.product.descriptionOfProduct.toString())
        screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
        if (oldOrNewChat == OldOrNewChat.NEW) {

            state.productImg = image
            state.titleOfProduct = name
            state.price = price
            state.imageSeller = productOwnerImg
            state.nameOfSeller = productOwner
        }
        Log.d("aaaaaaaa",isComeFromNotification.toString())

        val context = LocalContext.current
        LaunchedEffect(Unit) {

            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ChatUiEffect.OnNavigateBack ->
                        if (isComeFromNotification) {
                            SafeNavigator.safeReplaceAll(navigator, 
                                com.semicolon.dhaiban.presentation.home.container.MainContainer(), lifecycleOwner
                            )
                        } else if (navigator.canPop) {
                            SafeNavigator.safePop(navigator, lifecycleOwner)
                        }
                    ChatUiEffect.OnNavigateToNotificationScreen -> SafeNavigator.safePush(navigator, 
                        NotificationScreen(), lifecycleOwner
                    )
                }
            }
        }


        BackHandler {
            if (navigator.canPop)
                SafeNavigator.safePop(navigator, lifecycleOwner)
        }
        if (state.isLoadingForIntial) {
            LoadingContent()
        } else {

            ChatScreenContent(
                name,
                price,
                image,
                productOwner,
                inboxID ,
                productOwnerImg,
                model = screenModel,
                state,
                orderId = orderId,
                productId = productId,
                listener = screenModel,
                oldOrNewChat = oldOrNewChat, isComeFromNotification = isComeFromNotification
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        AppBarWithIcon(title = "Chat", onClickUpButton = { }, onClickNotification = {})
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(0.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
//                repeat(5) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
            }
//            }
//            Spacer(modifier = Modifier.height(20.dp))
//            Card(
//                modifier = Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp).shimmerEffect(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
////                colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
//                shape = RoundedCornerShape(8.dp)
//            ) {}
            Divider(thickness = 1.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .clip(
                        RoundedCornerShape(0.dp)
                    )
                    .shimmerEffect()
            )
            Divider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(30.dp))

            repeat(10) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (it % 2 == 0) Alignment.Start else Alignment.End
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(130.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }

            }

        }

    }

}


@Composable
fun ChatScreenContent(
    name: String,
    price: Double,
    image: String,
    productOwner: String,
    inboxID: Int,
    productOwnerImg: String,
    model: ChatScreenModel,
    state: ChatScreenState,
    oldOrNewChat: OldOrNewChat,
    productId: Int, orderId: Int,
    listener: ChatScreenInteractioListener, isComeFromNotification: Boolean,
    notificationViewModel: NotificationViewModel = koinViewModel()


) {
    val isNewNotification by notificationViewModel.isNewNotification.collectAsState()
    LaunchedEffect(isNewNotification) {
        if (isNewNotification) {
            model.getInboxById(inboxID)
                notificationViewModel.setNewNotification(false)

        }
    }
    var message by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    Log.d("is loading chat", state.isLoading.toString())
    val messages = model.messageState.collectAsLazyPagingItems()
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        topBar = {
            if (isComeFromNotification) {
                AppBarWithIcon(
                    title = "Chat",
                    modifier = Modifier.padding(top = 30.dp),
                    onClickUpButton = listener::onClickUpButton,
                    onClickNotification = listener::onClickNotification
                )
            } else
                AppBarWithIcon(
                    title = Theme.strings.chat,
                    onClickUpButton = listener::onClickUpButton,
                    onClickNotification = listener::onClickNotification
                )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()

                    .windowInsetsPadding(WindowInsets.systemBars)

                    .padding(bottom = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    // User info and product info rows
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors()
                                .copy(containerColor = Theme.colors.primary),
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape
                        ) {
                            Box(

                            ) {
                                CoilImage(
                                    modifier = Modifier
//                                        .border(1.dp, Theme.colors.toupe, CircleShape)
                                        .size(50.dp),
                                    url = state.imageSeller,
                                    scaleType = ContentScale.FillBounds,

                                    contentDescription = "Profile Avatar",
                                    shape = CircleShape
                                )
                            }
                        }

                        Text(
                            text = state.nameOfSeller,
                            style = Theme.typography.headline,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Divider(thickness = 1.dp)
                    Row(modifier = Modifier.padding(start = 12.dp, top = 6.dp, bottom = 6.dp)) {
                        Log.d("CoilImage", state.productImg)
                        CoilImage(
                            modifier = Modifier
                                .width(100.dp)
                                .height(70.dp),
                            url = state.productImg,
                            contentDescription = "Product Image",
                            shape = RoundedCornerShape(12.dp)
                        )
                        Column {
                            state.titleOfProduct?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(5.dp),
                                    style = Theme.typography.otherHeading
                                )
                            }
                            Row(modifier = Modifier.padding(start = 5.dp)) {
                                Text(
                                    text = String.format(
                                        "%.2f",
                                        state.price * state.currency.exchangeRate
                                    ),

                                    color = Theme.colors.primary,
                                    style = Theme.typography.caption
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = state.currency.symbol,
                                    color = Theme.colors.primary,
                                    style = Theme.typography.caption
                                )
                            }
                        }
                    }
                    Divider(thickness = 1.dp)

                    LazyColumn(
                        state = listState,
                        reverseLayout = true,
                        modifier = Modifier
                            .weight(1f)

                            .padding(
                                start = 15.dp,
                                top = 25.dp,
                                end = 15.dp,
                                bottom = 50.dp // Adjust for bottom padding if needed
                            )
                    ) {
                        items(messages.itemCount) { index ->
                            val message = messages[index]
                            if (message != null) {
                                chatRow(
                                    message = message,
                                    state = state,
                                    productOwnerImg = productOwnerImg
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(40.dp))

                }
                val modifier = if (layoutDirection == LayoutDirection.Rtl) {
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 20.dp, end = 10.dp, bottom = 20.dp)
                } else {
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 10.dp, end = 20.dp, bottom = 20.dp)
                }
                CustomTextField(
                    state = state,
                    text = message,
                    onValueChange = { model.onChatTextChange(it) },
                    send = {
                        val message = state.message

                        state.message = ""
                        model.sendMessage(message = message, inboxID = messages[0]?.inbox_id ?: 0)

                    },
                    modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                )
            }
        }
    }
}

@Composable
fun CommonIconButton(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(33.dp)
            .clip(CircleShape), contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = "",
            tint = Theme.colors.mediumBrown,
            modifier = Modifier.size(15.dp)
        )
    }

}

@Composable
fun CommonIconButtonDrawable(
    @DrawableRes icon: Int, modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(33.dp)
            .clip(CircleShape), contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = "",
            tint = Theme.colors.mediumBrown,
            modifier = Modifier.size(15.dp)
        )
    }

}

@Composable
fun chatRow(
    message: ChatsState,
    state: ChatScreenState, productOwnerImg: String
) {


    Column(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = if (message.userType != "customer") Alignment.Start else Alignment.End
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxHeight()
        ) {

            if (message.userType != "customer") {
                Card(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,


                    ) {
                    Box(

                    ) {
                        CoilImage(
                            modifier = Modifier
//                                        .border(1.dp, Theme.colors.toupe, CircleShape)
                                .size(50.dp),
                            url = productOwnerImg,
                            scaleType = ContentScale.FillBounds,

                            contentDescription = "Profile Avatar",
                            shape = CircleShape
                        )

                    }
                }
            }
            Box(
                modifier = Modifier

                    .clip(
                        RoundedCornerShape(
                            30f
                        )
                    )
                    .background(if (message.userType != "customer") Theme.colors.mediumBrown else Theme.colors.beigeTwo)
                    .padding(8.dp)
            ) {
                Column {
                    if (message.userType != "customer") {
                        Text(
                            text = message.message,
                            style = Theme.typography.title,
                            color = Theme.colors.white
                        )
                    } else {
                        Text(text = message.message, style = Theme.typography.title)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                }

                Text(
                    text = message.created_at,
                    color = if (message.userType != "customer") Theme.colors.white else Theme.colors.black,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.align(
                        Alignment.BottomEnd
                    )
                )


            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    state: ChatScreenState,
    text: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    send: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    val shape = if (layoutDirection == LayoutDirection.Rtl) {
        RoundedCornerShape(topStart = 0f, topEnd = 40f, bottomStart = 40f, bottomEnd = 40f)
    } else {
        RoundedCornerShape(topStart = 40f, topEnd = 0f, bottomStart = 40f, bottomEnd = 40f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .imePadding(),
        shape = shape,
        border = BorderStroke(1.dp, Theme.colors.beigeTwo),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.beigeTwo,
            contentColor = Theme.colors.transparent,
            disabledContainerColor = Theme.colors.transparent,
            disabledContentColor = Theme.colors.transparent
        )
    ) {
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Theme.colors.beigeTwo,
                unfocusedContainerColor = Theme.colors.beigeTwo
            ),
            modifier = Modifier.fillMaxWidth(),
            value = state.message,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Type Message Here",
                    style = Theme.typography.title,
                    textAlign = TextAlign.Center
                )
            },
            trailingIcon = {

                CommonIconButtonDrawable(
                    R.drawable.inchaticon,
                    modifier = Modifier.clickable { send() }
                )
            }
        )
    }
}
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun OtpBottomSheetPreview() {
//    DhaibanTheme {
//        ChatScreenContent("",1.0,"","")
//    }
//}
@Preview(showBackground = true)
@Composable
fun ShimmerChatEffectPreview() {
    DhaibanTheme {
        LoadingContent()
    }
}

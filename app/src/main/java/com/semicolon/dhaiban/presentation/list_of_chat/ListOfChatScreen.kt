package com.semicolon.dhaiban.presentation.list_of_chat


import ChatScreen
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.cart.composables.ButtonForEmptyState
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.trackorder.OldOrNewChat
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class ListOfChatScreen : Screen {
    @Composable
    override fun Content() {

        val screenModel =
            getScreenModel<ListOfChatScreenModel>(parameters = { parametersOf(OldOrNewChat.OLD) })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val appScreenState by appScreenModel.state.collectAsState()
        val state: ListOfChatScreenState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(Constants.MY_PROFILE_SCREEN)

            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ListOfChatUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    is ListOfChatUiEffect.OnNavigateToChatScreen -> {
                        navigator.push(
                            ChatScreen(
                                inboxID = effect.inboxId,
                                oldOrNewChat = OldOrNewChat.OLD,
                                price = effect.price,
                                image = effect.image,
                                productOwnerImg = effect.photoOfVendor,
                                name = effect.nameOfProduct
                            )
                        )
                    }

                    ListOfChatUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }


        BackHandler {

        }
        DhaibanTheme(
            stringResources = appScreenState.stringRes,
            layoutDirection = appScreenState.layoutDirection
        ) {
        ListofChatScreen(model = screenModel, state = state, listener = screenModel) }
    }
}


@Composable
fun ListofChatScreen(
    state: ListOfChatScreenState,
    listener: ListOfChatScreenInteractioListener,
    model: ListOfChatScreenModel
) {


    val messages = model.inboxStatestate.collectAsLazyPagingItems()
    Log.d("messages", messages.itemCount.toString())
    if (messages.itemCount != 0) {
        Column(modifier = Modifier.fillMaxHeight()) {
            AppBarWithIcon(
                countOFunReadNotifcation = state.countOfUnreadMessage,
                title = Theme.strings.chatRoom,
                onClickUpButton = listener::onClickUpButton,
                onClickNotification = listener::onClickNotification
            )


            LazyColumn(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 15.dp,
                        top = 14.dp,
                        end = 15.dp,
                        bottom = 50.dp
                    )

            ) {

                items(messages.itemCount) { index ->
                    val message = messages[index]
                    Log.d("ChatListItem", message?.message ?: "")
                    Log.d("ChatListItem", message?.created_at ?: "")

                    if (message != null) {
                        Log.d("ChatListItem", message.message)
                        Log.d("ChatListItem", message.created_at)
                        ChatListItem(message, model)
                    }
                }

            }
        }

    } else if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.colors.mediumBrown,

                )
        }
    } else {
        val scope = rememberCoroutineScope()
        scope.launch {
            delay(5000)
        }
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.inbox_empty_state),
                        contentDescription = "No Data Icon",
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        //"Inbox empty"
                        text =  Theme.strings.inboxEmpty,
                        style = Theme.typography.headline,

                        )

                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    //"You have no messages yet"
                    Text(

                        text = Theme.strings.You_have_no_messages_yet,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight(
                                400
                            )
                        ),
                        textAlign = TextAlign.Start,
                        color = Theme.colors.dimGray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    //Be the first to start a conversation"
                    Text(
                        text = Theme.strings.Be_the_first_to_start_a_conversation,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight(
                                400
                            )
                        ),
                        textAlign = TextAlign.Start,
                        color = Theme.colors.dimGray
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonForEmptyState("Go back") {
                        listener.onClickUpButton()
                    }


                }
            }
        }
    }
}

@Composable
fun ChatListItem(chatItem: InboxState, model: ListOfChatScreenModel) {
    Log.d("ListOfChatScreenname", chatItem.name)

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp)
                .fillMaxHeight()
                .fillMaxWidth()
                .clickable {
                    model.onClickInboxItem(
                        chatItem.inbox_id,
                        price = chatItem.price,
                        photoOfVendor = chatItem.photo,
                        nameOfProduct = chatItem.nameOfProuduct,
                        image = chatItem.imageOfProduct
                    )
                }
        ) {
            Card(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,


                ) {
                Box(

                ) {
                    CoilImage(
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp),
                        url = chatItem.photo,
                        contentDescription = "Product Image",
                        shape = RoundedCornerShape(12.dp)
                    )

                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = chatItem.name,
                    style = Theme.typography.header.copy(fontSize = 16.sp),
                    color = Theme.colors.black
                )
                val first50Chars = chatItem.message.substring(0, minOf(chatItem.message.length, 50))

                Text(
                    modifier = Modifier
                        .height(20.dp)
                        .width(100.dp),
                    overflow = TextOverflow.Ellipsis,
                    text = first50Chars,
                    style = Theme.typography.caption,
                    color = Theme.colors.greyishBrown
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = chatItem.created_at,
                style = Theme.typography.caption,
                color = Theme.colors.greyishBrown
            )


        }
        Divider(thickness = 1.dp)

    }

}


package com.semicolon.dhaiban.presentation.notification

import ChatScreen
import TrackOrderScreen
import TrackRefundScreen
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.orders.OrderUiState
import com.semicolon.dhaiban.presentation.utils.Constants.NOTIFICATION_SCREEN
import kotlinx.coroutines.flow.collectLatest

class NotificationScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<NotificationScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: NotificationScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(NOTIFICATION_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    NotificationScreenUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    is NotificationScreenUiEffect.OnNavigateToTrackOrder -> {
                        navigator.push(
                            TrackOrderScreen(OrderType.CURRENT, effect.id)
                        )


                    }

                    is NotificationScreenUiEffect.OnNavigateTorRefund -> {
                        navigator.push(
                            TrackRefundScreen(refundID = effect.refundID)
                        )


                    }
                    is NotificationScreenUiEffect.OnNavigateToMessage -> {
                        navigator.push(
                            ChatScreen(inboxID =  effect.id)
                        )


                    }
                    else -> {}
                }
            }
        }

        NotificationScreenContent(state = state, listener = screenModel)
    }

}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreenContent(
    state: NotificationScreenUiState, listener: NotificationScreenModel?
) {
    val userNotifications = listener!!.userNotificationsState.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        AppBarWithIcon(
            title = Theme.strings.notifcation,
            onClickUpButton = listener::onClickUpButton, withNotification = false
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {

                if (state.isLoading) {
                    item {
                        LoadingContent()
                    }
                } else {


                    items(userNotifications.itemCount) { index ->
                        val notification = userNotifications[index]

                        notification?.let {
                            Log.d("Notifcationscreen", it.type.toString())
//                            if (it.notificationData.deliveryStatus != DeliveryStatus.NOT_FOUND || it.notificationData.refundStatus != RefundStatus.NOT_FOUND) {
                            if (it.type == NotificationType.ORDER) {
                                Log.d("userNotificationsreadAt", it.readAt)

                                NotificationItem(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    type = NotificationType.ORDER,
                                    notification = it, isUnread = it.readAt.isEmpty()
                                ) {
                                    userNotifications[index]?.let { it1 ->
                                        userNotifications[index]?.notificationData?.let { it2 ->
                                            userNotifications[index]?.notificationData?.let { it2 -> OrderUiState(id = it2.orderId) }
                                                ?.let { it3 ->
                                                    listener.onClickTrackOrder(

                                                        it3,
                                                        OrderType.PREVIOUS,
                                                        it2.orderId,
                                                        notificationID = it1.id
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                            else if (it.type == NotificationType.MESSAGE) {

                                NotificationItem(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    notification = it ,type = NotificationType.PAYMENT, isUnread = it.readAt.isEmpty()
                                ) {

                                    userNotifications[index]?.let {
                                        userNotifications[index]?.notificationData?.let { it1 -> OrderUiState(id = it1.orderId) }
                                            ?.let { it2 ->
                                                listener?.onClickMessage(
                                                    it2,
                                                    OrderType.PREVIOUS,
                                                    it.notificationData.inboxID,
                                                    notificationID = it.id
                                                )
                                            }
                                    }
                                }
                            }

                            else if (it.type == NotificationType.PAYMENT) {

                                Log.d("userNotificationsreadAt", it.readAt)

                                NotificationItem(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    type = NotificationType.ORDER,
                                    notification = it, isUnread = it.readAt.isEmpty()
                                ) {
                                    userNotifications[index]?.let { it1 ->
                                        userNotifications[index]?.notificationData?.let { it2 ->
                                            userNotifications[index]?.notificationData?.let { it2 -> OrderUiState(id = it2.orderId) }
                                                ?.let { it3 ->
                                                    listener.onClickTrackOrder(

                                                        it3,
                                                        OrderType.PREVIOUS,
                                                        it2.orderId,
                                                        notificationID = it1.id
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                            else {

                                NotificationItem(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    type = NotificationType.ORDER,
                                    notification = it, isUnread = it.readAt.isEmpty()
                                ) {
                                    userNotifications[index]?.let { it1 ->
                                        userNotifications[index]?.notificationData?.let { it2 ->
                                            userNotifications[index]?.notificationData?.let { it2 -> OrderUiState(id = it2.orderId) }
                                                ?.let { it3 ->
                                                    listener.onClickRefund(

                                                        it3,
                                                        OrderType.PREVIOUS,

                                                        notificationID = it1.id,
                                                        it.notificationData.refundId
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                        }

//                        }
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun NotificationScreenContent(
//    state: NotificationScreenUiState, listener: NotificationScreenModel?
//) {
//    val userNotifications = listener!!.userNotificationsState.collectAsLazyPagingItems()
//    val userUnReadNotifications = listener.userUnReadNotificationsState.collectAsLazyPagingItems()
//    if(userNotifications.itemCount!=0){
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .safeDrawingPadding(),
//    ) {
//        AppBarWithIcon(
//            title = "Notifications",
//            onClickUpButton = listener::onClickUpButton, withNotification = false
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//
//            CombinedNotifications(userNotifications, userUnReadNotifications, state, listener)
//
//
//        }
//    }}
//    else{
//        Box(
//            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
//        ) {
//            Column {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.notification),
//                        contentDescription = "No Data Icon",
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "No Notification",
//                        style = Theme.typography.headline,
//
//                        )
//
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "You have no notifications yet",
//                        style = Theme.typography.body.copy(
//                            fontWeight = FontWeight(
//                                400
//                            )
//                        ),
//                        textAlign = TextAlign.Start,
//                        color = Theme.colors.dimGray
//                    )
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "please come back later",
//                        style = Theme.typography.body.copy(
//                            fontWeight = FontWeight(
//                                400
//                            )
//                        ),
//                        textAlign = TextAlign.Start,
//                        color = Theme.colors.dimGray
//                    )
//                }
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    ButtonForEmptyState("Go back"){
//                        listener.onClickUpButton()
//                    }
//
//
//                }
//            }
//        }
//
//    }
//}

@Composable
fun CombinedNotifications(
    userNotifications: LazyPagingItems<NotificationUiState>,
    userUnReadNotifications: LazyPagingItems<NotificationUiState>,
    state: NotificationScreenUiState,
    listener: NotificationScreenModel?
) {
    val allNotifications = remember { mutableListOf<NotificationUiState>() }

    // Get the unread notifications
    val unreadNotifications = userUnReadNotifications.itemSnapshotList.items

    // Get the read notifications and exclude the ones that are already in the unread list
    val readNotifications =
        userNotifications.itemSnapshotList.items.filterNot { it in unreadNotifications }

    // Combine the lists: unread notifications first
    allNotifications.clear()
    allNotifications.addAll(unreadNotifications)
    allNotifications.addAll(readNotifications)




        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(userNotifications.itemCount) { index ->
                val notification = allNotifications[index]



                if (userNotifications[index]?.type == NotificationType.ORDER) {

                    userNotifications[index]?.let {
                        Log.d("userNotifications", it.readAt)
                        NotificationItem(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            type = NotificationType.ORDER,
                            notification = it,
                            isUnread = it.readAt!=null ,
                        ) {
                            userNotifications[index]?.let { it1 ->
                                userNotifications[index]?.notificationData?.let { it2 ->
                                    userNotifications[index]?.notificationData?.let { it2 -> OrderUiState(id = it2.orderId) }
                                        ?.let { it3 ->
                                            listener?.onClickTrackOrder(

                                                it3,
                                                OrderType.PREVIOUS,
                                                it2.orderId,
                                                notificationID = it1.id
                                            )
                                        }
                                }
                            }
                        }
                    }
                } else {

                    NotificationItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        notification = allNotifications[index],
                        type = NotificationType.PAYMENT,
                        isUnread = allNotifications[index].readAt.isEmpty()
                    ) {

                        userNotifications[index]?.let {
                            userNotifications[index]?.notificationData?.let { it1 -> OrderUiState(id = it1.orderId) }
                                ?.let { it2 ->
                                    listener?.onClickRefund(
                                        it2,
                                        OrderType.PREVIOUS,
                                        notificationID = it.id,
                                        it.notificationData.refundId
                                    )
                                }
                        }
                    }
                }
            }
        }


}

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    notification: NotificationUiState,
    type: NotificationType,
    isUnread: Boolean,
    onClick: () -> Unit,


    ) {

    Row(modifier = modifier
        .fillMaxWidth()
        .shadow(elevation = 2.dp, shape = RoundedCornerShape(4.dp))
        .background(if (!isUnread) Theme.colors.white else Theme.colors.whiteTwo)
        .clickable { onClick() }
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        if (type == NotificationType.ORDER) {
            Image(


                painter = painterResource(id = R.drawable.deliveys),
                contentDescription = "Notification Icon"
            )


        } else if (notification.type==NotificationType.MESSAGE) {


                Image(


                    painter = painterResource(id = R.drawable.message),
                    contentDescription = "Notification Icon"
                )





        }
        else if (notification.type==NotificationType.PAYMENT) {


            Image(


                painter = painterResource(id = R.drawable.payment),
                contentDescription = "Notification Icon"
            )





        }
        else if (notification.type==NotificationType.REFUND) {


            Image(


                painter = painterResource(id = R.drawable.refund_icon),
                contentDescription = "Notification Icon"
            )





        }

        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Log.d("typeofNotification",type.toString())
//            Log.d("typeofNotification",notification.type.toString())
            Text(
                text = if (notification.type == NotificationType.MESSAGE) notification.notificationData.messageFrom else notification.notificationData.title,
                style = Theme.typography.body,
                color = Theme.colors.black
            )

            Text(
                modifier = Modifier.height(25.dp).fillMaxWidth().padding(bottom = 10.dp),
                overflow= TextOverflow.Visible,
                text = notification.notificationData.message,
                style = Theme.typography.caption,
                color = Theme.colors.dimGray
            )
//            Text(
//                text = notification.notificationData.orderId.toString(),
//                color = Theme.colors.dimGray,
//                style = Theme.typography.caption,
//
//                )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationItemPreview() {
    DhaibanTheme {
        NotificationItem(

            type = NotificationType.ORDER, isUnread = false, notification = NotificationUiState(
                notificationData = NotificationDataUiState(
                    title = "Bla Bla",
                    message = "Bla bla bla bla bla bla",
                )
            )
        ) {}
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingContent() {
    Column {
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationScreenContentPreview() {
    DhaibanTheme {
        NotificationScreenContent(
            state = NotificationScreenUiState(), listener = null
        )
    }
}
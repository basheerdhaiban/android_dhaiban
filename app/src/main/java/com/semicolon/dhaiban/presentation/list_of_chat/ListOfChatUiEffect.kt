package com.semicolon.dhaiban.presentation.list_of_chat

import com.semicolon.dhaiban.presentation.home.HomeScreenUiEffect


sealed interface  ListOfChatUiEffect {
    data object OnNavigateToNotificationScreen : ListOfChatUiEffect
    data object OnNavigateBack : ListOfChatUiEffect
    data class OnNavigateToChatScreen(val inboxId: Int, val image:String,val nameOfProduct:String, val price:Double ,val photoOfVendor :String) : ListOfChatUiEffect

}
package com.semicolon.dhaiban.presentation.list_of_chat

interface ListOfChatScreenInteractioListener {
    fun onChatTextChange(text: String)
    fun onClickUpButton()
    fun onClickInboxItem(inboxId: Int, image:String,nameOfProduct:String, price:Int ,photoOfVendor :String)
    fun onClickNotification()
}
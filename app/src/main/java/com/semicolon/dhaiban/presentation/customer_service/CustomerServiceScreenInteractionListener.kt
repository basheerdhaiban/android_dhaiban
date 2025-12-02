package com.semicolon.dhaiban.presentation.customer_service

interface CustomerServiceScreenInteractionListener {
    fun onClickUpButton()
    fun onClickNotification()
    fun onContentChange(text:String)
    fun onSubjectChange (subject :String)
}
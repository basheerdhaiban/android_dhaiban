package com.semicolon.dhaiban.presentation.profile

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface ProfileScreenInteractionListener : BaseInteractionListener {
    fun onClickFavoritesButton()
    fun onClickOrdersButton()
    fun onClickLogout()
    fun onClickLogin()
    fun onDismissDialog()
    fun onConfirmLogout()
    fun onMyProfileClick()
    fun onLanguageClick()
    fun onClickDeleteAccount()
    fun onDismissDeleteDialog()
    fun onConfirmDeleteAccount()
    fun tryToConnect()

    fun onCountryClick()
    fun onCurrencyClick()
    fun onDismissBottomSheet()
    fun onQueryValueChanged(query: String)
    fun onItemSelected(item: String, id: Int)
    fun onClickConfirm()
    fun onClickCancel()
    fun onClickAddress()
    fun onClickChatRoom()
    fun onClickSearch()
    fun onClickContactUs()
    fun onClickFaq()
    fun onClickRefund()
    fun onClickWallet()
    fun onDismissLoginDialog()
    fun onClickNotification()
}
package com.semicolon.dhaiban.presentation.configuration

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface ConfigurationScreenInteractionListener : BaseInteractionListener {
    fun onClickCountryButton()
    fun onClickLanguageButton()
    fun onClickCurrencyButton()
    fun onClickConfirmButton()
    fun onClickSkipButton()
    fun onDismissBottomSheet()
    fun onQueryValueChanged(query: String)
    fun onItemSelected(item: String, id: Int)
    fun onClickConfirm()
    fun onClickCancel()
}
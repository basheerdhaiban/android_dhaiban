package com.semicolon.dhaiban.presentation.address

interface AddressScreenInteractionListener {
    fun onClickUpButton()
    fun onClickAddAddress()
    fun onClickAddress(address: AddressUiState)
    fun onClickDefault(address: AddressUiState)
    fun onClickChange(address: AddressUiState)
    fun onDeleteAddress(addressId: Int)
    fun onCLickNotification()
}
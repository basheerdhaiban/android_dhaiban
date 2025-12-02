package com.semicolon.dhaiban.presentation.address

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.AddressModel
import com.semicolon.domain.usecase.ManageAddressUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update

class AddressScreenModel(private val manageAddressUseCase: ManageAddressUseCase) :
    BaseScreenModel<AddressScreenUiState, AddressScreenUiEffect>(
        AddressScreenUiState()
    ), AddressScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getAddresses()
    }

    private fun getAddresses() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageAddressUseCase.getAddresses() },
            onSuccess = ::onGetAddressesSuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun editAddress(addressModel: AddressModel) {
        tryToExecute(
            function = { manageAddressUseCase.editAddress(addressModel) },
            onSuccess = ::onEditAddressSuccess,
            onError = { Log.e("Edit error", it.message.toString()) }
        )
    }

    private fun deleteAddress(addressId: Int) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = {
                delay(500)
                manageAddressUseCase.deleteAddress(addressId)
            },
            onSuccess = ::onEditAddressSuccess,
            onError = { Log.e("Delete error", it.message.toString()) }
        )
    }

    private fun onGetAddressesSuccess(addresses: List<AddressModel>) {
        _state.update { uiState ->
            uiState.copy(isLoading = false, addresses = addresses.map { it.toAddressUiState() })
        }
    }

    private fun onEditAddressSuccess(addresses: List<AddressModel>) {
        _state.update { uiState ->
            uiState.copy(
                isLoading = false,
                addresses = addresses.map { it.toAddressUiState() })
        }
    }

    override fun onClickAddress(address: AddressUiState) {
        sendNewEffect(AddressScreenUiEffect.OnNavigateToCart(address))
    }

    override fun onClickUpButton() {
        sendNewEffect(AddressScreenUiEffect.OnNavigateBackToCart)

    }

    override fun onClickAddAddress() {
        sendNewEffect(AddressScreenUiEffect.OnNavigateToMap)
    }

    override fun onClickDefault(address: AddressUiState) {
        editAddress(address.toEditAddressModel())
    }

    override fun onClickChange(address: AddressUiState) {
        sendNewEffect(AddressScreenUiEffect.OnNavigateToChangeAddress(address))
    }

    override fun onDeleteAddress(addressId: Int) {
        deleteAddress(addressId)
    }

    override fun onCLickNotification() {
        sendNewEffect(AddressScreenUiEffect.OnNavigateToNotificationScreen)
    }
}
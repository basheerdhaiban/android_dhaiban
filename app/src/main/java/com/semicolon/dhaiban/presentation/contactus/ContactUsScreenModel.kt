package com.semicolon.dhaiban.presentation.contactus

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.orders.OrderUiState
import com.semicolon.dhaiban.presentation.orders.toCurrentOrderUiState
import com.semicolon.domain.entity.SocialModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.ManageOrderUseCase
import com.semicolon.domain.usecase.ManageSocialUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class ContactUsScreenModel (private val manageSocialUseCases: ManageSocialUseCases,private val  manageNotificationUseCase: ManageNotificationUseCase) :
    BaseScreenModel<ContactUsUiState, ContactUsUiEffect>(ContactUsUiState()),
    ContactUsScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
init {
    getSocialContact ()
}
    override fun onClickUpButton() {
        sendNewEffect(ContactUsUiEffect.OnNavigateBack)
    }

    override fun onClickcustomerService() {
        sendNewEffect(ContactUsUiEffect.OnNavigateToCustomerScreen)    }

    override fun onClickNotification() {
        sendNewEffect(ContactUsUiEffect.OnNavigateToNotificationScreen)
    }
    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState -> screenUiState.copy(
                        isLoading = false,
                        countOfUnreadMessage= it.notifications
                    )  }
                }

            },
            onError ={}
        )
    }
    private fun getSocialContact() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageSocialUseCases.getSocialContact() },
            onSuccess = ::onGetSocial,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetSocial(social: SocialModel) {

        _state.update { uiState ->
            uiState.copy(

                isLoading = false ,
                social = social.toContactUsUiState()
            )
        }
        Log.d("onGetSocial",state.value.social.facebook)

    }

}
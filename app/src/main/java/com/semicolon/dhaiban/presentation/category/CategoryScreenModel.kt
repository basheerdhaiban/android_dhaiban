package com.semicolon.dhaiban.presentation.category

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.Category
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageCategoryUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class CategoryScreenModel(
    private val manageCategoryUseCase: ManageCategoryUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase,
) :
    BaseScreenModel<CategoryScreenUiState, CategoryScreenUiEffect>(
        CategoryScreenUiState()
    ), CategoryScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getCategories()
        getUserData()
        getCountOfUnreadNotification()
    }
    fun getUserData() {
        tryToExecute(
            function = {
                UserDataUiState(
                    username = localConfigurationUseCase.getUserName(),
                    isAuthenticated = localConfigurationUseCase.getUserToken().isNotEmpty()
                )
            },
            onSuccess = ::onGetUserDataSuccess,
            onError = {}
        )

    }




    private fun onGetUserDataSuccess(userData: UserDataUiState) {
        _state.update {
            it.copy(
                userData = UserDataUiState(
                    userData.username,
                    userData.isAuthenticated
                )
            )
        }
    }

    fun getCategories() {
        _state.update { it.copy(isLoading = true, errorMessage = "") }
        tryToExecute(
            function = { manageCategoryUseCase.getCategories() },
            onSuccess = ::onGetCategoriesSuccess,
            onError = ::onError
        )
    }

    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true, errorMessage = "") }
        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState ->
                        screenUiState.copy(
                            isLoading = false,
                            countOfUnreadMessage = it.notifications
                        )
                    }
                }

            },
            onError = ::onErrornotification
        )
    }

    private fun onGetCategoriesSuccess(categories: List<Category>) {
        _state.update { screenUiState ->
            screenUiState.copy(
                isLoading = false,
                categories = categories.map { it.toEntity() })
        }
    }

    private fun onError(exception: Exception) {
        Log.d("onError", exception.message.toString())
        _state.update { it.copy(isLoading = false, errorMessage = "Network Error") }
    }

    private fun onErrornotification(exception: Exception) {
        Log.d("onErrornotification", exception.message.toString())
        _state.update { it.copy(isLoading = false) }
    }

    override fun onClickBackButton() {
        sendNewEffect(CategoryScreenUiEffect.OnNavigateToHomeScreen)
    }

    override fun onClickCategory(id: Int, title: String) {
        sendNewEffect(CategoryScreenUiEffect.OnNavigateToSubCategoryScreen(id, title))
    }

    override fun onClickNotification() {
        sendNewEffect(CategoryScreenUiEffect.OnNavigateToNotificationScreen)
    }
}
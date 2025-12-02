package com.semicolon.dhaiban.presentation.search

import android.util.Log
import androidx.paging.PagingData
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.home.ProductUiState
import com.semicolon.dhaiban.presentation.home.toProductUiState
import com.semicolon.domain.entity.Product
import com.semicolon.domain.usecase.ManageSearchUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SearchScreenModel(private val manageSearchUseCase: ManageSearchUseCase) :
    BaseScreenModel<SearchScreenUiState, SearchScreenUiEffect>(
        SearchScreenUiState()
    ), SearchScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _query = MutableStateFlow(_state.value.queryValue)
    private val query = _query.asStateFlow()

    private val _newProductsState: MutableStateFlow<PagingData<ProductUiState>> =
        MutableStateFlow(PagingData.empty())
    val newProductsState = _newProductsState.asStateFlow()

//    private val _isSearchByImage: MutableStateFlow<Boolean> = MutableStateFlow(value = true)
//    val isSearchByImage = _isSearchByImage.asStateFlow()


    private val _searchProducts: MutableStateFlow<List<ProductUiState>>  = MutableStateFlow(_state.value.searchList)
    @OptIn(FlowPreview::class)
    val searchProducts: StateFlow<List<ProductUiState>> = query
        .debounce(1000L)
        .combine(_searchProducts) { text, products ->
            if (_query.value.isNotEmpty())
                searchForProduct(_query.value, null)
//            else if (!_isSearchByImage.value)
//                _searchProducts.value = emptyList()

            if (text.isBlank()) {
                products
            } else {
                products.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
//        .onEach { _state.update { it.copy(isLoading = false) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _searchProducts.value)


    override fun searchForProduct(query: String?, imageByteArray: ByteArray?) {
//        _isSearchByImage.value = false
        _searchProducts.value = emptyList()
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageSearchUseCase.searchForProduct(query, imageByteArray) },
            onSuccess = ::onSearchForProductSuccess,
            onError = { exception ->
                Log.e("Search Error", exception.message.toString())
                _state.update { it.copy(isLoading = false) }
            }
        )
        _state.update { it.copy(isLoading = false) }
    }

    override fun searchForProductByImage(byteArray: ByteArray?) {
//        _isSearchByImage.value = true
        _query.value = ""
        _searchProducts.value = emptyList()
        _state.update { it.copy(isLoading = true, queryValue = "") }
        tryToExecute(
            function = { manageSearchUseCase.searchForProductByImage(byteArray) },
            onSuccess = ::onSearchForProductSuccess,
            onError = { exception ->
                Log.e("Image Search Error", exception.message.toString())
                _state.update { it.copy(isLoading = false) }
            }
        )
    }
    private fun onSearchForProductSuccess(searchResult: List<Product>) {
        Log.e("onSearchForProductSuccess", "searchResult: ${searchResult.size}")
        val productList = searchResult.map { it.toProductUiState() }
        _state.update { uiState ->
            uiState.copy(
                searchList = productList,
                isLoading = false
            )
        }
        _searchProducts.value = _state.value.searchList
//        _isSearchByImage.value = false
    }

    override fun onClickBackButton() {
        sendNewEffect(SearchScreenUiEffect.OnNavigateBack)
    }

    override fun onChangeSearchValue(queryText: String) {
        _state.update { it.copy(queryValue = queryText) }
        _query.value = queryText
    }

    override fun onClickProduct(productId: Int) {
        _state.update { it.copy(searchList = emptyList(), queryValue = "") }
        _searchProducts.value = emptyList()
        _query.value = ""
        sendNewEffect(SearchScreenUiEffect.OnNavigateToProductDetailsScreen(productId))
    }

    override fun onClickNotification() {
        sendNewEffect(SearchScreenUiEffect.OnNavigateToNotificationScreen)
    }
}
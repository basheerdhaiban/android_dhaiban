package com.semicolon.dhaiban.presentation.home

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.Brand
import com.semicolon.domain.entity.Category
import com.semicolon.domain.entity.Product
import com.semicolon.domain.entity.ProductType
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageBrandUseCase
import com.semicolon.domain.usecase.ManageCategoryUseCase
import com.semicolon.domain.usecase.ManageFavoritesUseCase
import com.semicolon.domain.usecase.ManageProductUseCase
import com.semicolon.domain.usecase.ManageSearchUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val manageCategoryUseCase: ManageCategoryUseCase,
    private val manageBrandUseCase: ManageBrandUseCase,
    private val manageProductUseCase: ManageProductUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase,
    private val manageSearchUseCase: ManageSearchUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase
) : BaseScreenModel<HomeScreenUiState, HomeScreenUiEffect>(HomeScreenUiState()),
    HomeScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _productsState: MutableStateFlow<PagingData<ProductUiState>> =
        MutableStateFlow(PagingData.empty())
    val productsState = _productsState.asStateFlow()

    private val _newProductsState: MutableStateFlow<PagingData<ProductUiState>> =
        MutableStateFlow(PagingData.empty())
    val newProductsState = _newProductsState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)

    private val _query = MutableStateFlow(_state.value.queryValue)
    private val query = _query.asStateFlow()

    private val _searchProducts = MutableStateFlow(_state.value.searchList)
    @OptIn(FlowPreview::class)
    val searchProducts = query
        .debounce(1000L)
        .combine(_searchProducts) { text, products ->
            if (_query.value.isNotEmpty())
                searchForProduct(_query.value, null)
            else
                _searchProducts.value = emptyList()
            if (text.isBlank()) {
                products
            } else {
                products.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _searchProducts.value)

    init {
//        getCountry()
//        refreshHome()
    }

     fun getMainCategories() {
        _state.update { it.copy(isLoading = true, refreshState = true) }
        tryToExecute(
            function = { manageCategoryUseCase.getCategories() },
            onSuccess = ::onGetMainCategoriesSuccess,
            onError = ::onError
        )
    }

    private fun onError(exception: Exception) {
//        _state.update { it.copy(isLoading = false, errorMessage = "Network Error") }
//        _state.update { it.copy(isLoading = false, errorMessage = exception.message.toString()) }
    }

     fun getFeaturedBrands() {
        _state.update { it.copy(isLoading = true, errorMessage = "", refreshState = true) }
        tryToExecute(
            function = { manageBrandUseCase.getFeaturedBrands() },
            onSuccess = ::onGetBrandsSuccess,
            onError = ::onError
        )
    }
     fun getCountry() {
        tryToExecute(
            function = { localConfigurationUseCase.getCountryId() },
            onSuccess = ::onGetUserCountrySuccess,
            onError = {Log.d("aaasssaaa","$it")}
        )
    }
    private fun onGetUserCountrySuccess(country: Int) {
        _state.update { it.copy(country = country.toString()) }
    }

     fun getSliderItems() {
        _state.update { it.copy(isLoading = true, errorMessage = "", refreshState = true) }
        tryToExecute(
            function = { manageProductUseCase.getSliderItems() },
            onSuccess = ::onGetSliderItemsSuccess,
            onError = ::onError
        )
    }

    fun getSaleProducts() {
        _state.update { it.copy(isLoading = true, errorMessage = "", refreshState = true) }
        tryToExecute(
            function = { manageProductUseCase.getSaleProducts() },
            onSuccess = ::onGetSaleProductsSuccess,
            onError = ::onError
        )
    }

    fun getNewProducts() {
        _state.update { it.copy(isLoading = true, errorMessage = "", refreshState = true) }
        tryToExecute(
            function = { manageProductUseCase.getNewProducts(state.value.userData.country.toString()) },
            onSuccess = ::onGetNewProductsSuccess,
            onError = ::onError
        )
    }

    fun getNewProducts1() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    isGroupProductsLoading = true,
                    errorMessage = ""
                )
            }
            try {
                manageProductUseCase.getNewProducts1(state.value.userData.country.toString())
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _newProductsState.value = pagingData.map { it.toProductUiState() }
                        _state.update { it.copy(isLoading = false) }
                        delay(3000)
                        _state.update { it.copy(isGroupProductsLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, isGroupProductsLoading = false) }
            }
        }
    }

    fun getProductTypes() {
        _state.update { it.copy(isLoading = true, errorMessage = "", refreshState = true) }
        tryToExecute(
            function = { manageProductUseCase.getProductTypes() },
            onSuccess = ::onGetProductTypesSuccess,
            onError = ::onError
        )
    }

    private fun getGroupProducts(offerGroupId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    isGroupProductsLoading = true,
                    errorMessage = ""
                )
            }
            try {
                manageProductUseCase.getProducts(offerGroupId)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _productsState.value = pagingData.map { it.toProductUiState() }
                        _state.update { it.copy(isLoading = false) }
                        delay(3000)
                        _state.update { it.copy(isGroupProductsLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, isGroupProductsLoading = false) }
            }
        }
    }

    private fun changeFavoriteState(productState: Boolean, productId: Int, isSaleProduct: Boolean) {
        tryToExecute(
            function = {
                if (productState) {
                    Triple(
                        isSaleProduct,
                        productId,
                        manageFavoritesUseCase.removeProductFromFavorite(productId)
                    )
                } else {
                    Triple(
                        isSaleProduct,
                        productId,
                        manageFavoritesUseCase.addProductToFavorite(productId)
                    )
                }
            },
            onSuccess = ::onChangeFavoriteStateSuccess,
            onError = {}
        )
    }

    fun getUserData() {
        tryToExecute(
            function = {
                UserDataUiState(
                    username = localConfigurationUseCase.getUserName(),
                    country = localConfigurationUseCase.getCountryId(),
                    imageUrl = localConfigurationUseCase.getImageUrl(),
                    isAuthenticated = localConfigurationUseCase.getUserToken().isNotEmpty()
                )
            },
            onSuccess = ::onGetUserDataSuccess,
            onError = {}
        )
    }

    fun updateCurrencySymbol(symbol: String) {
        _state.update { it.copy(currencySymbol = symbol) }
    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(homeCurrencyUiState = appCurrencyUiState.toEntity()) }
    }

    private fun searchForProduct(query: String?, imageByteArray: ByteArray?) {
        tryToExecute(
            function = { manageSearchUseCase.searchForProduct(query, imageByteArray) },
            onSuccess = ::onSearchForProductSuccess,
            onError = { Log.e("Search Error", it.message.toString()) }
        )
    }

    override fun onClickNotificationIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickBackToHome() {
        TODO("Not yet implemented")
    }

    override fun onClickTrackOrder(orderId: Int) {
        TODO("Not yet implemented")
    }

    fun updateHomeData() {
        getSaleProducts()
        getNewProducts()
        getUserData()
    }

    private fun refreshHome() {
        getSaleProducts()
        getNewProducts()
        getUserData()
        getFeaturedBrands()
        getMainCategories()
        getSliderItems()
        getProductTypes()
        getNewProducts1()
    }

    override fun onClickCategory(categoryId: Int, categoryTitle: String) {
        sendNewEffect(HomeScreenUiEffect.OnNavigateToSubCategoryScreen(categoryId, categoryTitle))
    }
    fun onPullToRefreshTrigger() {
        _state.update { it.copy(_isRefreshing=true) }
        viewModelScope.launch {
            refreshHome()
            _state.update { it.copy(_isRefreshing=false) }
        }
    }

    override fun onClickProductFavorite(
        productId: Int,
        isSaleProduct: Boolean,
        isFavorite: Boolean
    ) {
        changeFavoriteState(isFavorite, productId, isSaleProduct)
    }

    override fun onClickViewAllCategories() {
        sendNewEffect(HomeScreenUiEffect.OnNavigateToCategoriesScreen)
    }

    override fun onClickViewAllBrands() {
        sendNewEffect(HomeScreenUiEffect.OnNavigateToBrandsScreen)
    }

    override fun onClickViewAllSaleProducts() {
        sendNewEffect(HomeScreenUiEffect.OnNavigateToFlashSaleScreen)
    }

    override fun onClickNewProducts() {
        _state.update { it.copy(selectedList = it.newProducts) }
    }

    override fun onClickProductType(id: Int) {
        _productsState.value = PagingData.empty()
        getGroupProducts(id)
    }

    override fun onClickProduct(productId: Int) {
        _state.update { it.copy(searchList = emptyList(), queryValue = "") }
        _searchProducts.value = emptyList()
        _query.value = ""
        sendNewEffect(HomeScreenUiEffect.OnNavigateToProductDetailsScreen(productId))
    }

    override fun onClickBrand(brandId: Int, brandTitle: String) {
        sendNewEffect(HomeScreenUiEffect.OnNavigateToBrandProductsScreen(brandId, brandTitle))
    }

    override fun onChangeSearchValue(queryText: String) {
        _state.update { it.copy(queryValue = queryText) }
        _query.value = queryText
    }

    override fun cancelSearch() {
        _query.value = ""
        _searchProducts.value = emptyList()
        _state.update { it.copy(queryValue = "") }
    }

    override fun onClickProfile() {
        sendNewEffect(HomeScreenUiEffect.OnNavigateToProfileScreen)
    }

    override fun onClickSearch() {
        sendNewEffect(HomeScreenUiEffect.OnNavigateToSearchScreen)
    }

    override fun onRefresh() {
        _state.update { it.copy(refreshState = true) }
        refreshHome()
    }

    override fun onClickBack() {
        _state.update { it.copy(showExitDialog = true) }
    }

    override fun onDismissExit() {
        _state.update { it.copy(showExitDialog = false) }
    }

    private fun onGetMainCategoriesSuccess(categories: List<Category>) {
        _state.update { homeScreenUiState ->
            homeScreenUiState.copy(
                categories = categories.map { it.toEntity() },
                isLoading = false,
                refreshState = false
            )
        }
    }

    private fun onGetBrandsSuccess(brands: List<Brand>) {
        _state.update { homeScreenUiState ->
            homeScreenUiState.copy(
                brands = brands.map { it.toEntity() },
                isLoading = false,
                refreshState = false
            )
        }
    }

    private fun onGetSliderItemsSuccess(sliderItems: List<SliderItem>) {
        _state.update { homeScreenUiState ->
            homeScreenUiState.copy(
                sliderItems = sliderItems.map { it.toEntity() },
                isLoading = false,
                refreshState = false
            )
        }
    }

    private fun onGetSaleProductsSuccess(saleProducts: List<Product>) {
        _state.update { homeScreenUiState ->
            homeScreenUiState.copy(
                saleProducts = saleProducts.map { it.toEntity() },
                isLoading = false,
                refreshState = false
            )
        }
    }

    private fun onGetNewProductsSuccess(newProducts: List<Product>) {
        sendNewEffect(HomeScreenUiEffect.OnNotifyDataUpdated)
        Log.d("onGetNewProductsSuccess",newProducts.get(0).title.toString())
        Log.d("onGetNewProductsSuccess",newProducts.get(0).unitPrice.toString())
        _state.update { homeScreenUiState ->
            homeScreenUiState.copy(
                newProducts = newProducts.map { it.toProductUiState() },
                selectedList = newProducts.map { it.toProductUiState() },
                isLoading = false,
                refreshState = false
            )
        }
    }

    private fun onGetProductTypesSuccess(productsTypes: List<ProductType>) {
        _state.update { homeScreenUiState ->
            homeScreenUiState.copy(
                productTypes = productsTypes.map { it.toProductTypeUiState() },
                isLoading = false,
                refreshState = false
            )
        }
    }

    private fun onChangeFavoriteStateSuccess(result: Triple<Boolean, Int, String>) {
        val isSaleProduct = result.first
        val productId = result.second
        val message = result.third
        if (message.isNotEmpty()) {
            if (isSaleProduct) {
                _state.update { homeScreenUiState ->
                    homeScreenUiState.copy(
                        saleProducts = homeScreenUiState.saleProducts.map {
                            if (it.id == productId)
                                it.copy(isFavourite = it.isFavourite.not())
                            else
                                it
                        }
                    )
                }
            } else {
                _state.update { homeScreenUiState ->
                    homeScreenUiState.copy(selectedList = homeScreenUiState.selectedList.map {
                        if (it.id == productId)
                            it.copy(isFavourite = it.isFavourite.not())
                        else
                            it
                    })
                }
            }
            viewModelScope.launch {
                _productsState.emit(_productsState.value.map {
                    it.copy(
                        isFavourite = if (it.id == productId) it.isFavourite.not()
                        else it.isFavourite
                    )
                })
            }

        }
    }

    private fun onGetUserDataSuccess(userData: UserDataUiState) {
        _state.update {
            it.copy(
                userData = UserDataUiState(
                    userData.username.ifEmpty { "Guest" },
                    userData.imageUrl,
                    userData.isAuthenticated
                ),
                refreshState = false
            )
        }
    }

    private fun onSearchForProductSuccess(searchResult: List<Product>) {
        _state.update { uiState -> uiState.copy(searchList = searchResult.map { it.toProductUiState() }) }
        _searchProducts.value = _state.value.searchList
    }
}
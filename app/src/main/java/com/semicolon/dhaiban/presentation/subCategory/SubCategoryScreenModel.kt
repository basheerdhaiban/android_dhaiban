package com.semicolon.dhaiban.presentation.subCategory

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.FilterDataModel
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageFavoritesUseCase
import com.semicolon.domain.usecase.ManageProductUseCase
import com.semicolon.domain.usecase.ManageSubCategoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubCategoryScreenModel(
    private val id: Int,
    private val manageSubCategoryUseCase: ManageSubCategoryUseCase,
    private val manageProductUseCase: ManageProductUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase
) : BaseScreenModel<SubCategoryScreenUiState, SubCategoryScreenUiEffect>(
    SubCategoryScreenUiState()
), SubCategoryScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _subCategoryProductsState: MutableStateFlow<PagingData<SubCategoryProductUiState>> =
        MutableStateFlow(PagingData.empty())
    val subCategoryProductsState =
        _subCategoryProductsState.asStateFlow()

    init {
        getUserToken()
        getCountry()
        getSubCategories(id)
        getSliderByCategoryId(id)
        getCategoryProducts(id)
        getFilterData(id)
    }

    private fun getUserToken() {
        tryToExecute(
            function = { localConfigurationUseCase.getUserToken() },
            onSuccess = ::onGetUserTokenSuccess,
            onError = {}
        )
    }

    private fun getCountry() {
        tryToExecute(
            function = { localConfigurationUseCase.getCountryId() },
            onSuccess = ::onGetUserCountrySuccess,
            onError = {}
        )
    }


    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toSubCategoryCurrencyUiState()) }
    }

    private fun getSubCategories(categoryId: Int) {

        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = {
                manageSubCategoryUseCase.getSubCategories(categoryId).map { it.toEntity() }
            },
            onSuccess = ::onGetSubCategoriesSuccess,
            onError = ::onError
        )

    }

    private fun getCategoryProducts(
        categoryId: Int,
        sortById: Int? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        colorId: Int? = null,
        attributes: Map<Int, List<String>>? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, isProductsLoading = true) }
            try {
                manageProductUseCase.getProducts(
                    categoryId = categoryId,
                    sortBy = sortById,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    color = colorId,
                    attributes = attributes
                ).cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _subCategoryProductsState.value =
                            pagingData.map { it.toSubCategoryProductModel() }
                        _state.update { it.copy(isLoading = false) }
                        delay(5000)
                        _state.update { it.copy(isProductsLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, isProductsLoading = false,) }
            }
        }
    }

    private fun getSubCategoryProducts(subCategoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, isProductsLoading = true) }
            try {
                manageProductUseCase.getProducts(categoryId = subCategoryId)
//                manageProductUseCase.getProducts(categoryId = 0)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _subCategoryProductsState.value =
                            pagingData.map { it.toSubCategoryProductModel() }
                        _state.update { it.copy(isLoading = false) }
                        delay(5000)
                        _state.update { it.copy(isProductsLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, isProductsLoading = false) }
            }
        }
    }

    private fun getFilterData(categoryId: Int) {
        tryToExecute(
            function = { manageSubCategoryUseCase.getFilterData(categoryId, state.value.country) },
            onSuccess = ::onGetFilterDataSuccess,
            onError = {
                Log.e("error", it.message.toString())
            }
        )
    }

    private fun getSliderByCategoryId(categoryId: Int) {
        tryToExecute(
            function = { manageSubCategoryUseCase.getCategorySlider(categoryId) },
            onSuccess = ::onGetCategorySliderSuccess,
            onError = {
                Log.d("onGetCategorySliderErroraaaaaaaaaaaaa",it.message.toString())
            }
        )
    }

    private fun changeFavoriteState(productState: Boolean, productId: Int) {
        tryToExecute(
            function = {
                if (productState) {
                    productId to manageFavoritesUseCase.removeProductFromFavorite(productId)
                } else {
                    productId to manageFavoritesUseCase.addProductToFavorite(productId)
                }
            },
            onSuccess = ::onChangeFavoriteStateSuccess,
            onError = {}
        )
    }

    private fun onGetUserTokenSuccess(token: String) {
        _state.update { it.copy(isAuthorized = token.isNotEmpty()) }
    }
    private fun onGetUserCountrySuccess(country: Int) {
        _state.update { it.copy(country = country.toString()) }
    }

    private fun onGetSubCategoriesSuccess(subCategories: List<SubCategoryUiState>) {
        _state.update { it.copy(subCategories = subCategories) }
    }

    private fun onGetCategorySliderSuccess(sliderItems: List<SliderItem>) {
        Log.d("onGetCategorySliderSuccess",sliderItems.get(0).photoUrl.toString())
        _state.update { uiState ->
            uiState.copy(sliderItems = sliderItems.map { it.toSliderUiState() })
        }
    }

    private fun onGetFilterDataSuccess(filterData: FilterDataModel) {
        _state.update { uiState ->

            Log.d("onGetFilterDataSuccesssss","minPrice ${filterData.minPrice} maxPrice ${filterData.maxPrice} ")
            uiState.copy(
                colors = filterData.colors.map { it.toColorUiState() },
                attributes = filterData.attributes.map { it.toEntity() },
                minPrice = filterData.minPrice,
                maxPrice = filterData.maxPrice
            )
        }
    }

    private fun onChangeFavoriteStateSuccess(result: Pair<Int, String>) {
        val productId = result.first
        val message = result.second
        if (message.isNotEmpty()) {
            viewModelScope.launch {
                _subCategoryProductsState.emit(_subCategoryProductsState.value.map {
                    it.copy(
                        isFavourite = if (it.id == productId) it.isFavourite.not()
                        else it.isFavourite
                    )
                })
            }
        }
    }

    private fun onError(exception: Exception) {
        _state.update { it.copy(isLoading = false) }
    }

    fun hideFilter() {
        _state.update { it.copy(isFilterSelected = false) }
    }

    override fun onClickBackButton() {
        sendNewEffect(SubCategoryScreenUiEffect.OnNavigateToCategories)
    }

    override fun onClickSubCategory(id: Int) {
        if (id == this.id) {
            _state.update { it.copy(selectedSubCategory = -1) }
        } else {
            _state.update { it.copy(selectedSubCategory = id) }
        }
        _subCategoryProductsState.value = PagingData.empty()
        getSubCategoryProducts(id)
    }

    override fun onClickFilter(visibility: Boolean) {
        _state.update { it.copy(isFilterSelected = !visibility) }
    }

    override fun onClickProduct(productId: Int) {
        sendNewEffect(SubCategoryScreenUiEffect.OnNavigateToProductDetailsScreen(productId))
    }

    override fun onClickProductFavorite(productId: Int, isFavorite: Boolean) {
        changeFavoriteState(isFavorite, productId)
    }

    override fun onClickApplyFilter(
        categorySelected: Boolean,
        categoryId: Int,
        sortById: Int,
        colorId: Int,
        minPrice: Int,
        maxPrice: Int,
        attributes: Map<Int, List<String>>
    ) {
        Log.d("changeRate",(minPrice*_state.value.currency.exchangeRate).toString())
        _subCategoryProductsState.value = PagingData.empty()
        _state.update {
            it.copy(
                isFilterSelected = false,
                selectedSubCategory =
                if (state.value.subCategories.isNotEmpty() && categorySelected)
                    state.value.subCategories.first().id
                else
                    0
            )
        }

        getCategoryProducts(
            categoryId = categoryId,
            sortById = sortById,
            minPrice = (minPrice/_state.value.currency.exchangeRate).toInt(),
            maxPrice = (maxPrice/_state.value.currency.exchangeRate).toInt(),
            colorId = if (colorId == 0) null else colorId,
            attributes = attributes
        )
    }

    override fun onClickSearch() {
        sendNewEffect(SubCategoryScreenUiEffect.OnNavigateToSearchScreen)
    }

    override fun onClickNotification() {
        sendNewEffect(SubCategoryScreenUiEffect.OnNavigateToNotificationScreen)
    }

    override fun updateFirstItemState(visible: Boolean) {
        _state.update { it.copy(firstItemIsVisible = visible) }
    }
}
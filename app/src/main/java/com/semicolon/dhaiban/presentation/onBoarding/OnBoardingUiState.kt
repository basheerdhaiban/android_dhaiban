package com.semicolon.dhaiban.presentation.onBoarding

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.presentation.base.ErrorState

@Immutable
data class OnBoardingUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorState: ErrorState = ErrorState.BadRequest,
    val onBoardingData: List<OnBoardingContentUiState> = listOf(
        OnBoardingContentUiState(
            image = R.drawable.on_boarding_first,
            description = "We provide high\n" +
                    "quality products just \n" +
                    "for you ❤"
        ),
        OnBoardingContentUiState(
            image = R.drawable.on_boarding_second,
            description = "We provide high\n" +
                    "quality products just \n" +
                    "for you ❤"
        ),
        OnBoardingContentUiState(
            image = R.drawable.on_boarding_third,
            description = "We provide high\n" +
                    "quality products just \n" +
                    "for you ❤"
        ),
    ),
)

@Immutable
data class OnBoardingContentUiState(@DrawableRes val image: Int, val description: String)
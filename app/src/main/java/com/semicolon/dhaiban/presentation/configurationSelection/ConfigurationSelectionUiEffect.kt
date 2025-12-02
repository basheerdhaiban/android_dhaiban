package com.semicolon.dhaiban.presentation.configurationSelection

sealed interface ConfigurationSelectionUiEffect {
    data object OnDismissBottomSheet : ConfigurationSelectionUiEffect
}
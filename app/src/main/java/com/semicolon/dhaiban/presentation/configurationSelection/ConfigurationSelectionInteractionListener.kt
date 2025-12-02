package com.semicolon.dhaiban.presentation.configurationSelection

interface ConfigurationSelectionInteractionListener {

    fun onSelectItem(item: String)

    fun onQueryChanged(query: String)
}
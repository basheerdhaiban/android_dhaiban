package com.semicolon.dhaiban.presentation.home.container

import CartScreen
import CategoryScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.presentation.home.HomeScreen
import com.semicolon.dhaiban.presentation.profile.ProfileScreen

object HomeTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Theme.strings.home
            return remember { TabOptions(index = 0u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = HomeScreen()) {
            SlideTransition(it)
        }
    }

}

object CategoriesTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Theme.strings.categories
            return remember { TabOptions(index = 1u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = CategoryScreen()) {
            SlideTransition(it)
        }
    }
}

object CartTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Theme.strings.cart
            return remember { TabOptions(index = 3u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = CartScreen()) {
            SlideTransition(it)
        }
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Theme.strings.profile
            return remember { TabOptions(index = 4u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = ProfileScreen()) {
            SlideTransition(it)
        }
    }
}

@Composable
fun rememberTabsContainer(): List<TabContainer> {
    return remember {
        listOf(
            TabContainer(
                HomeTab,
                selectedIcon = R.drawable.icon_home_selected,
                unSelectedIcon = R.drawable.icon_home_unselected
            ), TabContainer(
                CategoriesTab,
                selectedIcon = R.drawable.icon_category_selected,
                unSelectedIcon = R.drawable.icon_category_unselected
            ), TabContainer(
                CartTab,
                selectedIcon = R.drawable.icon_cart_selected,
                unSelectedIcon = R.drawable.icon_cart_unselected
            ), TabContainer(
                ProfileTab,
                selectedIcon = R.drawable.icon_profile_selected,
                unSelectedIcon = R.drawable.icon_profile_unselected
            )
        )
    }
}

data class TabContainer(
    val tab: Tab,
    val selectedIcon: Int,
    val unSelectedIcon: Int,
)

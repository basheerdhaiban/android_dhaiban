import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.DhaibanSnackBar
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.category.CategoryScreenInteractionListener
import com.semicolon.dhaiban.presentation.category.CategoryScreenModel
import com.semicolon.dhaiban.presentation.category.CategoryScreenUiEffect
import com.semicolon.dhaiban.presentation.category.CategoryScreenUiState
import com.semicolon.dhaiban.presentation.category.CategoryUiState
import com.semicolon.dhaiban.presentation.home.container.HomeTab
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants.CATEGORY_SCREEN
import kotlinx.coroutines.flow.collectLatest

class CategoryScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CategoryScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: CategoryScreenUiState by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(Unit) {
            screenModel.getCategories()

                screenModel.getUserData()
            appScreenModel.setCurrentScreen(CATEGORY_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    CategoryScreenUiEffect.OnNavigateToHomeScreen -> navigator.parent?.let { parentNav -> SafeNavigator.safeReplace(
                        parentNav, HomeTab,
                        lifecycleOwner = lifecycleOwner
                    ) }

                    is CategoryScreenUiEffect.OnNavigateToSubCategoryScreen -> SafeNavigator.safePush(
                        navigator, SubCategoryScreen(effect.id, effect.title),
                        lifecycleOwner = lifecycleOwner
                    )

                    CategoryScreenUiEffect.OnNavigateToNotificationScreen ->
                        SafeNavigator.safePush(
                            navigator = navigator,
                            screen = NotificationScreen(),
                            lifecycleOwner = lifecycleOwner
                        )
                }
            }
        }

        CategoryScreenContent(state = state, listener = screenModel)
        Log.d("DhaibanSnackBar", state.errorMessage)
//        DhaibanSnackBar(
//            icon = R.drawable.icon_language,
//            iconBackgroundColor = Color.Transparent,
//            iconTint = Theme.colors.white,
//            isVisible = state.errorMessage.isNotEmpty(),
//            modifier = Modifier.padding( bottom = 30.dp)
//                .fillMaxWidth()
//                .safeDrawingPadding()
//        ) {
//            Text(
//                text = state.errorMessage,
//                style = Theme.typography.body,
//                color = Theme.colors.white
//            )
//        }

        BackHandler {
            navigator.parent?.let { parentNav -> SafeNavigator.safeReplace(
                parentNav, HomeTab,
                lifecycleOwner = lifecycleOwner
            ) }
        }
    }


}

@Composable
private fun CategoryScreenContent(
    state: CategoryScreenUiState,
    listener: CategoryScreenInteractionListener
) {
    DhaibanSnackBar(
        icon = R.drawable.icon_language,
        iconBackgroundColor = Color.Transparent,
        iconTint = Theme.colors.white,
        isVisible = state.errorMessage.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
    ) {
        Text(
            text = state.errorMessage, style = Theme.typography.body, color = Theme.colors.white
        )
    }
    Log.d("CategoryScreenContent",state.countOfUnreadMessage.toString())
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage,
            title = Theme.strings.categories,
            onClickUpButton = listener::onClickBackButton,
            onClickNotification = listener::onClickNotification,
            withNotification = state.userData.isAuthenticated
        )

Log.d("CategoryScreenContent",state.userData.isAuthenticated.toString())
        AnimatedContent(targetState = state.isLoading, label = "") { isLoading ->
            if (isLoading) {
                LoadingContent()
            } else {
                if (state.categories.isNotEmpty()) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxHeight().background(Color.White)
                            .padding(horizontal = 12.dp),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(state.categories) { category ->
                            CategoryItem(
                                id = category.id,
                                title = category.title,
                                image = category.image,
                                backColor = Theme.colors.black60,
                                textColor = Theme.colors.white,
                                onClick = { id ->
                                    listener.onClickCategory(id, category.title)
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image_error_icon),
                            contentDescription = ""
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun CategoryItem(
    id: Int,
    title: String,
    image: String,
    backColor: Color,
    textColor: Color,
    onClick: (Int) -> Unit
) {
    Box(
        Modifier
            .size(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(start = 6.dp, end = 6.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(id) },
        contentAlignment = Alignment.Center
    ) {
        CoilImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            url = image,
            contentDescription = ""
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxSize()
                .background(backColor)
        ) {}
        Text(
            text = title,
            style = Theme.typography.titleLarge,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingContent() {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(10) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CategoryScreenPreview() {
    val dummyListener = object : CategoryScreenInteractionListener {
        override fun onClickBackButton() {}
        override fun onClickCategory(id: Int, title: String) {}
        override fun onClickNotification() {}
    }
    val categories = listOf(
        CategoryUiState(
            1,
            "Fashion fcbdfndfn",
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
        ),
        CategoryUiState(
            2,
            "Beauty",
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
        ),
        CategoryUiState(
            3,
            "Bla",
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
        ),
        CategoryUiState(
            4,
            "Fashion fcbdfndfndhdfgj",
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
        ),
        CategoryUiState(
            5,
            "Beauty",
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
        ),
        CategoryUiState(
            6,
            "Bla",
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
        )
    )
    DhaibanTheme {
        CategoryScreenContent(
            state = CategoryScreenUiState(categories = categories),
            listener = dummyListener
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    DhaibanTheme {
        CategoryItem(
            id = 1,
            title = "Bla",
            image = "",
            backColor = Theme.colors.black60,
            textColor = Theme.colors.white,
            onClick = {})
    }
}

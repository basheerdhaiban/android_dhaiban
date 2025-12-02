package com.semicolon.dhaiban.presentation.profile.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.DhaibanSearchBar
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.profile.ProfileScreenUiState

@Composable
fun ProfileHeaderComponent(
    modifier: Modifier = Modifier,
    username: String,
    image: String,
    state: ProfileScreenUiState,
    onSearchChanged: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    countOFunReadNotifcation:Int,
    onClick: () -> Unit,


    ) {
    val searchInput by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.colors.mediumBrown)
            .padding(top = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CoilImage(
                        modifier = Modifier
                            .border(1.dp, Theme.colors.toupe, CircleShape)
                            .size(80.dp),
                        url = image,
                        errorImage = R.drawable.avater_img,
                        placeholderImage = R.drawable.avater_img,
                        contentDescription = "Profile Avatar",
                        shape = CircleShape
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Theme.strings.welcome + " " + username,
                        color = Theme.colors.white,
                        style = Theme.typography.body
                    )
                }
                if (state.userData.isAuthenticated) {
                    BadgedBox(
                    badge = {
                        if (countOFunReadNotifcation != 0) {
                            Icon(painter = painterResource(id = R.drawable.dot),contentDescription = null , tint = Theme.colors.white , modifier = Modifier.align(
                                Alignment.TopStart))
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier.noRippleEffect { onNotificationsClick() },
                        painter =painterResource(id = R.drawable.notifcation),
                        tint = Theme.colors.white,

                        contentDescription = null
                    )
                } }
            }
            Spacer(modifier = Modifier.height(16.dp))
            DhaibanSearchBar(
                enabled = false,
                value = searchInput, hint = Theme.strings.search, onQueryChanged = {
                    onSearchChanged(it)
                },
                modifier = modifier
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .noRippleEffect { onClick() },
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Preview
@Composable
fun ProfileHeaderComponentPreview() {
    DhaibanTheme {
        ProfileHeaderComponent(
            username = "Mohamed Adel",
            image = "",
            countOFunReadNotifcation = 5,
            onSearchChanged = {},
            state = ProfileScreenUiState(),
            onNotificationsClick = {}) {}
    }
}
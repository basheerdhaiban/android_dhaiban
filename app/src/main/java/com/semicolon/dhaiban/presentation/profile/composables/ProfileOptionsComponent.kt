package com.semicolon.dhaiban.presentation.profile.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.profile.ProfileOptionUiState

@Composable
fun ProfileOptionsComponent(
    modifier: Modifier = Modifier,
    title: String,
    options: List<ProfileOptionUiState>,
    onMyProfileClick: () -> Unit = {},
    onAddressClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onCountryClick: () -> Unit = {},
    onCurrencyClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    onFaqClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onShippingClick: () -> Unit = {},
    onChatRoomClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            color = Theme.colors.black,
            style = Theme.typography.body.copy(fontWeight = FontWeight.SemiBold)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Theme.colors.white)
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { profileOption ->
                    ProfileOptionsItem(
                        profileOption.name,
                        profileOption.icon
                    ) {
                        when (profileOption.icon) {
                            R.drawable.profile_icon -> {
                                onMyProfileClick()
                            }
                            R.drawable.charroom -> {
                                onChatRoomClick()
                            }

                            R.drawable.address_icon -> {
                                onAddressClick()
                            }

                            R.drawable.language_icon -> {
                                onLanguageClick()
                            }

                            R.drawable.country_icon -> {
                                onCountryClick()
                            }

                            R.drawable.currency_icon -> {
                                onCurrencyClick()
                            }

                            R.drawable.contact_us_icon -> {
                                onContactUsClick()
                            }

                            R.drawable.faq_icon -> {
                                onFaqClick()
                            }

                            R.drawable.terms_conditions_icon -> {
                                onTermsClick()
                            }

                            R.drawable.privacy_policy_icon -> {
                                onPrivacyClick()
                            }

                            R.drawable.shipping_policy_icon -> {
                                onShippingClick()
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ProfileOptionsItem(name: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleEffect { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(painter = painterResource(id = icon), contentDescription = "")
            Text(
                text = name,
                color = Theme.colors.black,
                style = Theme.typography.body.copy(fontWeight = FontWeight(500))
            )
        }
        Icon(
            modifier = Modifier.scale(
                scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                scaleY = 1f
            ),
            painter = painterResource(id = R.drawable.open_location_icon),
            contentDescription = "",
            tint = Theme.colors.greyishTwo
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfileOptionsComponentPrev() {
    DhaibanTheme {
        ProfileOptionsComponent(
            title = "Account",
            options = listOf(
                ProfileOptionUiState("My Profile", 0),
                ProfileOptionUiState("Address", 0),
                ProfileOptionUiState("Language", 0),
            )
        )
    }
}
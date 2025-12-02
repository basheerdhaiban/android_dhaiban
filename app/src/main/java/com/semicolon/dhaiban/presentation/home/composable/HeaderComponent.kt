package com.semicolon.dhaiban.presentation.home.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage

@Composable
fun HeaderComponent(
    profile: String,
    image: String,
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit
) {
    val hiMessage = if (Theme.strings.welcome!="Welcome") Theme.strings.welcome else "hi"
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
                url = image,
                errorImage = R.drawable.avater_img,
                placeholderImage = R.drawable.avater_img,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(1.dp, color = Theme.colors.primary),
                        shape = CircleShape,
                    )
                    .clickable { onProfileClicked() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$hiMessage $profile", style = Theme.typography.title)
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Image",
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
//                .clip(CircleShape)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HeaderComponentPreview() {
    DhaibanTheme {
        HeaderComponent(
            profile = "Mohamed Adel",
            image = "",
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {}
    }
}
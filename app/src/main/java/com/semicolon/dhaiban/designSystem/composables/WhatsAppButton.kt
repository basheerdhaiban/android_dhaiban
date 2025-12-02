package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun WhatsAppButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp), // Fill the entire available space
        verticalArrangement = Arrangement.Top, // Center children vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
    ) {
        Text(
            text = stringResource(R.string.have_trouble),
            color = Theme.colors.black87,
            style = Theme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.contact_us_on_whatsapp),
            color = Theme.colors.black87,
            style = Theme.typography.body,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            modifier = Modifier
                .widthIn(60.dp),
//                            .align(alignment = Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.transparent),
            border = BorderStroke(width = 0.dp, color = Color.Transparent),
            shape = RoundedCornerShape(Theme.radius.medium),
            onClick = onClick,//{ listener.openWhatsApp(context = context) },
            enabled = true
        ) {
            CoilImage(
                shape = RoundedCornerShape(0.dp),
                url = "",
                errorImage = R.drawable.whatsapps,
                placeholderImage = R.drawable.whatsapps,
                contentDescription = "Product Image",
                modifier = Modifier.size(54.dp),
            )
        }
    }
}

@Preview
@Composable
fun PreviewWhatsAppButton() {
    DhaibanTheme {
        WhatsAppButton(){}
    }

}


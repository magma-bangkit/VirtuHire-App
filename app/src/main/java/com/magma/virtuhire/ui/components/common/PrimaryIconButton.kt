package com.magma.virtuhire.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.magma.virtuhire.ui.theme.Primary100
import com.magma.virtuhire.ui.theme.Primary200
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100

@Composable
fun PrimaryIconButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    onClick: () -> Unit,
    variant: ButtonVariant = ButtonVariant.Solid,
    size: ButtonSize = ButtonSize.Large,
    icon: @Composable () -> Unit
) {
    val colors = when (variant) {
        ButtonVariant.Solid -> ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = PrimaryGray100
        )
        ButtonVariant.White -> ButtonDefaults.buttonColors(
            containerColor = PrimaryGray100,
            contentColor = Primary500
        )
        ButtonVariant.Light -> ButtonDefaults.buttonColors(
            containerColor = Primary200,
            contentColor = Primary700
        )
        ButtonVariant.Lighter -> ButtonDefaults.buttonColors(
            containerColor = Primary100,
            contentColor = Primary500
        )
    }

    val style = when (size) {
        ButtonSize.Normal -> MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.SemiBold,
        )
        ButtonSize.Large -> MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        shape = MaterialTheme.shapes.small
    ) {
        Row {
            icon()
            if (text != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, style = style)
            }
        }
    }
}

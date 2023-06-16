package com.magma.virtuhire.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.magma.virtuhire.ui.theme.Primary100
import com.magma.virtuhire.ui.theme.Primary200
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Solid,
    size: ButtonSize = ButtonSize.Large
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
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text, style = style,

        )
    }
}

enum class ButtonSize {
    Normal,
    Large
}

enum class ButtonVariant {
    Solid,
    Light,
    Lighter,
    White,
}


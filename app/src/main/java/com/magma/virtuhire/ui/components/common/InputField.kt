package com.magma.virtuhire.ui.components.common

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.magma.virtuhire.ui.theme.Primary200
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray200
import com.magma.virtuhire.ui.theme.PrimaryGray700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onDoneClicked: () -> Unit = {},
    label: String? = null,
    enabled: Boolean = true,
    validation: (String) -> Pair<Boolean, String> = { true to "" },
    isNumeric: Boolean = false,
    isPassword: Boolean = false,
) {
    val keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text
    val (isValid, errorMessage) = validation(value)
    val borderColor = if (isValid) PrimaryGray700 else Color.Red
    val textColor = if (isValid) Color.Black else Color.Red
    val focusedBorderColor = Primary500

    val isFocused = remember { mutableStateOf(false) }

    Column(modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = PrimaryGray1000
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, if (isFocused.value) focusedBorderColor else borderColor, RoundedCornerShape(6.dp))
                .onFocusChanged { isFocused.value = it.isFocused },
            enabled = enabled,
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor,
                containerColor = PrimaryGray200,
                cursorColor = Primary700,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                imeAction = if (isNumeric) ImeAction.Done else ImeAction.Default
            ),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardActions = KeyboardActions(
                onDone = {
                    onDoneClicked()
                }
            )
        )
        if (!isValid && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}




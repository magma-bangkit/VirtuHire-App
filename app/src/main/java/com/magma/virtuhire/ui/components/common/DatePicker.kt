package com.magma.virtuhire.ui.components.common

import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import com.magma.virtuhire.ui.theme.PrimaryGray200
import com.magma.virtuhire.ui.theme.PrimaryGray700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    displayFormat: FormatStyle = FormatStyle.MEDIUM
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val dateTime = if (value.isNotBlank()) LocalDateTime.parse(value, formatter) else LocalDateTime.now()
    val dialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            val newDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, dateTime.hour, dateTime.minute, dateTime.second)
            onValueChange(newDateTime.format(formatter))
        },
        dateTime.year,
        dateTime.monthValue - 1,
        dateTime.dayOfMonth
    )

    val displayFormatter = DateTimeFormatter.ofLocalizedDate(displayFormat)

    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = dateTime.format(displayFormatter),
            onValueChange = {},
            enabled = false,
            colors = TextFieldDefaults.textFieldColors(
                textColor = PrimaryGray1200,
                containerColor = PrimaryGray200,
                cursorColor = Primary700,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = PrimaryGray1200
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, PrimaryGray700, RoundedCornerShape(6.dp))
                .clickable { dialog.show() },
            trailingIcon = { Icon(Icons.Filled.DateRange, "Date") },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }
}

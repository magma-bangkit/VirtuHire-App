package com.magma.virtuhire.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import com.magma.virtuhire.ui.theme.PrimaryGray200
import com.magma.virtuhire.ui.theme.PrimaryGray700
import com.magma.virtuhire.ui.theme.VirtuHireTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> LargeDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    selectedItem: T? = null,
    onItemSelected: (index: Int, item: T) -> Unit,
    searchValue: String,
    onSearchValueChange: (query: String) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    checkIsItemSelected: (T, T) -> Boolean,
    drawItem: @Composable (T, String, Boolean, Boolean, () -> Unit) -> Unit = { item, name, selected, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = name,
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor = PrimaryGray700
    val focusedBorderColor = Primary500

    val isFocused = remember { mutableStateOf(false) }

    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = modifier.height(IntrinsicSize.Min)) {
            OutlinedTextField(
                value = if (selectedItem != null) selectedItemToString(selectedItem) else "",
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (isFocused.value) focusedBorderColor else borderColor,
                        RoundedCornerShape(6.dp)
                    )
                    .onFocusChanged { isFocused.value = it.isFocused },
                trailingIcon = {
                    val icon =
                        expanded.select(Icons.Filled.KeyboardArrowUp, Icons.Filled.KeyboardArrowDown)
                    Icon(imageVector = icon, "")
                },
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = PrimaryGray1200,
                    containerColor = PrimaryGray200,
                    cursorColor = Primary700,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            // Transparent clickable surface on top of OutlinedTextField
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable(enabled = enabled) { expanded = true },
                color = Color.Transparent,
            ) {}
        }
    }


    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(vertical = 100.dp)
            ) {
                val listState = rememberLazyListState()
                if (selectedIndex > -1) {
                    LaunchedEffect("ScrollToSelected") {
                        listState.scrollToItem(index = selectedIndex)
                    }
                }
                Column(
                    modifier = Modifier.background(PrimaryGray100)
                ) {
                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        InputField(value = searchValue, onValueChange = {
                            onSearchValueChange(it)
                        })
                    }
                    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                        if (notSetLabel != null) {
                            item {
                                LargeDropdownMenuItem(
                                    text = notSetLabel,
                                    selected = false,
                                    enabled = false,
                                    onClick = { },
                                )
                            }
                        }
                        // val filteredItems = items.filter { selectedItemToString(it).contains(searchQuery, ignoreCase = true) }
                        itemsIndexed(items) { index, item ->
                            val isItemSelected = if (selectedItem != null) checkIsItemSelected(
                                item,
                                selectedItem
                            ) else false
                            val itemText = selectedItemToString(item)
                            drawItem(
                                item,
                                itemText,
                                isItemSelected,
                                true
                            ) {
                                onItemSelected(index, item)
                                expanded = false
                            }

                            if (index < items.lastIndex) {
                                Divider(modifier = Modifier.padding(horizontal = 5.dp))
                            }
                        }
                    }
                }

            }
        }
    }
}

fun <T> Boolean.select(a: T, b: T): T = if (this) {
    a
} else {
    b
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy()
        selected -> MaterialTheme.colorScheme.primary.copy()
        else -> MaterialTheme.colorScheme.onSurface.copy()
    }
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> LargeDropdownMenuForMultiple(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    items: List<T>,
    onItemSelected: (index: Int, item: T) -> Unit,
    searchValue: String,
    onSearchValueChange: (query: String) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, String, Boolean, () -> Unit) -> Unit = { item, name, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = name,
            selected = false,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor = PrimaryGray700
    val focusedBorderColor = Primary500

    val isFocused = remember { mutableStateOf(false) }

    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = modifier.height(IntrinsicSize.Min)) {
            OutlinedTextField(
                value = "",
                enabled = enabled,
                placeholder = { Text(text = "Pick One or More", color = PrimaryGray1000)},
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (isFocused.value) focusedBorderColor else borderColor,
                        RoundedCornerShape(6.dp)
                    )
                    .onFocusChanged { isFocused.value = it.isFocused },
                trailingIcon = {
                    val icon =
                        expanded.select(Icons.Filled.KeyboardArrowUp, Icons.Filled.KeyboardArrowDown)
                    Icon(imageVector = icon, "")
                },
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = PrimaryGray1200,
                    containerColor = PrimaryGray200,
                    cursorColor = Primary700,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            // Transparent clickable surface on top of OutlinedTextField
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable(enabled = enabled) { expanded = true },
                color = Color.Transparent,
            ) {}
        }
    }


    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(vertical = 100.dp)
            ) {
                Column(
                    modifier = Modifier.background(PrimaryGray100)
                ) {
                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        InputField(value = searchValue, onValueChange = {
                            onSearchValueChange(it)
                        })
                    }
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        // val filteredItems = items.filter { selectedItemToString(it).contains(searchQuery, ignoreCase = true) }
                        itemsIndexed(items) { index, item ->
                            val itemText = selectedItemToString(item)
                            drawItem(
                                item,
                                itemText,
                                true,
                            ) {
                                onItemSelected(index, item)
                                expanded = false
                            }

                            if (index < items.lastIndex) {
                                Divider(modifier = Modifier.padding(horizontal = 5.dp))
                            }
                        }
                    }
                }

            }
        }
    }
}
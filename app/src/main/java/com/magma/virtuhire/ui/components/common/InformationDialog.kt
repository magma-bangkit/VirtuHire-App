package com.magma.virtuhire.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.magma.virtuhire.ui.theme.Primary100
import com.magma.virtuhire.ui.theme.Primary200
import com.magma.virtuhire.ui.theme.Primary50
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.VirtuHireTheme

@Composable
fun InformationDialog(
    isDialogOpen: Boolean,
    onDialogClose: () -> Unit,
    title: String,
    message: String
) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = { onDialogClose() }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(vertical = 100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(PrimaryGray100)
                        .padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Primary100)
                                .padding(20.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Primary500)
                                    .padding(20.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(50.dp),
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "Information",
                                    tint = PrimaryGray100
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    PrimaryButton(
                        size = ButtonSize.Normal,
                        onClick = { onDialogClose() },
                        text = "Close"
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun InformationDialogPreview() {
    VirtuHireTheme {
        ErrorDialog(isDialogOpen = true, onDialogClose = {}, errorMessage = "")
    }
}
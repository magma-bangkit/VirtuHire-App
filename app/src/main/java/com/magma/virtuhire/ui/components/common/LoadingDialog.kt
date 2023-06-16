package com.magma.virtuhire.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.VirtuHireTheme

@Composable
fun LoadingDialog(isDialogOpen: Boolean, onDialogClose: () -> Unit) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = { onDialogClose() }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(vertical = 100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(PrimaryGray100)
                        .padding(30.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        DotsTyping()
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Loading...",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoadingDialogPreview() {
    VirtuHireTheme {
        LoadingDialog(isDialogOpen = true, onDialogClose = {})
    }
}
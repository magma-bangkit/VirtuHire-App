package com.magma.virtuhire.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.magma.virtuhire.R
import com.magma.virtuhire.ui.components.common.DotsTyping
import com.magma.virtuhire.ui.components.layout.bottombar.BottomBar
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray200
import com.magma.virtuhire.ui.theme.PrimaryGray700
import com.magma.virtuhire.ui.theme.VirtuHireTheme
import com.magma.virtuhire.utils.showToast
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    navController: NavHostController = rememberNavController(),
    navigateToHistory: () -> Unit,
    navigateToTerms: () -> Unit,
    navigateToOnboard: () -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val state = settingViewModel.profileState.value
    val logOutState = settingViewModel.logOutState.value

    var isLoadingDialogExpanded by remember { mutableStateOf(false) }
    var isErrorDialogExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(logOutState) {
        isLoadingDialogExpanded = logOutState.isLoading
        if (logOutState.isError) {
            isErrorDialogExpanded = true
        }
        if (!logOutState.isError && logOutState.isLoggedOut) {
            showToast(context, "Logout Success")
            navigateToOnboard()

        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryGray200)
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = stringResource(R.string.setting_screen_headline),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
            )

            if (state.isLoading) {
                ProfileCardSkeleton()
            } else {
                if (state.profile != null) {
                    ProfileCard(
                        name = state.profile.firstName + state.profile.lastName,
                        email = state.profile.email
                    )
                }
                else {
                    ProfileCardSkeleton()
                }
            }


            SettingItem(
                "Interview History",
                painter = painterResource(R.drawable.history),
                onItemClick = { navigateToHistory() })
            Divider(color = PrimaryGray700)
            SettingItem(
                "Terms and Condition",
                painter = painterResource(R.drawable.corporate),
                onItemClick = { navigateToTerms() })
            Divider(color = PrimaryGray700)
            SettingItem("Logout", icon = Icons.Filled.ExitToApp, onItemClick = {
                coroutineScope.launch {
                    settingViewModel.logOutUser()
                }
            })
            Divider(color = PrimaryGray700)


        }
        BottomBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun ProfileCardSkeleton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(8.dp))
            .background(PrimaryGray100)
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        DotsTyping()
    }
}

@Composable
fun ProfileCard(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(8.dp))
            .background(PrimaryGray100)
            .clickable { /* Handle click event */ }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(PrimaryGray700)
            ) {
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = PrimaryGray1000
                )
            }
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null, // Content description can be null for decorative icons
            tint = PrimaryGray1000
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    icon: ImageVector? = null,
    painter: Painter? = null,
    onItemClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null)
        }
        if (painter != null) {
            Icon(painter, contentDescription = null)
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
@Preview
fun SettingScreenPreview() {
    VirtuHireTheme {
        SettingScreen(
            navigateToHistory = {},
            navigateToTerms = {},
            navigateToOnboard = {}
        )
    }
}
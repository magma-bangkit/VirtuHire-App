package com.magma.virtuhire.ui.components.layout.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.magma.virtuhire.R
import com.magma.virtuhire.ui.navigation.NavigationItem
import com.magma.virtuhire.ui.navigation.Screen
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import com.magma.virtuhire.ui.theme.PrimaryGray300
import com.magma.virtuhire.ui.theme.PrimaryGray500
import com.magma.virtuhire.ui.theme.VirtuHireTheme

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationItems = listOf(
        NavigationItem(
            title = stringResource(R.string.menu_home),
            icon = Icons.Filled.Home,
            screen = Screen.Home
        ),
        NavigationItem(
            title = stringResource(R.string.menu_interview),
            icon = ImageVector.vectorResource(R.drawable.chat_bubble),
            screen = Screen.History
        ),
        NavigationItem(
            title = stringResource(R.string.menu_profile),
            icon = Icons.Filled.Person,
            screen = Screen.Setting
        ),
    )

    Box(modifier = modifier.padding(20.dp)) {
        NavigationBar(
            modifier = modifier.shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(60.dp),
                clip = true
            ),
            containerColor = PrimaryGray100
        ) {
            navigationItems.map { item ->
                NavigationBarItem(
                    modifier = Modifier,
                    icon = {
                        Icon(
                            modifier = Modifier
                                .graphicsLayer(alpha = 0.99f)
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush = if (currentRoute == item.screen.route) Brush.verticalGradient(
                                                colors = listOf(
                                                    Primary500, Primary700
                                                ),
                                                startY = 0f,
                                            ) else Brush.verticalGradient(
                                                colors = listOf(
                                                    PrimaryGray1200, PrimaryGray1200
                                                ),
                                                startY = 0f,
                                            ), blendMode = BlendMode.SrcAtop
                                        )
                                    }
                                },
                            imageVector = item.icon,
                            contentDescription = item.title,

                            )
                    },
                    selected = currentRoute == item.screen.route,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = PrimaryGray100
                    ),
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }

}

@Preview
@Composable
fun BottomBarPreview() {
    VirtuHireTheme {
        BottomBar(navController = rememberNavController())
    }
}
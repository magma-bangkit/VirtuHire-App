package com.magma.virtuhire

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.magma.virtuhire.ui.components.layout.bottombar.BottomBar
import com.magma.virtuhire.ui.navigation.Screen
import com.magma.virtuhire.ui.screen.detail.DetailScreen
import com.magma.virtuhire.ui.screen.find.FindScreen
import com.magma.virtuhire.ui.screen.history.HistoryScreen
import com.magma.virtuhire.ui.screen.history_detail.HistoryDetailScreen
import com.magma.virtuhire.ui.screen.home.HomeScreen
import com.magma.virtuhire.ui.screen.interview.InterviewScreen
import com.magma.virtuhire.ui.screen.introduction.IntroductionScreen
import com.magma.virtuhire.ui.screen.login.LoginScreen
import com.magma.virtuhire.ui.screen.profile.ProfileScreen
import com.magma.virtuhire.ui.screen.register.RegisterScreen
import com.magma.virtuhire.ui.screen.setting.SettingScreen
import com.magma.virtuhire.ui.screen.onboard.OnboardScreen
import com.magma.virtuhire.ui.screen.terms_and_condition.TermsScreen
import com.magma.virtuhire.ui.theme.VirtuHireTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtuHireApp(
    modifier: Modifier = Modifier,
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val screenWithBottomBar = listOf(Screen.Setting.route)

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Onboard.route) {
                OnboardScreen(
                    navigateToLogin = {
                        navController.navigate(Screen.Login.route)
                    }, navigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    })
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    navigateToRegister = {
                        navController.popBackStack()
                        navController.navigate(Screen.Register.route)
                    },
                    navigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboard.route) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToIntroduction = {
                        navController.navigate(Screen.Introduction.createRoute(isFromLogin = true)) {
                            popUpTo(Screen.Onboard.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(navigateToLogin = {
                    navController.popBackStack()
                    navController.navigate(Screen.Login.route)
                })
            }
            composable(
                Screen.Introduction.route,
                arguments = listOf(navArgument("isFromLogin") { type = NavType.BoolType }),
            ) {
                val isFromLogin = it.arguments?.getBoolean("isFromLogin") ?: false
                IntroductionScreen(isFromLogin = isFromLogin, navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Introduction.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    navigateToDetail = { jobId ->
                        navController.navigate(Screen.Detail.createRoute(jobId))
                    },
                    navigateToFind = {
                        navController.navigate(Screen.Find.route)
                    }
                )
            }
            composable(Screen.Setting.route) {
                SettingScreen(
                    navController = navController,
                    navigateToHistory = {
                        navController.navigate(Screen.History.route)
                    },
                    navigateToTerms = {
                        navController.navigate(Screen.Terms.route)
                    },
                    navigateToOnboard = {
                        navController.navigate(Screen.Onboard.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Terms.route) {
                TermsScreen(
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    navController = navController,
                    navigateToDetail = {
                        navController.navigate(Screen.HistoryDetail.createRoute(it))
                    }
                )
            }
            composable(Screen.Find.route) {
                FindScreen(
                    navigateToDetail = { jobId ->
                        navController.navigate(Screen.Detail.createRoute(jobId))
                    },
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("jobId") { type = NavType.StringType }),
            ) {
                val id = it.arguments?.getString("jobId") ?: ""
                DetailScreen(
                    jobId = id,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    navigateToInterview = {
                        navController.navigate(Screen.Interview.createRoute(id))
                    }
                )
            }
            composable(
                Screen.Interview.route,
                arguments = listOf(navArgument("jobId") { type = NavType.StringType }),
            ) {
                val id = it.arguments?.getString("jobId") ?: ""
                InterviewScreen(jobId = id, navigateBack = {navController.navigateUp()})

            }

            composable(
                Screen.HistoryDetail.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.StringType }),
            ) {
                val id = it.arguments?.getString("sessionId") ?: ""
                HistoryDetailScreen(sessionId = id, navigateBack = { navController.navigateUp() })

            }
        }
    }
}

@Composable
fun DefaultScreen(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun VirtuHireAppPreview() {
    VirtuHireTheme {
        VirtuHireApp(Modifier, Screen.Home.route)
    }
}
package com.magma.virtuhire.ui.navigation

sealed class Screen(val route: String) {
    object Onboard : Screen("onboard")
    object Login : Screen("login")
    object Register : Screen("register")
    object Introduction : Screen("introduction/{isFromLogin}") {
        fun createRoute(isFromLogin: Boolean) = "introduction/$isFromLogin"
    }

    object Home : Screen("home")
    object Cart : Screen("cart")
    object Setting : Screen("setting")
    object Profile : Screen("profile")
    object Find : Screen("find")
    object History : Screen("history")
    object Terms : Screen("terms_and_condition")
    object Detail : Screen("home/{jobId}") {
        fun createRoute(jobId: String) = "home/$jobId"
    }

    object Interview : Screen("home/{jobId}/interview") {
        fun createRoute(jobId: String) = "home/$jobId/interview"
    }

    object HistoryDetail : Screen("history/{sessionId}") {
        fun createRoute(sessionId: String) = "history/$sessionId"
    }
}
package com.guru.app.ui.navigation

sealed class ScreenRoute(val route: String) {
    object Splash : ScreenRoute("splash")
    object Onboarding : ScreenRoute("onboarding")
    object Login : ScreenRoute("login")
    object Signup : ScreenRoute("signup")
    object ForgotPassword : ScreenRoute("forgot_password")
    object Dashboard : ScreenRoute("dashboard")
    object Timer : ScreenRoute("timer")
    object Ambient : ScreenRoute("ambient")
    object Planner : ScreenRoute("planner")
    object Calendar : ScreenRoute("calendar")
    object Profile : ScreenRoute("profile")
    object Settings : ScreenRoute("settings")
}

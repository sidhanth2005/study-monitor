package com.guru.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.guru.app.ui.components.BottomNavBar
import com.guru.app.ui.screens.ambient.AmbientSoundScreen
import com.guru.app.ui.screens.auth.ForgotPasswordScreen
import com.guru.app.ui.screens.auth.LoginScreen
import com.guru.app.ui.screens.auth.SignupScreen
import com.guru.app.ui.screens.calendar.CalendarScreen
import com.guru.app.ui.screens.dashboard.DashboardScreen
import com.guru.app.ui.screens.onboarding.OnboardingScreen
import com.guru.app.ui.screens.planner.PlannerScreen
import com.guru.app.ui.screens.profile.ProfileScreen
import com.guru.app.ui.screens.settings.SettingsScreen
import com.guru.app.ui.screens.splash.SplashScreen
import com.guru.app.ui.screens.timer.TimerScreen

val mainBottomBarRoutes = listOf(
    ScreenRoute.Dashboard.route,
    ScreenRoute.Timer.route,
    ScreenRoute.Ambient.route,
    ScreenRoute.Planner.route,
    ScreenRoute.Calendar.route,
    ScreenRoute.Profile.route
)

@Composable
fun GuruNavGraph(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in mainBottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(ScreenRoute.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(ScreenRoute.Splash.route) {
                SplashScreen(
                    onNavigate = { destination ->
                        navController.navigate(destination) {
                            popUpTo(ScreenRoute.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(ScreenRoute.Onboarding.route) {
                OnboardingScreen(
                    onNavigate = { destination ->
                        navController.navigate(destination) {
                            popUpTo(ScreenRoute.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(ScreenRoute.Login.route) {
                LoginScreen(
                    onNavigate = { destination ->
                        if (destination == ScreenRoute.Dashboard.route) {
                            navController.navigate(destination) {
                                popUpTo(ScreenRoute.Login.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(destination)
                        }
                    }
                )
            }

            composable(ScreenRoute.Signup.route) {
                SignupScreen(
                    onNavigate = { destination ->
                        if (destination == ScreenRoute.Dashboard.route) {
                            navController.navigate(destination) {
                                popUpTo(ScreenRoute.Signup.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(destination)
                        }
                    }
                )
            }

            composable(ScreenRoute.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ScreenRoute.Dashboard.route) {
                DashboardScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(ScreenRoute.Timer.route) {
                TimerScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ScreenRoute.Ambient.route) {
                AmbientSoundScreen()
            }

            composable(ScreenRoute.Planner.route) {
                PlannerScreen()
            }

            composable(ScreenRoute.Calendar.route) {
                CalendarScreen()
            }

            composable(ScreenRoute.Profile.route) {
                ProfileScreen()
            }

            composable(ScreenRoute.Settings.route) {
                SettingsScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
        }
    }
}

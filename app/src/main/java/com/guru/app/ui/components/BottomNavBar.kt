package com.guru.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.guru.app.ui.navigation.ScreenRoute
import com.guru.app.ui.theme.GlassBorder

data class NavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    NavigationItem(ScreenRoute.Dashboard.route, "Home", Icons.Default.Home),
    NavigationItem(ScreenRoute.Timer.route, "Timer", Icons.Default.Timer),
    NavigationItem(ScreenRoute.Ambient.route, "Ambient", Icons.Default.GraphicEq),
    NavigationItem(ScreenRoute.Planner.route, "Tasks", Icons.Default.CheckCircle),
    NavigationItem(ScreenRoute.Calendar.route, "Calendar", Icons.Default.DateRange),
    NavigationItem(ScreenRoute.Profile.route, "Profile", Icons.Default.Person)
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
            )
        }
    }
}

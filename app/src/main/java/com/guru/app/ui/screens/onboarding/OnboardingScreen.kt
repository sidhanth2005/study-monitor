package com.guru.app.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guru.app.ui.components.GlassCard
import com.guru.app.ui.components.GradientButton
import com.guru.app.ui.navigation.ScreenRoute
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badge: String
)

val onboardingPages = listOf(
    OnboardingPageData(
        title = "Welcome to GURU",
        description = "Your personal AI-powered productivity system designed to eliminate distractions and boost deep focus.",
        icon = Icons.Default.Bolt,
        badge = "01 / EXPLAIN"
    ),
    OnboardingPageData(
        title = "Focus Sessions",
        description = "Master your time with Pomodoro, Stopwatch, Countdown, and Deep Work timer modes.",
        icon = Icons.Default.Timer,
        badge = "02 / TIMERS"
    ),
    OnboardingPageData(
        title = "Task Planning",
        description = "Organize tasks with categories, priorities, estimated pomodoros, and reminders.",
        icon = Icons.Default.CheckCircle,
        badge = "03 / PLANNER"
    ),
    OnboardingPageData(
        title = "Daily Goals",
        description = "Set customized daily study targets, keep active streaks, and build strong habits.",
        icon = Icons.Default.LocalFireDepartment,
        badge = "04 / GOALS"
    ),
    OnboardingPageData(
        title = "Progress Tracking",
        description = "Earn XP, unlock level badges, and analyze your detailed daily focus statistics.",
        icon = Icons.Default.BarChart,
        badge = "05 / TRACKING"
    ),
    OnboardingPageData(
        title = "Supercharge Productivity",
        description = "Ready to elevate your performance? Let's kick off your productivity journey!",
        icon = Icons.Default.Stars,
        badge = "06 / GET STARTED"
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onNavigate: (String) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GURU",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            if (pagerState.currentPage < onboardingPages.size - 1) {
                TextButton(
                    onClick = {
                        viewModel.completeOnboarding {
                            onNavigate(ScreenRoute.Login.route)
                        }
                    }
                ) {
                    Text("Skip", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val pageData = onboardingPages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlassCard(
                    modifier = Modifier.size(160.dp),
                    cornerRadius = 80.dp,
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = pageData.icon,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = pageData.badge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = pageData.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pageData.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        // Pager indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 20.dp)
        ) {
            repeat(onboardingPages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(8.dp)
                        .width(if (isSelected) 28.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                )
            }
        }

        GradientButton(
            text = if (pagerState.currentPage == onboardingPages.size - 1) "Get Started" else "Next",
            onClick = {
                if (pagerState.currentPage < onboardingPages.size - 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    viewModel.completeOnboarding {
                        onNavigate(ScreenRoute.Login.route)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

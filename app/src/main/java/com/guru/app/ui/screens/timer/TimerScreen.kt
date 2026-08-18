package com.guru.app.ui.screens.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guru.app.domain.model.TimerMode
import com.guru.app.ui.components.AnimatedProgressRing
import com.guru.app.ui.components.GlassCard
import com.guru.app.ui.components.GradientButton
import com.guru.app.ui.theme.AccentGreen
import com.guru.app.ui.theme.AccentOrange
import com.guru.app.ui.theme.PrimaryCyan
import com.guru.app.ui.theme.PrimaryNeon

@Composable
fun TimerScreen(
    onNavigateBack: () -> Unit,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val mins = state.remainingSeconds / 60
    val secs = state.remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)

    val progress = if (state.targetSeconds > 0) {
        if (state.selectedMode == TimerMode.STOPWATCH) 1.0f
        else (state.targetSeconds - state.remainingSeconds).toFloat() / state.targetSeconds.toFloat()
    } else 0f

    if (state.isSessionCompleted) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSessionCompleteDialog() },
            title = { Text("🎉 Focus Session Completed!", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Awesome job! You successfully stayed focused during this ${state.selectedMode.name} session.")
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PrimaryNeon.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "+${state.earnedXp} XP Earned!",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryNeon
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.dismissSessionCompleteDialog() }) {
                    Text("Continue")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (state.isFullscreenMode) Color.Black else MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Fullscreen toggle & title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Text(
                    text = "FOCUS TIMER",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                IconButton(onClick = { viewModel.toggleFullscreen() }) {
                    Icon(
                        imageVector = if (state.isFullscreenMode) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = "Fullscreen"
                    )
                }
            }

            // Mode Selector Tabs (Hidden in Fullscreen)
            AnimatedVisibility(visible = !state.isFullscreenMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TimerModeTab(
                        title = "Pomodoro",
                        isSelected = state.selectedMode == TimerMode.POMODORO
                    ) { viewModel.selectMode(TimerMode.POMODORO) }

                    TimerModeTab(
                        title = "Deep Work",
                        isSelected = state.selectedMode == TimerMode.DEEP_WORK
                    ) { viewModel.selectMode(TimerMode.DEEP_WORK) }

                    TimerModeTab(
                        title = "Countdown",
                        isSelected = state.selectedMode == TimerMode.COUNTDOWN
                    ) { viewModel.selectMode(TimerMode.COUNTDOWN) }

                    TimerModeTab(
                        title = "Stopwatch",
                        isSelected = state.selectedMode == TimerMode.STOPWATCH
                    ) { viewModel.selectMode(TimerMode.STOPWATCH) }
                }
            }

            // Center Circular Timer Engine
            AnimatedProgressRing(
                progress = progress,
                size = 280.dp,
                strokeWidth = 20.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 54.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = CircleShape,
                        color = PrimaryNeon.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = state.selectedMode.name,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                            color = PrimaryNeon
                        )
                    }
                }
            }

            // Timer Controls: Play / Pause / Reset
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(if (state.isRunning) AccentOrange else PrimaryNeon)
                        .clickable {
                            if (state.isRunning) viewModel.pauseTimer()
                            else viewModel.startTimer()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isRunning) "Pause" else "Start",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TimerModeTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryNeon else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
            ),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

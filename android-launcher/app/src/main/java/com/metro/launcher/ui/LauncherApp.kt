package com.metro.launcher.ui

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.metro.launcher.data.InstalledApp
import com.metro.launcher.data.LauncherPreferences
import com.metro.launcher.data.queryInstalledApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private enum class LauncherScreen {
    Home,
    Apps,
    Notes,
    Calculator,
    Game2048,
    Weather,
    Shortcuts,
}

@Composable
fun LauncherApp() {
    val context = LocalContext.current
    val preferences = remember { LauncherPreferences(context.applicationContext) }
    val note by preferences.note.collectAsState(initial = "")
    val highScore by preferences.highScore.collectAsState(initial = 0)
    val scope = rememberCoroutineScope()
    var route by rememberSaveable { mutableStateOf(LauncherScreen.Home.name) }
    var installedApps by remember { mutableStateOf(emptyList<InstalledApp>()) }

    LaunchedEffect(Unit) {
        installedApps = withContext(Dispatchers.IO) { queryInstalledApps(context) }
    }

    val currentScreen = runCatching { LauncherScreen.valueOf(route) }
        .getOrDefault(LauncherScreen.Home)
    val goHome: () -> Unit = { route = LauncherScreen.Home.name }

    BackHandler(enabled = currentScreen != LauncherScreen.Home) {
        route = when (currentScreen) {
            LauncherScreen.Apps -> LauncherScreen.Home.name
            else -> LauncherScreen.Home.name
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF14151B), Color(0xFF0F1015), Color(0xFF171820)),
                ),
            )
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Crossfade(
            targetState = currentScreen,
            animationSpec = tween(durationMillis = 220),
            label = "launcher-navigation",
        ) { screen ->
            when (screen) {
                LauncherScreen.Home -> HomeScreen(
                    appCount = installedApps.size,
                    notePreview = note,
                    onOpenApps = { route = LauncherScreen.Apps.name },
                    onOpenNotes = { route = LauncherScreen.Notes.name },
                    onOpenGame = { route = LauncherScreen.Game2048.name },
                    onOpenWeather = { route = LauncherScreen.Weather.name },
                    onOpenShortcuts = { route = LauncherScreen.Shortcuts.name },
                    onLaunchIntent = { intent -> launchSafely(context, intent) },
                )

                LauncherScreen.Apps -> AppDrawerScreen(
                    apps = installedApps,
                    onBack = goHome,
                    onRefresh = {
                        scope.launch {
                            installedApps = withContext(Dispatchers.IO) {
                                queryInstalledApps(context)
                            }
                        }
                    },
                    onLaunchApp = { app -> launchInstalledApp(context, app) },
                )

                LauncherScreen.Notes -> NotesScreen(
                    value = note,
                    onValueChange = { value ->
                        scope.launch { preferences.saveNote(value) }
                    },
                    onBack = goHome,
                )

                LauncherScreen.Calculator -> CalculatorScreen(onBack = goHome)
                LauncherScreen.Game2048 -> Game2048Screen(
                    highScore = highScore,
                    onHighScore = { score ->
                        scope.launch { preferences.saveHighScore(score) }
                    },
                    onBack = goHome,
                )
                LauncherScreen.Weather -> WeatherScreen(onBack = goHome)
                LauncherScreen.Shortcuts -> ShortcutsScreen(
                    onBack = goHome,
                    onOpenCalculator = { route = LauncherScreen.Calculator.name },
                    onLaunchIntent = { intent -> launchSafely(context, intent) },
                )
            }
        }
    }
}

private fun launchInstalledApp(context: Context, app: InstalledApp) {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        component = ComponentName(app.packageName, app.className)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    launchSafely(context, intent)
}

private fun launchSafely(context: Context, intent: Intent) {
    try {
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "That app is not available on this device.", Toast.LENGTH_SHORT)
            .show()
    } catch (_: SecurityException) {
        Toast.makeText(context, "Android blocked this action.", Toast.LENGTH_SHORT).show()
    }
}

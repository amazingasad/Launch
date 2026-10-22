package com.metro.launcher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build

data class InstalledApp(
    val label: String,
    val packageName: String,
    val className: String,
    val icon: Drawable,
)

fun queryInstalledApps(context: Context): List<InstalledApp> {
    val manager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    val activities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        manager.queryIntentActivities(
            intent,
            PackageManager.ResolveInfoFlags.of(0),
        )
    } else {
        @Suppress("DEPRECATION")
        manager.queryIntentActivities(intent, 0)
    }

    return activities
        .mapNotNull { result ->
            val info = result.activityInfo ?: return@mapNotNull null
            InstalledApp(
                label = result.loadLabel(manager).toString(),
                packageName = info.packageName,
                className = info.name,
                icon = result.loadIcon(manager),
            )
        }
        .distinctBy { "${it.packageName}/${it.className}" }
        .sortedBy { it.label.lowercase() }
}

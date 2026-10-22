package com.metro.launcher.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metro.launcher.data.InstalledApp

@Composable
fun AppDrawerScreen(
    apps: List<InstalledApp>,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onLaunchApp: (InstalledApp) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(apps, query) {
        apps.filter { it.label.contains(query.trim(), ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Text("‹", color = Color(0xFFF4F1E9), fontSize = 32.sp)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "APPLICATIONS",
                    color = Color(0xFFF4F1E9),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp,
                )
                Text(
                    text = "${apps.size} installed apps",
                    color = Color(0xFFA3A5AF),
                    fontSize = 12.sp,
                )
            }
            IconButton(onClick = onRefresh) {
                Text("↻", color = Color(0xFFB9F36A), fontSize = 24.sp)
            }
        }

        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Search installed apps") },
            shape = RoundedCornerShape(17.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB9F36A),
                unfocusedBorderColor = Color(0xFF383A44),
                focusedTextColor = Color(0xFFF4F1E9),
                unfocusedTextColor = Color(0xFFF4F1E9),
                focusedPlaceholderColor = Color(0xFF8D8F99),
                unfocusedPlaceholderColor = Color(0xFF8D8F99),
                cursorColor = Color(0xFFB9F36A),
            ),
        )

        Spacer(Modifier.height(13.dp))
        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 54.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (apps.isEmpty()) "No launcher apps found on this device."
                    else "No apps match “$query”.",
                    color = Color(0xFFA3A5AF),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                items(filtered, key = { "${it.packageName}/${it.className}" }) { app ->
                    AppRow(app = app, onClick = { onLaunchApp(app) })
                }
            }
        }
    }
}

@Composable
private fun AppRow(app: InstalledApp, onClick: () -> Unit) {
    val bitmap = remember(app.packageName, app.className) { app.icon.toBitmap() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF292B34)),
            )
            Column {
                Text(
                    text = app.label,
                    color = Color(0xFFF1EFE9),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = app.packageName,
                    color = Color(0xFF858792),
                    fontSize = 10.sp,
                )
            }
        }
    }
}

private fun Drawable.toBitmap(): Bitmap {
    val width = intrinsicWidth.coerceAtLeast(1).coerceAtMost(192)
    val height = intrinsicHeight.coerceAtLeast(1).coerceAtMost(192)
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { bitmap ->
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}

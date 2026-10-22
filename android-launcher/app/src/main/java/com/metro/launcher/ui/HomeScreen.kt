package com.metro.launcher.ui

import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class HomeTile(
    val title: String,
    val detail: String,
    val symbol: String,
    val color: Color,
    val wide: Boolean = false,
    val height: Int = 138,
    val action: () -> Unit,
)

@Composable
fun HomeScreen(
    appCount: Int,
    notePreview: String,
    onOpenApps: () -> Unit,
    onOpenNotes: () -> Unit,
    onOpenGame: () -> Unit,
    onOpenWeather: () -> Unit,
    onOpenShortcuts: () -> Unit,
    onLaunchIntent: (Intent) -> Unit,
) {
    val context = LocalContext.current
    val deviceStatus = remember { readDeviceStatus(context) }
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(30_000)
        }
    }
    val time = now.format(DateTimeFormatter.ofPattern("h:mm", Locale.getDefault()))
    val period = now.format(DateTimeFormatter.ofPattern("a", Locale.getDefault()))
    val date = now.format(DateTimeFormatter.ofPattern("EEE  /  d MMM", Locale.getDefault()))

    val tiles = listOf(
        HomeTile(
            title = "TODAY · SAMPLE",
            detail = "10:30  Studio check-in",
            symbol = now.dayOfMonth.toString().padStart(2, '0'),
            color = Color(0xFFE15B46),
            height = 158,
            action = {
                onLaunchIntent(
                    Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_CALENDAR),
                )
            },
        ),
        HomeTile(
            title = "PHOTOS",
            detail = "02 sample memories",
            symbol = "02",
            color = Color(0xFFD6B65E),
            height = 158,
            action = {
                onLaunchIntent(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"))
            },
        ),
        HomeTile(
            title = "WEATHER · SAMPLE",
            detail = "PARTLY CLOUDY  ·  SAMPLE FORECAST",
            symbol = "24°",
            color = Color(0xFF458F99),
            wide = true,
            height = 100,
            action = onOpenWeather,
        ),
        HomeTile(
            title = "MUSIC",
            detail = "Open your music app",
            symbol = "♫",
            color = Color(0xFFBE4C69),
            height = 132,
            action = {
                onLaunchIntent(
                    Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MUSIC),
                )
            },
        ),
        HomeTile(
            title = "QUICK NOTE",
            detail = notePreview.trim().ifBlank { "Make room for good ideas." }
                .replace('\n', ' ')
                .take(34),
            symbol = "N",
            color = Color(0xFFB8D548),
            height = 132,
            action = onOpenNotes,
        ),
        HomeTile(
            title = "BROWSER",
            detail = "Open a page in your browser",
            symbol = "↗",
            color = Color(0xFF31588E),
            wide = true,
            height = 86,
            action = {
                onLaunchIntent(
                    Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.google.com")),
                )
            },
        ),
        HomeTile(
            title = "GAMES",
            detail = "PLAY A ROUND",
            symbol = "2048",
            color = Color(0xFFB9693D),
            wide = true,
            height = 86,
            action = onOpenGame,
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = date.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFD1D0C6),
                letterSpacing = 1.7.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${deviceStatus.batteryPercent}%  ·  ${deviceStatus.network}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFB9F36A),
                letterSpacing = 1.1.sp,
            )
        }

        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = time,
                color = Color(0xFFF4F1E9),
                fontSize = 74.sp,
                lineHeight = 78.sp,
                letterSpacing = (-5).sp,
                fontWeight = FontWeight.Light,
            )
            Text(
                text = " ${period.lowercase(Locale.getDefault())}",
                modifier = Modifier.padding(bottom = 10.dp),
                color = Color(0xFFB9F36A),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Text(
            text = "A good day to begin.",
            color = Color(0xFFB5B6BE),
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(15.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            itemsIndexed(
                items = tiles,
                span = { _, tile -> GridItemSpan(if (tile.wide) 2 else 1) },
                key = { index, tile -> "${tile.title}-$index" },
            ) { _, tile ->
                MetroTile(
                    tile = tile,
                    modifier = Modifier
                        .fillMaxWidth()
                    .height(tile.height.dp),
                )
            }
        }

        BottomLauncherBar(
            appCount = appCount,
            onOpenApps = onOpenApps,
            onOpenShortcuts = onOpenShortcuts,
        )
    }
}

@Composable
private fun MetroTile(tile: HomeTile, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.975f else 1f,
        label = "tile-press",
    )
    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = tile.action,
            ),
        shape = RoundedCornerShape(21.dp),
        colors = CardDefaults.cardColors(containerColor = tile.color),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.11f), Color.Transparent),
                    ),
                )
                .padding(15.dp),
        ) {
            Column(
                modifier = Modifier.align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = tile.title.uppercase(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp,
                )
                Text(
                    text = tile.detail,
                    color = Color.White.copy(alpha = 0.76f),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = tile.symbol,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(start = 52.dp),
                color = Color.White,
                fontSize = when {
                    tile.title.startsWith("TODAY") -> 36.sp
                    tile.wide -> 31.sp
                    else -> 25.sp
                },
                lineHeight = 34.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
private fun BottomLauncherBar(
    appCount: Int,
    onOpenApps: () -> Unit,
    onOpenShortcuts: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.material3.TextButton(onClick = onOpenApps) {
            Text(
                text = "ALL APPS  ·  $appCount  ↗",
                color = Color(0xFFB9F36A),
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        androidx.compose.material3.TextButton(onClick = onOpenShortcuts) {
            Text(
                text = "QUICK TOOLS",
                color = Color(0xFFA2A4AE),
                fontSize = 10.sp,
                letterSpacing = 1.3.sp,
            )
        }
    }
}

private data class DeviceStatus(
    val batteryPercent: Int,
    val network: String,
)

private fun readDeviceStatus(context: Context): DeviceStatus {
    val battery = context.registerReceiver(
        null,
        IntentFilter(Intent.ACTION_BATTERY_CHANGED),
    )
    val level = battery?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = battery?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val percent = if (level >= 0 && scale > 0) (level * 100) / scale else 0

    val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        as? ConnectivityManager
    val activeNetwork = connectivity?.activeNetwork
    val capabilities = activeNetwork?.let(connectivity::getNetworkCapabilities)
    val network = when {
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "WI-FI"
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "CELL"
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true -> "ONLINE"
        else -> "OFFLINE"
    }
    return DeviceStatus(percent, network)
}

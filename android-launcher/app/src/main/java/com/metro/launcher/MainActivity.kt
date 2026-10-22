package com.metro.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.metro.launcher.ui.LauncherApp
import com.metro.launcher.ui.theme.MetroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF101116),
                ) {
                    LauncherApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LauncherPreview() {
    MetroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF101116),
        ) {
            LauncherApp()
        }
    }
}

package com.metro.launcher.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotesScreen(
    value: String,
    onValueChange: (String) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) {
        ScreenHeader(title = "NOTES", onBack = onBack)
        Spacer(Modifier.height(20.dp))
        Text(
            text = "A place for the things you want to remember.",
            color = Color(0xFFB4B5BD),
            fontSize = 13.sp,
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = { Text("Write a note…") },
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB9F36A),
                unfocusedBorderColor = Color(0xFF383A44),
                focusedTextColor = Color(0xFFF4F1E9),
                unfocusedTextColor = Color(0xFFF4F1E9),
                focusedPlaceholderColor = Color(0xFF858792),
                unfocusedPlaceholderColor = Color(0xFF858792),
                cursorColor = Color(0xFFB9F36A),
            ),
        )
        Text(
            text = "Saved on this device as you type",
            modifier = Modifier.padding(vertical = 12.dp),
            color = Color(0xFF8F919B),
            fontSize = 11.sp,
        )
    }
}

private data class Shortcut(
    val title: String,
    val detail: String,
    val marker: String,
    val color: Color,
    val intent: Intent,
)

@Composable
fun ShortcutsScreen(
    onBack: () -> Unit,
    onOpenCalculator: () -> Unit,
    onLaunchIntent: (Intent) -> Unit,
) {
    val shortcuts = listOf(
        Shortcut(
            title = "Phone",
            detail = "Open the system dialer",
            marker = "01",
            color = Color(0xFF466F65),
            intent = Intent(Intent.ACTION_DIAL),
        ),
        Shortcut(
            title = "Messages",
            detail = "Open the default SMS app",
            marker = "02",
            color = Color(0xFF596C92),
            intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")),
        ),
        Shortcut(
            title = "Browser",
            detail = "Open a web page in your browser",
            marker = "03",
            color = Color(0xFF96694E),
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")),
        ),
        Shortcut(
            title = "Calendar",
            detail = "Open your calendar app",
            marker = "04",
            color = Color(0xFF77659B),
            intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_CALENDAR),
        ),
        Shortcut(
            title = "Camera",
            detail = "Open the system camera",
            marker = "05",
            color = Color(0xFF8A6B41),
            intent = Intent("android.media.action.IMAGE_CAPTURE"),
        ),
        Shortcut(
            title = "System settings",
            detail = "Open Android settings",
            marker = "06",
            color = Color(0xFF5C6573),
            intent = Intent(android.provider.Settings.ACTION_SETTINGS),
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) {
        ScreenHeader(title = "QUICK TOOLS", onBack = onBack)
        Spacer(Modifier.height(14.dp))
        Text(
            text = "These actions open the apps already installed on your phone.",
            color = Color(0xFFB4B5BD),
            fontSize = 13.sp,
        )
        Spacer(Modifier.height(18.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            items(shortcuts) { shortcut ->
                Button(
                    onClick = { onLaunchIntent(shortcut.intent) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(138.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = shortcut.color,
                        contentColor = Color.White,
                    ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = shortcut.marker,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                        )
                        Column {
                            Text(
                                text = shortcut.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = shortcut.detail,
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.75f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = onOpenCalculator,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
                .height(54.dp),
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB9F36A),
                contentColor = Color(0xFF14200B),
            ),
        ) {
            Text("OPEN CALCULATOR", fontSize = 11.sp, letterSpacing = 1.4.sp)
        }
    }
}

@Composable
fun CalculatorScreen(onBack: () -> Unit) {
    var display by rememberSaveable { mutableStateOf("0") }
    var storedValue by rememberSaveable { mutableStateOf<Double?>(null) }
    var pendingOperator by rememberSaveable { mutableStateOf<String?>(null) }
    var startsNewNumber by rememberSaveable { mutableStateOf(false) }

    fun enter(value: String) {
        when (value) {
            "AC" -> {
                display = "0"
                storedValue = null
                pendingOperator = null
                startsNewNumber = false
            }

            "⌫" -> {
                display = if (display.length <= 1) "0" else display.dropLast(1)
            }

            "±" -> {
                display = formatNumber(-display.toDoubleOrNull().orZero())
            }

            "%" -> {
                display = formatNumber(display.toDoubleOrNull().orZero() / 100.0)
            }

            "+", "−", "×", "÷" -> {
                val current = display.toDoubleOrNull().orZero()
                val previous = storedValue
                val previousOperator = pendingOperator
                storedValue = if (previous != null && previousOperator != null && !startsNewNumber) {
                    calculate(previous, current, previousOperator)
                } else {
                    current
                }
                pendingOperator = value
                display = formatNumber(storedValue ?: current)
                startsNewNumber = true
            }

            "=" -> {
                val left = storedValue
                val operator = pendingOperator
                if (left != null && operator != null) {
                    display = formatNumber(
                        calculate(left, display.toDoubleOrNull().orZero(), operator),
                    )
                    storedValue = null
                    pendingOperator = null
                    startsNewNumber = true
                }
            }

            "." -> {
                if (startsNewNumber) {
                    display = "0."
                    startsNewNumber = false
                } else if (!display.contains('.')) {
                    display += "."
                }
            }

            else -> {
                display = if (startsNewNumber || display == "0") value else display + value
                startsNewNumber = false
            }
        }
    }

    val keys = listOf(
        "AC", "±", "%", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "−",
        "1", "2", "3", "+",
        "⌫", "0", ".", "=",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) {
        ScreenHeader(title = "CALCULATOR", onBack = onBack)
        Spacer(Modifier.weight(1f))
        Text(
            text = pendingOperator?.let { "$it  " }.orEmpty(),
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFB9F36A),
            textAlign = TextAlign.End,
            fontSize = 14.sp,
        )
        Text(
            text = display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 21.dp),
            color = Color(0xFFF4F1E9),
            textAlign = TextAlign.End,
            fontSize = 54.sp,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            fontWeight = FontWeight.Light,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.1f),
            contentPadding = PaddingValues(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            items(keys) { key ->
                val isOperation = key in listOf("÷", "×", "−", "+", "=")
                val isUtility = key in listOf("AC", "±", "%", "⌫")
                Button(
                    onClick = { enter(key) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    shape = RoundedCornerShape(21.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            key == "=" -> Color(0xFFB9F36A)
                            isOperation -> Color(0xFF293333)
                            isUtility -> Color(0xFF30313A)
                            else -> Color(0xFF20212A)
                        },
                        contentColor = if (key == "=") Color(0xFF14200B) else Color(0xFFF4F1E9),
                    ),
                ) {
                    Text(
                        text = key,
                        fontSize = if (key == "AC" || key == "⌫") 15.sp else 21.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) {
        ScreenHeader(title = "WEATHER", onBack = onBack)
        Spacer(Modifier.height(36.dp))
        Text(
            text = "FORECAST PREVIEW",
            color = Color(0xFFB9F36A),
            letterSpacing = 1.8.sp,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "24°",
            color = Color(0xFFF4F1E9),
            fontSize = 82.sp,
            lineHeight = 88.sp,
            fontWeight = FontWeight.Light,
        )
        Text(
            text = "Partly cloudy",
            color = Color(0xFFE0DED7),
            fontSize = 19.sp,
        )
        Text(
            text = "Sample conditions — connect a weather source for live forecasts.",
            modifier = Modifier.padding(top = 8.dp),
            color = Color(0xFF9698A2),
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(34.dp))
        listOf(
            "TODAY" to "24°    Partly cloudy",
            "FRIDAY" to "22°    Light rain",
            "SATURDAY" to "26°    Mostly clear",
        ).forEach { (day, conditions) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(day, color = Color(0xFFB9F36A), fontSize = 11.sp, letterSpacing = 1.4.sp)
                Text(conditions, color = Color(0xFFE0DED7), fontSize = 13.sp)
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun ScreenHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onBack,
            contentPadding = PaddingValues(end = 14.dp),
        ) {
            Text("‹", color = Color(0xFFB9F36A), fontSize = 27.sp)
        }
        Text(
            text = title,
            color = Color(0xFFF4F1E9),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.6.sp,
        )
    }
}

private fun calculate(left: Double, right: Double, operator: String): Double {
    val result = when (operator) {
        "+" -> left + right
        "−" -> left - right
        "×" -> left * right
        "÷" -> if (right == 0.0) Double.NaN else left / right
        else -> right
    }
    return if (result.isFinite()) result else 0.0
}

private fun formatNumber(value: Double): String {
    if (!value.isFinite()) return "0"
    val rounded = kotlin.math.round(value * 1_000_000_000.0) / 1_000_000_000.0
    return if (rounded == rounded.toLong().toDouble()) rounded.toLong().toString()
    else rounded.toString()
}

private fun Double?.orZero(): Double = this ?: 0.0

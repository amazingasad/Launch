package com.metro.launcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metro.launcher.game.Game2048State
import com.metro.launcher.game.MoveDirection
import kotlin.math.abs

@Composable
fun Game2048Screen(
    highScore: Int,
    onHighScore: (Int) -> Unit,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(Game2048State.newGame()) }
    LaunchedEffect(state.score, highScore) {
        if (state.score > highScore) onHighScore(state.score)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 11.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = onBack,
                contentPadding = PaddingValues(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFB9F36A),
                ),
            ) {
                Text("‹", fontSize = 27.sp)
            }
            Text(
                text = "2048",
                color = Color(0xFFF4F1E9),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.6.sp,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Text("SCORE", color = Color(0xFF9698A2), fontSize = 10.sp, letterSpacing = 1.5.sp)
                Text(
                    state.score.toString(),
                    color = Color(0xFFF4F1E9),
                    fontSize = 31.sp,
                    fontWeight = FontWeight.Light,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("BEST", color = Color(0xFF9698A2), fontSize = 10.sp, letterSpacing = 1.5.sp)
                Text(
                    highScore.toString(),
                    color = Color(0xFFB9F36A),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Light,
                )
            }
            Button(
                onClick = { state = Game2048State.newGame() },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB9F36A),
                    contentColor = Color(0xFF14200B),
                ),
            ) {
                Text("NEW GAME", fontSize = 10.sp, letterSpacing = 1.2.sp)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .aspectRatio(1f)
                .background(Color(0xFF292A30), RoundedCornerShape(18.dp))
                .padding(8.dp)
                .pointerInput(state) {
                    var dragX = 0f
                    var dragY = 0f
                    detectDragGestures(
                        onDragEnd = {
                            if (maxOf(abs(dragX), abs(dragY)) > 30f) {
                                val direction = if (abs(dragX) > abs(dragY)) {
                                    if (dragX < 0) MoveDirection.Left else MoveDirection.Right
                                } else {
                                    if (dragY < 0) MoveDirection.Up else MoveDirection.Down
                                }
                                state = state.move(direction)
                            }
                            dragX = 0f
                            dragY = 0f
                        },
                        onDragCancel = {
                            dragX = 0f
                            dragY = 0f
                        },
                        onDrag = { change, amount ->
                            change.consume()
                            dragX += amount.x
                            dragY += amount.y
                        },
                    )
                },
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            itemsIndexed(state.board) { _, value ->
                val tileColor = tileColor(value)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(11.dp),
                    colors = CardDefaults.cardColors(containerColor = tileColor),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (value != 0) {
                            Text(
                                text = value.toString(),
                                color = if (value <= 4) Color(0xFF35312A) else Color(0xFFFCF9F2),
                                fontSize = if (value >= 1024) 18.sp else 22.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = when {
                state.isGameOver() -> "No moves left. Start a new game to play again."
                else -> "Swipe the board or use the controls below."
            },
            modifier = Modifier.padding(top = 15.dp, bottom = 8.dp),
            color = if (state.isGameOver()) Color(0xFFF2A58B) else Color(0xFF9698A2),
            fontSize = 12.sp,
        )

        DirectionPad(
            onMove = { direction -> state = state.move(direction) },
            enabled = !state.isGameOver(),
        )
    }
}

@Composable
private fun DirectionPad(
    onMove: (MoveDirection) -> Unit,
    enabled: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        MoveButton("↑", enabled) { onMove(MoveDirection.Up) }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            MoveButton("←", enabled) { onMove(MoveDirection.Left) }
            MoveButton("↓", enabled) { onMove(MoveDirection.Down) }
            MoveButton("→", enabled) { onMove(MoveDirection.Right) }
        }
    }
}

@Composable
private fun MoveButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.padding(horizontal = 2.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 7.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF272932),
            contentColor = Color(0xFFF4F1E9),
            disabledContainerColor = Color(0xFF20212A),
            disabledContentColor = Color(0xFF777983),
        ),
    ) {
        Text(label, fontSize = 18.sp)
    }
}

private fun tileColor(value: Int): Color = when (value) {
    0 -> Color(0xFF36373E)
    2 -> Color(0xFFECE6D8)
    4 -> Color(0xFFE3D6B9)
    8 -> Color(0xFFE8AB75)
    16 -> Color(0xFFE68B62)
    32 -> Color(0xFFD86B55)
    64 -> Color(0xFFC74E49)
    128 -> Color(0xFFE4CC74)
    256 -> Color(0xFFD8B94F)
    512 -> Color(0xFFC6A532)
    else -> Color(0xFFB9F36A)
}

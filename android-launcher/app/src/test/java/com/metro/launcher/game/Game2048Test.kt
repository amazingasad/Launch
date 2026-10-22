package com.metro.launcher.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class Game2048Test {
    @Test
    fun leftMoveMergesEachPairOnceAndAddsScore() {
        val start = Game2048State(
            board = listOf(
                2, 2, 2, 2,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
            ),
        )

        val result = start.move(MoveDirection.Left, Random(7))

        assertEquals(8, result.score)
        assertEquals(listOf(4, 4, 0, 0), result.board.take(4))
        assertEquals(2, result.board.take(4).count { it == 4 })
        assertEquals(3, result.board.count { it != 0 })
    }

    @Test
    fun noOpMoveDoesNotSpawnATile() {
        val start = Game2048State(
            board = listOf(
                2, 4, 8, 16,
                32, 64, 128, 256,
                512, 1024, 2, 4,
                8, 16, 32, 64,
            ),
        )

        assertEquals(start, start.move(MoveDirection.Left, Random(3)))
    }

    @Test
    fun fullBoardWithNoMatchingNeighborsIsGameOver() {
        val state = Game2048State(
            board = listOf(
                2, 4, 2, 4,
                4, 2, 4, 2,
                2, 4, 2, 4,
                4, 2, 4, 2,
            ),
        )

        assertTrue(state.isGameOver())
    }

    @Test
    fun availableMovePreventsGameOver() {
        val state = Game2048State(
            board = listOf(
                2, 2, 4, 8,
                16, 32, 64, 128,
                256, 512, 1024, 2,
                4, 8, 16, 32,
            ),
        )

        assertFalse(state.isGameOver())
    }
}

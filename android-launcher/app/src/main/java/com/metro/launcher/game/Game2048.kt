package com.metro.launcher.game

import kotlin.random.Random

enum class MoveDirection { Left, Right, Up, Down }

data class Game2048State(
    val board: List<Int> = List(16) { 0 },
    val score: Int = 0,
) {
    fun move(direction: MoveDirection, random: Random = Random.Default): Game2048State {
        val moved = board.toMutableList()
        val lines = when (direction) {
            MoveDirection.Left -> (0..3).map { row -> (0..3).map { row * 4 + it } }
            MoveDirection.Right -> (0..3).map { row -> (3 downTo 0).map { row * 4 + it } }
            MoveDirection.Up -> (0..3).map { col -> (0..3).map { row -> row * 4 + col } }
            MoveDirection.Down -> (0..3).map { col -> (3 downTo 0).map { row -> row * 4 + col } }
        }

        var gained = 0
        lines.forEach { indexes ->
            val compact = indexes.map { board[it] }.filter { it != 0 }
            val merged = mutableListOf<Int>()
            var cursor = 0
            while (cursor < compact.size) {
                if (cursor + 1 < compact.size && compact[cursor] == compact[cursor + 1]) {
                    val value = compact[cursor] * 2
                    merged += value
                    gained += value
                    cursor += 2
                } else {
                    merged += compact[cursor]
                    cursor += 1
                }
            }
            indexes.forEachIndexed { position, index ->
                moved[index] = merged.getOrElse(position) { 0 }
            }
        }

        if (moved == board) return this
        val emptyCells = moved.indices.filter { moved[it] == 0 }
        if (emptyCells.isNotEmpty()) {
            val cell = emptyCells[random.nextInt(emptyCells.size)]
            moved[cell] = if (random.nextInt(10) == 0) 4 else 2
        }
        return Game2048State(moved, score + gained)
    }

    fun isGameOver(): Boolean {
        if (board.any { it == 0 }) return false
        for (row in 0..3) {
            for (column in 0..3) {
                val value = board[row * 4 + column]
                if (column < 3 && value == board[row * 4 + column + 1]) return false
                if (row < 3 && value == board[(row + 1) * 4 + column]) return false
            }
        }
        return true
    }

    companion object {
        fun newGame(random: Random = Random.Default): Game2048State {
            var state = Game2048State()
            repeat(2) {
                val empty = state.board.indices.filter { state.board[it] == 0 }
                val cell = empty[random.nextInt(empty.size)]
                state = state.copy(
                    board = state.board.toMutableList().also {
                        it[cell] = if (random.nextInt(10) == 0) 4 else 2
                    },
                )
            }
            return state
        }
    }
}

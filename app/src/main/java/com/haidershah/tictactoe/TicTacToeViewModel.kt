package com.haidershah.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TicTacToeViewModel : ViewModel() {

    var turn by mutableStateOf(Turn.PLAYER)
        private set

    var gameState by mutableStateOf(GameState.IN_PROGRESS)

    private val _moves = mutableStateListOf(
        Move.PLAYER,
        Move.NO_MOVE,
        Move.COMPUTER,
        Move.NO_MOVE,
        Move.PLAYER,
        Move.COMPUTER,
        Move.NO_MOVE,
        Move.NO_MOVE,
        Move.NO_MOVE
    )
    val moves: List<Move> = _moves

    fun playerMakesMove(positionInMoves: Int) {
        turn = Turn.COMPUTER
        _moves[positionInMoves] = Move.PLAYER
        gameState = checkGameStatus()
    }

    fun computerMakesMove(positionInMoves: Int) {
        turn = Turn.PLAYER
        _moves[positionInMoves] = Move.COMPUTER
        gameState = checkGameStatus()
    }

    fun playerTappedOnBoard(x: Int, y: Int) {
        if (isPlayerTurnAndGameInProgress()) {
            val positionInMoves = y * 3 + x
            if (positionInMoves in moves.indices && moves[positionInMoves] == Move.NO_MOVE) {
                playerMakesMove(positionInMoves)
            }
        }
    }

    fun isPlayerTurnAndGameInProgress() = turn == Turn.PLAYER && gameState == GameState.IN_PROGRESS

    fun resetGame() {
        turn = Turn.PLAYER

        // reset board
        for (i in 0..8) {
            _moves[i] = Move.NO_MOVE
        }

        gameState = GameState.IN_PROGRESS
    }

    fun checkGameStatus(): GameState {
        return if ((moves[0] == Move.PLAYER && moves[1] == Move.PLAYER && moves[2] == Move.PLAYER) ||
            (moves[3] == Move.PLAYER && moves[4] == Move.PLAYER && moves[5] == Move.PLAYER) ||
            (moves[6] == Move.PLAYER && moves[7] == Move.PLAYER && moves[8] == Move.PLAYER) ||
            (moves[0] == Move.PLAYER && moves[3] == Move.PLAYER && moves[6] == Move.PLAYER) ||
            (moves[1] == Move.PLAYER && moves[4] == Move.PLAYER && moves[7] == Move.PLAYER) ||
            (moves[2] == Move.PLAYER && moves[5] == Move.PLAYER && moves[8] == Move.PLAYER) ||
            (moves[0] == Move.PLAYER && moves[4] == Move.PLAYER && moves[8] == Move.PLAYER) ||
            (moves[6] == Move.PLAYER && moves[4] == Move.PLAYER && moves[2] == Move.PLAYER)
        ) {
            GameState.PLAYER_WON
        } else if ((moves[0] == Move.COMPUTER && moves[1] == Move.COMPUTER && moves[2] == Move.COMPUTER) ||
            (moves[3] == Move.COMPUTER && moves[4] == Move.COMPUTER && moves[5] == Move.COMPUTER) ||
            (moves[6] == Move.COMPUTER && moves[7] == Move.COMPUTER && moves[8] == Move.COMPUTER) ||
            (moves[0] == Move.COMPUTER && moves[3] == Move.COMPUTER && moves[6] == Move.COMPUTER) ||
            (moves[1] == Move.COMPUTER && moves[4] == Move.COMPUTER && moves[7] == Move.COMPUTER) ||
            (moves[2] == Move.COMPUTER && moves[5] == Move.COMPUTER && moves[8] == Move.COMPUTER) ||
            (moves[0] == Move.COMPUTER && moves[4] == Move.COMPUTER && moves[8] == Move.COMPUTER) ||
            (moves[6] == Move.COMPUTER && moves[4] == Move.COMPUTER && moves[2] == Move.COMPUTER)
        ) {
            GameState.COMPUTER_WON
        } else {
            var isMoveAvailable = false
            for (i in 0..8) {
                if (moves[i] == Move.NO_MOVE) {
                    isMoveAvailable = true
                    break
                }
            }
            if (isMoveAvailable) {
                GameState.IN_PROGRESS
            } else {
                GameState.DRAW
            }
        }
    }
}

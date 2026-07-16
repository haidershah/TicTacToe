package com.haidershah.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TicTacToeViewModel : ViewModel() {

    var turn by mutableStateOf(Turn.PLAYER)
        private set

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
    }

    fun computerMakesMove(positionInMoves: Int) {
        turn = Turn.PLAYER
        _moves[positionInMoves] = Move.COMPUTER
    }

    fun resetGame() {
        turn = Turn.PLAYER

        // reset board
        for (i in 0..8) {
            _moves[i] = Move.NO_MOVE
        }
    }
}

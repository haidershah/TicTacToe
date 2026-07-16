package com.haidershah.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TicTacToeViewModel : ViewModel() {

    var turn by mutableStateOf(Turn.PLAYER)
        private set

    fun playerMakesMove() {
        turn = Turn.COMPUTER
    }

    fun computerMakesMove() {
        turn = Turn.PLAYER
    }

    fun resetGame() {
        turn = Turn.PLAYER
    }
}

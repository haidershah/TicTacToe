package com.haidershah.tictactoe

enum class Turn {
    PLAYER,
    COMPUTER
}

enum class Move {
    PLAYER,
    COMPUTER,
    NO_MOVE
}

enum class GameState {
    IN_PROGRESS,
    PLAYER_WON,
    COMPUTER_WON,
    DRAW,
}

package com.haidershah.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.haidershah.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TicTacToeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToeScreen(modifier: Modifier) {
    /**
     * true - player's turn
     * false - computer's turn
     */
    val playerTurn = remember { mutableStateOf(true) }

    /**
     * true - player move
     * false - computer move
     * null - no move
     */
    val moves =
        remember { mutableStateListOf(true, null, false, null, true, false, null, null, null) }

    Header(playerTurn.value, modifier)
//    Board(moves)
}

@Composable
fun Header(playerTurn: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val playerBoxColor = if (playerTurn) Color.Cyan else Color.LightGray
        val computerBoxColor = if (playerTurn) Color.LightGray else Color.Red

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(playerBoxColor)
        ) {
            Text(
                text = "Player", modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(computerBoxColor)
        ) {
            Text(
                text = "Computer", modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun Board(moves: List<Boolean?>) {

}
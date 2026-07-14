package com.haidershah.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haidershah.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

enum class Turn {
    PLAYER,
    COMPUTER
}

enum class GameState {
    IN_PROGRESS,
    PLAYER_WON,
    COMPUTER_WON,
    DRAW,
}

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
    val turn = remember { mutableStateOf(Turn.PLAYER) }

    /**
     * true - player move
     * false - computer move
     * null - no move
     */
    val moves =
        remember { mutableStateListOf(true, null, false, null, true, false, null, null, null) }

    val gameState = remember { mutableStateOf(GameState.IN_PROGRESS) }

    val onTap: (Int, Int) -> Unit = { x, y ->
        // player's turn and game is in-progress
        if (turn.value == Turn.PLAYER && gameState.value == GameState.IN_PROGRESS) {
            val positionInMoves = y * 3 + x
            if (positionInMoves in moves.indices && moves[positionInMoves] == null) {
                moves[positionInMoves] = true
                turn.value = Turn.COMPUTER
                gameState.value = getGameState(moves)
            }
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.title_tic_tac_toe),
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )
        Header(turn.value)
        Board(moves, onTap)

        // computer's turn and game is in-progress
        if (turn.value == Turn.COMPUTER && gameState.value == GameState.IN_PROGRESS) {
            CircularProgressIndicator(color = Color.Red, modifier = Modifier.padding(16.dp))

            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit) {
                coroutineScope.launch {
                    delay(1500.milliseconds)
                    while (true) {
                        val index = Random.nextInt(9)
                        if (moves[index] == null) {
                            moves[index] = false
                            turn.value = Turn.PLAYER
                            gameState.value = getGameState(moves)
                            break
                        }
                    }
                }
            }
        }

        when (gameState.value) {
            GameState.PLAYER_WON -> Text(
                text = stringResource(R.string.message_player_won),
                fontSize = 25.sp
            )

            GameState.COMPUTER_WON -> Text(
                text = stringResource(R.string.message_computer_won),
                fontSize = 30.sp
            )

            GameState.DRAW -> Text(text = stringResource(R.string.message_draw), fontSize = 30.sp)

            GameState.IN_PROGRESS -> {}
        }
    }
}

fun getGameState(moves: List<Boolean?>): GameState {
    return if ((moves[0] == true && moves[1] == true && moves[2] == true) ||
        (moves[3] == true && moves[4] == true && moves[5] == true) ||
        (moves[6] == true && moves[7] == true && moves[8] == true) ||
        (moves[0] == true && moves[3] == true && moves[6] == true) ||
        (moves[1] == true && moves[4] == true && moves[7] == true) ||
        (moves[2] == true && moves[5] == true && moves[8] == true) ||
        (moves[0] == true && moves[4] == true && moves[8] == true) ||
        (moves[6] == true && moves[4] == true && moves[2] == true)
    ) {
        GameState.PLAYER_WON
    } else if ((moves[0] == false && moves[1] == false && moves[2] == false) ||
        (moves[3] == false && moves[4] == false && moves[5] == false) ||
        (moves[6] == false && moves[7] == false && moves[8] == false) ||
        (moves[0] == false && moves[3] == false && moves[6] == false) ||
        (moves[1] == false && moves[4] == false && moves[7] == false) ||
        (moves[2] == false && moves[5] == false && moves[8] == false) ||
        (moves[0] == false && moves[4] == false && moves[8] == false) ||
        (moves[6] == false && moves[4] == false && moves[2] == false)
    ) {
        GameState.COMPUTER_WON
    } else {
        var isMoveAvailable = false
        for (i in 0..8) {
            if (moves[i] == null) {
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

@Composable
fun Header(turn: Turn) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val playerBoxColor = if (turn == Turn.PLAYER) Color.Blue else Color.LightGray
        val computerBoxColor = if (turn == Turn.COMPUTER) Color.Red else Color.LightGray

        val playerFontColor = if (turn == Turn.PLAYER) Color.White else Color.Black

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(playerBoxColor)
        ) {
            Text(
                text = "Player",
                color = playerFontColor, modifier = Modifier
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
fun Board(moves: List<Boolean?>, onTap: (Int, Int) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(32.dp)
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    val cellWidth = size.width / 3f
                    val cellHeight = size.height / 3f
                    val x = (offset.x / cellWidth).toInt().coerceIn(0, 2)
                    val y = (offset.y / cellHeight).toInt().coerceIn(0, 2)
                    onTap(x, y)
                })
            }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {}
            Row(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {}
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.Black)
            ) { }
            Column(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.Black)
            ) { }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        val index = i * 3 + j
                        val move = moves[index]

                        Column(modifier = Modifier.weight(1f)) {
                            GetComposableFromMove(move)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GetComposableFromMove(move: Boolean?) {
    when (move) {
        true -> Image(
            painter = painterResource(R.drawable.ic_x),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(Color.Blue)
        )

        false -> Image(
            painter = painterResource(R.drawable.ic_o),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(Color.Red)
        )

        null -> Image(
            painter = painterResource(R.drawable.ic_null),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

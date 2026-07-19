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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haidershah.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class TicTacToeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TicTacToeTopAppBar() }
                ) { innerPadding ->
                    TicTacToeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeTopAppBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.title_tic_tac_toe)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                3.dp
            )
        )
    )
}

@Composable
fun TicTacToeScreen(modifier: Modifier, viewModel: TicTacToeViewModel = viewModel()) {
    val onTap: (Int, Int) -> Unit = { x, y ->
        viewModel.playerTappedOnBoard(x, y)
    }

    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(viewModel.turn)
        Board(viewModel.moves, onTap)

        // computer's turn and game is in-progress
        if (viewModel.isComputerTurnAndGameInProgress()) {
            CircularProgressIndicator(color = Color.Red, modifier = Modifier.padding(16.dp))

            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit) {
                coroutineScope.launch {
                    delay(1500.milliseconds)
                    while (true) {
                        val index = Random.nextInt(9)
                        if (viewModel.moves[index] == Move.NO_MOVE) {
                            viewModel.computerMakesMove(index)
                            break
                        }
                    }
                }
            }
        }

        when (viewModel.gameState) {
            GameState.PLAYER_WON -> Text(
                text = stringResource(R.string.message_player_won),
                fontSize = 30.sp
            )

            GameState.COMPUTER_WON -> Text(
                text = stringResource(R.string.message_computer_won),
                fontSize = 30.sp
            )

            GameState.DRAW -> Text(text = stringResource(R.string.message_draw), fontSize = 30.sp)

            GameState.IN_PROGRESS -> {}
        }

        // game is finished
        if (viewModel.gameState != GameState.IN_PROGRESS) {
            Button(modifier = Modifier.padding(16.dp), onClick = {
                viewModel.resetGame()
            }) {
                Text(
                    text = stringResource(R.string.button_start_over)
                )
            }
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
fun Board(moves: List<Move>, onTap: (Int, Int) -> Unit) {
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
fun GetComposableFromMove(move: Move) {
    when (move) {
        Move.PLAYER -> Image(
            painter = painterResource(R.drawable.ic_x),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(Color.Blue)
        )

        Move.COMPUTER -> Image(
            painter = painterResource(R.drawable.ic_o),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(Color.Red)
        )

        Move.NO_MOVE -> Image(
            painter = painterResource(R.drawable.ic_null),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

package com.example.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.timefighter.ui.theme.TimeFighterTheme

class MainActivity : ComponentActivity() {
    lateinit var gameScoreTextView: TextView
    lateinit var timeLeftTextView: TextView
    lateinit var tapMeButton: Button
    lateinit var killGameButton: Button

    private  var gameStarted = false

    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60


    private var score = 0
    // 2
    override fun onCreate(savedInstanceState: Bundle?) {
        // 3
        super.onCreate(savedInstanceState)
        // 4
        setContentView(R.layout.activity_main)

        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)
        killGameButton = findViewById(R.id.kill_game_button)

        tapMeButton.setOnClickListener { incrementScore() }

        killGameButton.setOnClickListener { endGame() }

        resetGame()
    }

    private fun incrementScore(){

        if (!gameStarted){
            startGame()
        }

        score++

        val newScore= getString(R.string.your_score, score)

        gameScoreTextView.text = newScore

    }

    private fun resetGame(){

        score =0



        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore // update state

        val initialTimeLeft = getString(R.string.time_left, 60)
        timeLeftTextView.text = initialTimeLeft

        countDownTimer = object  : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt()/1000

                val timeLeftString = getString(R.string.time_left, timeLeft)

                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
               endGame()
            }


        }
    }
    private fun startGame(){
        countDownTimer.start()
        gameStarted= true


    }
    private fun endGame(){

        Toast.makeText(this, getString(R.string.game_finished_score, score), Toast.LENGTH_LONG).show()

        gameStarted=false

        resetGame()

    }
}


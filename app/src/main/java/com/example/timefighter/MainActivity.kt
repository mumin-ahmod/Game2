package com.example.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.nfc.Tag
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
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

class MainActivity : AppCompatActivity() {
    lateinit var gameScoreTextView: TextView
    lateinit var timeLeftTextView: TextView
    lateinit var tapMeButton: Button
    lateinit var killGameButton: Button

    private var gameStarted = false

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

        Log.d("TAG", "On Create Called, score: $score")
        //resetGame() instead we will call saveinstance and then call reset

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d("TAG", "SaveInstance Called, score: $score")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("TAG", "OnDestroy Called, score: $score")
    }

    private fun incrementScore() {

        if (!gameStarted) {
            startGame()
        }

        score++

        val newScore = getString(R.string.your_score, score)

        gameScoreTextView.text = newScore

    }

    private fun resetGame() {

        score = 0

        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore // update state

        val initialTimeLeft = getString(R.string.time_left, 60)
        timeLeftTextView.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left, timeLeft)

                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }


        }
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true


    }

    private fun endGame() {

        Toast.makeText(this, getString(R.string.game_finished_score, score), Toast.LENGTH_LONG)
            .show()

        gameStarted = false
        countDownTimer.cancel()
        resetGame()

    }

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score, score)
        gameScoreTextView.text = restoredScore

        val restoredTime = getString(R.string.time_left, timeLeft)
        timeLeftTextView.text = restoredTime

        countDownTimer = object : CountDownTimer(
            (timeLeft *
                    1000).toLong(), countDownInterval
        ) {
            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(
                    R.string.time_left,
                    timeLeft
                )
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }
}


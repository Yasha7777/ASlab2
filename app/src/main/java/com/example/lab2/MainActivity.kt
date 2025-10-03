package com.example.lab2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.content.Intent
import android.widget.Toast
import android.widget.ImageButton


class MainActivity : AppCompatActivity() {

    private var playerScore = 0
    private var computerScore = 0
    private var winPoints = 5

    private lateinit var textResult: TextView
    private lateinit var textPlayerScore: TextView
    private lateinit var textComputerScore: TextView
    private lateinit var buttonPlayAgain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        winPoints = prefs.getInt("winPoints", 5)

        initViews()
        updateScoreDisplay()
        setupClickListeners()
    }

    private fun initViews() {
        try {
            textResult = findViewById(R.id.textResult)
            textPlayerScore = findViewById(R.id.textPlayerScore)
            textComputerScore = findViewById(R.id.textComputerScore)
            buttonPlayAgain = findViewById(R.id.buttonPlayAgain)

            findViewById<Button>(R.id.buttonSettings).setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }

            buttonPlayAgain.setOnClickListener {
                resetGame()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка инициализации интерфейса", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.buttonRock).setOnClickListener { playGame("rock") }
        findViewById<ImageButton>(R.id.buttonPaper).setOnClickListener { playGame("paper") }
        findViewById<ImageButton>(R.id.buttonScissors).setOnClickListener { playGame("scissors") }
    }

    private fun playGame(playerChoice: String) {
        if (playerScore >= winPoints || computerScore >= winPoints) {
            return
        }

        val choices = listOf("rock", "paper", "scissors")
        val computerChoice = choices[Random.nextInt(choices.size)]

        val result = determineWinner(playerChoice, computerChoice)
        updateResultText(result, playerChoice, computerChoice)

        when (result) {
            GameResult.PLAYER_WIN -> playerScore++
            GameResult.COMPUTER_WIN -> computerScore++
            GameResult.TIE -> {}
        }

        updateScoreDisplay()

        if (playerScore >= winPoints) {
            textResult.text = getString(R.string.player_wins)
            showPlayAgainButton()
        } else if (computerScore >= winPoints) {
            textResult.text = getString(R.string.computer_wins)
            showPlayAgainButton()
        }
    }

    private fun determineWinner(player: String, computer: String): GameResult {
        return when {
            player == computer -> GameResult.TIE
            (player == "rock" && computer == "scissors") ||
                    (player == "scissors" && computer == "paper") ||
                    (player == "paper" && computer == "rock") -> GameResult.PLAYER_WIN
            else -> GameResult.COMPUTER_WIN
        }
    }

    private fun updateResultText(result: GameResult, player: String, computer: String) {
        val resultText = when (result) {
            GameResult.PLAYER_WIN -> "Вы: $player vs Комп: $computer → Вы выиграли!"
            GameResult.COMPUTER_WIN -> "Вы: $player vs Комп: $computer → Компьютер выиграл!"
            GameResult.TIE -> "Вы: $player vs Комп: $computer → Ничья!"
        }
        textResult.text = resultText
    }

    private fun updateScoreDisplay() {
        textPlayerScore.text = getString(R.string.player_score, playerScore)
        textComputerScore.text = getString(R.string.computer_score, computerScore)
    }

    private fun showPlayAgainButton() {
        buttonPlayAgain.visibility = android.view.View.VISIBLE
    }

    private fun resetGame() {
        playerScore = 0
        computerScore = 0
        buttonPlayAgain.visibility = android.view.View.GONE
        textResult.text = getString(R.string.result_text)
        updateScoreDisplay()
    }
}
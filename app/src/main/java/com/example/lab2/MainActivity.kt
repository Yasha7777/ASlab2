package com.example.lab2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab2.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var playerScore = 0
    private var computerScore = 0
    private var winPoints = 5

    companion object {
        private val choices = listOf("rock", "paper", "scissors")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        winPoints = prefs.getInt("winPoints", 5)

        updateScoreDisplay()
        setupClickListeners()
    }

    private fun setupClickListeners() = with(binding) {
        buttonRock.setOnClickListener { playGame("rock") }
        buttonPaper.setOnClickListener { playGame("paper") }
        buttonScissors.setOnClickListener { playGame("scissors") }

        buttonSettings.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }

        buttonPlayAgain.setOnClickListener { resetGame() }
    }

    private fun playGame(playerChoice: String) {
        if (playerScore >= winPoints || computerScore >= winPoints) return

        val computerChoice = choices.random()
        val result = determineWinner(playerChoice, computerChoice)

        binding.textResult.text = when (result) {
            GameResult.PLAYER_WIN -> "Вы: $playerChoice vs Комп: $computerChoice → 🎉 Победа!"
            GameResult.COMPUTER_WIN -> "Вы: $playerChoice vs Комп: $computerChoice → 🤖 Компьютер выиграл!"
            GameResult.TIE -> "Вы: $playerChoice vs Комп: $computerChoice → 🤝 Ничья!"
        }

        when (result) {
            GameResult.PLAYER_WIN -> playerScore++
            GameResult.COMPUTER_WIN -> computerScore++
            GameResult.TIE -> {}
        }

        updateScoreDisplay()
        checkForWinner()
    }

    private fun determineWinner(player: String, computer: String): GameResult =
        when {
            player == computer -> GameResult.TIE
            (player == "rock" && computer == "scissors") ||
                    (player == "scissors" && computer == "paper") ||
                    (player == "paper" && computer == "rock") -> GameResult.PLAYER_WIN
            else -> GameResult.COMPUTER_WIN
        }

    private fun updateScoreDisplay() = with(binding) {
        textPlayerScore.text = getString(R.string.player_score, playerScore)
        textComputerScore.text = getString(R.string.computer_score, computerScore)
    }

    private fun checkForWinner() = with(binding) {
        when {
            playerScore >= winPoints -> {
                textResult.text = getString(R.string.player_wins)
                buttonPlayAgain.show()
            }
            computerScore >= winPoints -> {
                textResult.text = getString(R.string.computer_wins)
                buttonPlayAgain.show()
            }
        }
    }

    private fun resetGame() = with(binding) {
        playerScore = 0
        computerScore = 0
        textResult.text = getString(R.string.result_text)
        buttonPlayAgain.hide()
        updateScoreDisplay()
    }

    // 👇 удобные расширения
    private fun android.view.View.show() { visibility = android.view.View.VISIBLE }
    private fun android.view.View.hide() { visibility = android.view.View.GONE }
}

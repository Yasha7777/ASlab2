package com.example.lab2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab2.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        val currentWinPoints = prefs.getInt("winPoints", 5)
        binding.editWinPoints.setText(currentWinPoints.toString())

        binding.buttonSave.setOnClickListener {
            val input = binding.editWinPoints.text.toString().trim()
            val points = input.toIntOrNull()

            when {
                input.isEmpty() -> showToast("Введите число")
                points == null || points <= 0 -> showToast("Введите положительное число")
                points > 100 -> showToast("Слишком большое число (макс. 100)")
                else -> {
                    prefs.edit().putInt("winPoints", points).apply()
                    showToast("Настройки сохранены ✅")
                    finish()
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

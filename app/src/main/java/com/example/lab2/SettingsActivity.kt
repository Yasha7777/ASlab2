package com.example.lab2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val editWinPoints = findViewById<EditText>(R.id.editWinPoints)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        // Загрузка текущего значения
        val prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        val currentWinPoints = prefs.getInt("winPoints", 5)
        editWinPoints.setText(currentWinPoints.toString())

        buttonSave.setOnClickListener {
            val input = editWinPoints.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(this, "Введите число", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val points = input.toIntOrNull()
            if (points == null || points <= 0) {
                Toast.makeText(this, "Введите положительное число", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit().putInt("winPoints", points).apply()
            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
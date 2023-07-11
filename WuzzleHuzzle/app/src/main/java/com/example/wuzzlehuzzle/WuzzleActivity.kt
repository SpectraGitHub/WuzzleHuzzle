package com.example.wuzzlehuzzle

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class WuzzleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wuzzle)

        val imagePath = intent.getStringExtra("imagePath")
        val language = intent.getStringExtra("language")
        val level = intent.getIntExtra("level", 1)
        val wuzzleNumber = intent.getIntExtra("wuzzleNumber", 0)

        val img = assets.open(imagePath!!)
        val drawable = Drawable.createFromStream(img, null);

        val wuzzleDisplay = findViewById<ImageView>(R.id.wuzzleImage)
        wuzzleDisplay.setImageDrawable(drawable)

        var chosenLanguage = GLOBALGAMEDATA.norwegianLevels
        if (language == "english") chosenLanguage = GLOBALGAMEDATA.englishLevels

        var levelWuzzle = chosenLanguage.get(level)
        val wuzzle = levelWuzzle.wuzzles.get(wuzzleNumber)

        val submitButton = findViewById<Button>(R.id.submitGuess)
        submitButton.setOnClickListener() {
            val guess = findViewById<EditText>(R.id.guessInput).text.toString()
            if (submitGuess(guess, wuzzle)) {
                Toast.makeText(this, "CORRECT ANSWER! WELL DONE", Toast.LENGTH_SHORT).show()
                levelWuzzle.finishedWuzzles = levelWuzzle.finishedWuzzles + 1;
                if (levelWuzzle.finishedWuzzles >= 4) {
                    if (chosenLanguage.size > level) {
                        chosenLanguage[level + 1].locked = false
                    }
                }
            } else {
                Toast.makeText(this, "Incorrect answer, try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun submitGuess(guess : String, wuzzle : Wuzzle) : Boolean {
        var checkGuess = guess.replace(" ", "").lowercase()
        checkGuess = checkGuess.replace("æ", "ae")
        checkGuess = checkGuess.replace("å", "a")
        checkGuess = checkGuess.replace("ø", "o")

        if (checkGuess.lowercase() in wuzzle.answers) {
            guessedCorrectly(wuzzle)
            return true
        }
        return false
    }

    private fun guessedCorrectly(wuzzle: Wuzzle) {
        setWuzzleCompleted(wuzzle) // Save the wuzzle as completed
        finish() // Finish the activity, which returns the user to the last activity
    }

    private fun setWuzzleCompleted(wuzzle: Wuzzle) {
        wuzzle.finished = true
    }
}
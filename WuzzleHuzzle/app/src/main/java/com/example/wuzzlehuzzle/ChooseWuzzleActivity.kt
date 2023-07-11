package com.example.wuzzlehuzzle

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible

class ChooseWuzzleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_wuzzle)

        val language = intent.getStringExtra("language")
        val level = intent.getStringExtra("level")

        // Check that the language and level is set
        if (level == null || language == null) {
            return
        }

        var title = findViewById<TextView>(R.id.levelTitle)
        title.text = level.replace("Level", "Level ")

        setWuzzles(language, level)

    }

    fun setWuzzles(language: String, level: String) {
        val table =  findViewById<TableLayout>(R.id.levelTable)
        var index = 0
        val levelInt = (level.drop(5).toInt()) - 1


        var chosenLanguage = GLOBALGAMEDATA.norwegianLevels
        if (language == "english") chosenLanguage = GLOBALGAMEDATA.englishLevels
        val chosenLevel = chosenLanguage[levelInt]
        var currentWuzzle: Wuzzle = chosenLevel.wuzzles[index]

        table.forEach {
            if (it is TableRow) {
                it.forEach {
                    if (it is ConstraintLayout) {
                        it.forEach {
                            if(it is ImageButton) {
                                val currentIndex = index
                                currentWuzzle = chosenLevel.wuzzles[index]
                                val img = assets.open(currentWuzzle.filename)
                                val drawable = Drawable.createFromStream(img, null);
                                it.setImageDrawable(drawable) // Set wuzzleimage

                                val clickableWuzzle = currentWuzzle
                                it.setOnClickListener { // Wait for click
                                    openWuzzleActivity(clickableWuzzle.filename, language, levelInt, currentIndex)
                                }
                                index++;
                            } else if (it is ImageView) { // If it is a checkmark
                                if (currentWuzzle.finished) { // If the corresponding wuzzle is finished
                                    it.visibility = View.VISIBLE // Show checkmark to mark the wuzzle as finished
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private fun openWuzzleActivity(imagePath : String, language : String, level: Int, wuzzleNumber: Int) {
        val intent = Intent(this, WuzzleActivity::class.java)
        intent.putExtra("imagePath", imagePath)
        intent.putExtra("language", language)
        intent.putExtra("level", level)
        intent.putExtra("wuzzleNumber", wuzzleNumber)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setWuzzles(intent.getStringExtra("language")!!, intent.getStringExtra("level")!!)
    }
}


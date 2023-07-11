package com.example.wuzzlehuzzle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.lang.Exception


class Wuzzle : Serializable {
    var filename = ""
    var answers: List<String>
    var finished = false

    constructor(ans: List<String>) {
        answers = ans
    }
}

class Level: Serializable {
    lateinit var wuzzles: MutableList<Wuzzle>
    val finishedWuzzles = 0
    val locked = false

    constructor(newWuzz : Wuzzle) {
        wuzzles = mutableListOf(newWuzz)
    }
}

class GameData() : Serializable {
    var norwegianLevels : MutableList<Level> = mutableListOf()
    var englishLevels : MutableList<Level> = mutableListOf()

    fun readGameData(context: Context) {
        norwegianLevels.add(Level(Wuzzle(listOf("hei", "Pia"))))
        // Read gamedata from file in assets
        val inputStream = context.assets.open("gameData.txt")

        val size = inputStream.available()
        val buffer = ByteArray(size)

        inputStream.read(buffer)

        val list = String(buffer).split("\n")
        var language = ""
        var level = 0
        var wuzzlenum = 0

        for(i in list) {
            var line = i.dropLast(1)
            if(line.contains("NORWEGIAN") || line.contains("ENGLISH")) {
                language = line
                continue
            }

            // If the line indicates a new level
            if(line.contains("LEVEL")) {
                // Set the level to 1 less (used as an index later)
                level = (line.drop(5).toInt()) - 1
                println("LEVEL:" + level)
                wuzzlenum = 0   // Set the wuzzlenumber to be the first of the level
                continue
            }

            // Split the line at each space to get all the possible answers
            val allAns = line.split(" ")

            // If it is the first wuzzle of the level, add a new level using the level constructor
            if (wuzzlenum == 0) {
                println("Wuzzlenum 0")
                if (language == "NORWEGIAN") {
                    this.norwegianLevels.add(Level(Wuzzle(allAns)))
                } else if (language == "ENGLISH") {
                    this.englishLevels.add(Level(Wuzzle(allAns)))
                }
            } else { // If it is not the first wuzzle of the level, add the wuzzle to the existing level
                if (language == "NORWEGIAN") {
                    this.norwegianLevels[level].wuzzles.add(Wuzzle(allAns))
                } else if (language == "ENGLISH") {
                    this.englishLevels[level].wuzzles.add(Wuzzle(allAns))
                }
            }
            wuzzlenum++
        }
    }


}

/**
 * Saves the gameData 'norwegianLevels' and 'englishLevels' to local storage
 * @return Boolean - true on successful write
 */
fun saveGameDataToFile(context: Context) {
    val localFileName = "gameData.txt"

    val f = FileOutputStream(File(context.filesDir, localFileName))
    val fos = ObjectOutputStream(f)
    fos.writeObject(GLOBALGAMEDATA)
    fos.flush()
    fos.close()
}

/**
 * Loads the gameData 'norwegianLevels' and 'englishLevels' from local storage
 */
fun readGameDataFromFile(context: Context) {
    val localFileName = "gameData.txt"
    try {
        val f = File(context.filesDir, localFileName)
        val fis = FileInputStream(f)
        val ois = ObjectInputStream(fis)

        var gameData = ois.readObject() as? GameData
        if (gameData != null) { // Only set data if successfully read
            GLOBALGAMEDATA = gameData
        }
    } catch (ex: Exception) {
        GLOBALGAMEDATA.readGameData(context)
        println("Failed to read from file")
    }

}

var GLOBALGAMEDATA = GameData()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GLOBALGAMEDATA.readGameData(this.baseContext)

        saveGameDataToFile(this.baseContext)

        readGameDataFromFile(this.baseContext)
        println("------------GAME DATA:-------------")
        println(GLOBALGAMEDATA.englishLevels.forEach{
            it.wuzzles.forEach{
                println(it.answers.toString())
            }
        })



        // Norwegian onClick listener
        val norwegian_flag = findViewById<ImageView>(R.id.norwegian_flag)
        val norwegian_text = findViewById<TextView>(R.id.norwegian_text)
        norwegian_flag.setOnClickListener { playNorwegianWuzzle() }
        norwegian_text.setOnClickListener { playNorwegianWuzzle() }


        // English onClick listener
        val english_flag = findViewById<ImageView>(R.id.english_flag)
        val english_text = findViewById<TextView>(R.id.english_text)
        english_flag.setOnClickListener { playEnglishWuzzle() }
        english_text.setOnClickListener { playEnglishWuzzle() }


    }

    fun playNorwegianWuzzle() {
        val intent = Intent(this, ChooseLevelActivity::class.java)
        intent.putExtra("language", "norwegian")
        startActivity(intent)
    }

    fun playEnglishWuzzle() {
        val intent = Intent(this, ChooseLevelActivity::class.java)
        intent.putExtra("language", "english")
        startActivity(intent)
    }
}
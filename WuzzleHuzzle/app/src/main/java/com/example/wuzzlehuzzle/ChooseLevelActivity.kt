package com.example.wuzzlehuzzle

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_choose_level.*
import org.xmlpull.v1.XmlPullParserFactory


class ChooseLevelActivity : AppCompatActivity() {
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ChooseLevelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_level)

        val language = intent.getStringExtra("language")!!

        // Add layout manager etc.
        layoutManager = LinearLayoutManager(this)
        levelsRecyclerView.layoutManager = layoutManager

        setLevels(language)
    }

    fun setLevels(language : String) {
        val assetList = assets.list(language)
        var chosenLanguage = GLOBALGAMEDATA.norwegianLevels
        if (language == "english") {
            chosenLanguage = GLOBALGAMEDATA.englishLevels
        }

        var levels: ArrayList<Int> = arrayListOf()
        assetList?.indices?.map { levels.add(it+1) }
        var scores = setScores(chosenLanguage)
        var numberOfWuzzles = arrayListOf<Int>()
        var lockedArray = arrayListOf<Boolean>()
        for(level in chosenLanguage) {
            numberOfWuzzles.add(level.wuzzles.size)
            lockedArray.add(level.locked)
        }


        // Initiate recyclerview
        adapter = ChooseLevelAdapter(levels, scores, numberOfWuzzles, lockedArray, language)
        levelsRecyclerView.adapter = adapter
    }

    fun setScores(chosenLanguage: MutableList<Level>) : ArrayList<Int> {

        var scoreList = arrayListOf<Int>()
        for (level in chosenLanguage) {
            scoreList.add(level.finishedWuzzles)
        }

        return scoreList
    }

    override fun onResume() {
        super.onResume()
        setLevels(intent.getStringExtra("language")!!)
    }
}
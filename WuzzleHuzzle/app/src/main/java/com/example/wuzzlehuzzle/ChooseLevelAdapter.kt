package com.example.wuzzlehuzzle

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_level_row.view.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory


class ChooseLevelAdapter(private val levels: ArrayList<Int>, private val scores: ArrayList<Int>, private val wuzzles: ArrayList<Int>, private val lockedArray: ArrayList<Boolean>, private val language: String) : RecyclerView.Adapter <ChooseLevelAdapter.ChooseLevelHolder>() {
    class ChooseLevelHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view: View = v
        var level: Int = 0
        var score: Int = 0
        var wuzzle: Int = 0

        fun bindLevel(level: Int, score: Int, wuzzle: Int) {
            this.level = level
            view.level.text = level.toString()
            this.score = score
            view.wuzzlesCompleted.text = score.toString()
            this.wuzzle = wuzzle
            view.wuzzlesTotal.text = "/${wuzzle}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseLevelHolder {
        return ChooseLevelHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_level_row, parent, false))
    }

    override fun onBindViewHolder(holder: ChooseLevelHolder, position: Int) {
        var level = levels[position]
        var score = scores[position]
        var wuzzle = wuzzles[position]
        var locked = lockedArray[position]
        holder.bindLevel(level, score, wuzzle)
        if(locked) {
            holder.itemView.setBackgroundColor(0xFFB900)
        }
        holder.itemView.setOnClickListener { goToChooseWuzzleActivity(it, locked) }
    }

    override fun getItemCount(): Int {
        return levels.count()
    }

    private fun goToChooseWuzzleActivity(view: View, locked : Boolean) {

        if (locked) {
            Toast.makeText(view.context, "This level is locked. Complete 4 wuzzles from previous level to unlock", Toast.LENGTH_LONG).show()
            return
        }
        println("GOING TO:" + view.parent)

        val parent: RecyclerView = view.parent as RecyclerView

        if (parent !is RecyclerView) {
            println("ERROR (goToChooseWuzzleActivity) should be of type recyclerview, is type: " + parent.javaClass.name)
        }

        val position = parent.indexOfChild(view)
        val level = levels[position]

        println("GO to choose wuzzle activity. Language:$language, level: $level")

        val intent = Intent(view.context, ChooseWuzzleActivity::class.java)
        intent.putExtra("level", "Level$level")
        intent.putExtra("language", language)
        startActivity(view.context, intent, null)
    }

}
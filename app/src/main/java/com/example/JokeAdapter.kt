package com.example

import android.app.Activity
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JokeAdapter(private val jokes: List<Joke>, private val activity: Activity) : RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        return JokeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_joke,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return jokes.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(jokes[position])
    }

    inner class JokeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val jokeTextView = view.findViewById<TextView>(R.id.joke_text)
        private val jokeTagsView = view.findViewById<LinearLayout>(R.id.tags_list)
        fun bind(joke: Joke) {
            jokeTextView.text = joke.joke_text
            jokeTagsView.removeAllViews()
            joke.tags.split(",").forEach {
                val textView = TextView(activity)
                val layoutParams = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(5,0,5,0)
                textView.layoutParams = layoutParams
                textView.text = it.trim()
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundResource(R.drawable.rounded_backdround)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
                jokeTagsView.addView(textView)
            }
        }
    }
}
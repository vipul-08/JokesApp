package com.example

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.JsonObject



class MainActivity : AppCompatActivity() {

    lateinit var list: MutableList<Joke>
    lateinit var jokesAdapter: JokeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.jokes_list)
        val mainProgressBar = findViewById<ProgressBar>(R.id.mainProgressBar)

        list = mutableListOf<Joke>()
        jokesAdapter = JokeAdapter(list, this)
        recyclerView.adapter = jokesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitClient.instance.getJokes()
            .enqueue(object: Callback<List<Joke>> {
                override fun onFailure(call: Call<List<Joke>>, t: Throwable) {
                    mainProgressBar.visibility = GONE
                    Toast.makeText(applicationContext, "Unable to get Jokes!", LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<List<Joke>>, response: Response<List<Joke>>) {
                    mainProgressBar.visibility = GONE
                    if (response.isSuccessful) {
                        val jokes = response.body()!!
                        list.addAll(jokes)
                        jokesAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(applicationContext, "Unable to get Jokes!", LENGTH_SHORT).show()
                    }
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_joke -> {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.add_joke_dialog)
                dialog.setCancelable(false)
                val jokeTextInput = dialog.findViewById<TextInputEditText>(R.id.enter_joke)
                val jokeTagsInput = dialog.findViewById<TextInputEditText>(R.id.enter_tags)
                val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar)

                val okButton = dialog.findViewById<Button>(R.id.ok_btn)
                val cancelButton = dialog.findViewById<Button>(R.id.cancel_btn)

                okButton.setOnClickListener {
                    progressBar.visibility = VISIBLE
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("joke_text", jokeTextInput.text.toString())
                    jsonObject.addProperty("joke_tags", jokeTagsInput.text.toString())
                    RetrofitClient.instance.addJoke(jsonObject)
                        .enqueue(object: Callback<JsonObject> {
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                progressBar.visibility = GONE
                                Toast.makeText(applicationContext, "Unable to add Joke!", LENGTH_SHORT).show()
                                dialog.dismiss()
                            }

                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                progressBar.visibility = GONE
                                dialog.dismiss()
                                if(response.isSuccessful) {
                                    list.add(Joke(-1, jokeTextInput.text.toString(), jokeTagsInput.text.toString()))
                                    jokesAdapter.notifyDataSetChanged()
                                    Toast.makeText(applicationContext, "Joke Added!", LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext, "Unable to add Joke!", LENGTH_SHORT).show()
                                }
                            }

                        })

                }
                cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

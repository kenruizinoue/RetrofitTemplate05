package com.kenruizinoue.retrofittemplate05.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kenruizinoue.retrofittemplate05.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startDataObservation()
    }

    private fun startDataObservation() {
        viewModel.data.observe(this, { rickAndMortyCharacters ->
            textView.text = "Fetched: ${rickAndMortyCharacters.results.size} characters"
            // println(rickAndMortyCharacters.results.toTypedArray().contentToString())
        })
    }
}
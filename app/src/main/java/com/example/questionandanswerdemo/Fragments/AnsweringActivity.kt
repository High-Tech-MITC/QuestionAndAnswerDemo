package com.example.questionandanswerdemo.Fragments

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.questionandanswerdemo.R

class AnsweringActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answering_question)
        val autoSlideText = findViewById<TextView>(R.id.autoSlideText)
        autoSlideText.isSelected = true
        /* checking the layout design

         */
    }
}
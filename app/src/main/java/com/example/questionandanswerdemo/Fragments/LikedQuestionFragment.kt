package com.example.questionandanswerdemo.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.questionandanswerdemo.R

class LikedQuestionFragment: Fragment(R.layout.liked_question_view2) {
    lateinit var image:ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         image=view.findViewById(R.id.imageofuser)

    }
}
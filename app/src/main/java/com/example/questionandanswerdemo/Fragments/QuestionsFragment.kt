package com.example.questionandanswerdemo.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.questionandanswerdemo.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class QuestionsFragment:Fragment(R.layout.question_fragment) {
   lateinit var recyleQuestion:RecyclerView
   lateinit var profileimage:ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseAuth=FirebaseAuth.getInstance()
        recyleQuestion=view.findViewById(R.id.recyler_question)
        profileimage=view.findViewById(R.id.profileimageview)
        if (firebaseAuth.currentUser!=null){
            Picasso.get().load(firebaseAuth.currentUser!!.photoUrl).transform(CropCircleTransformation()).fit().into(profileimage)
        }else{
            profileimage.visibility=View.INVISIBLE
        }
    }

}
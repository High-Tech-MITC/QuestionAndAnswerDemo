package com.example.questionandanswerdemo.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.questionandanswerdemo.Adapters.QuestionAdapter
import com.example.questionandanswerdemo.DataLoad.DataLoadListener
import com.example.questionandanswerdemo.MainActivity
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.QuestionView
import com.example.questionandanswerdemo.ViewModel.QuestionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.littlemango.stacklayoutmanager.StackLayoutManager
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class QuestionsFragment:Fragment(R.layout.question_fragment) {
   lateinit var recyleQuestion:RecyclerView
   lateinit var profileimage:ImageView
   lateinit var questionViewModel: QuestionViewModel
    lateinit var questionAdapter: QuestionAdapter
    private val questionViews = ArrayList<QuestionView>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadinitials(view)
        loadquestions()
    }

    private fun loadinitials(view: View) {
        val firebaseAuth=FirebaseAuth.getInstance()
        recyleQuestion=view.findViewById(R.id.recyler_question)
        recyleQuestion.hasFixedSize()
        val stackLayoutManager=StackLayoutManager(StackLayoutManager.ScrollOrientation.TOP_TO_BOTTOM,3)
        recyleQuestion.layoutManager=stackLayoutManager
        questionAdapter= QuestionAdapter(questionViews,requireContext())
        profileimage=view.findViewById(R.id.profileimageview)
        if (firebaseAuth.currentUser!=null){
            Picasso.get().load(firebaseAuth.currentUser!!.photoUrl).transform(CropCircleTransformation()).fit().into(profileimage)
        }else{
            profileimage.visibility=View.INVISIBLE
        }
        profileimage.setOnClickListener {
            firebaseAuth.signOut()
            val intent=Intent(context,MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun loadquestions() {
        val firebase = FirebaseDatabase.getInstance().reference.child("Question").orderByChild("time").limitToLast(100)
        firebase.keepSynced(true)
        firebase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionViews.clear()
                for (dataSnapshot in snapshot.children) {
                    for (dataSnapshot1 in dataSnapshot.children) {
                        val questionView = dataSnapshot1.getValue(QuestionView::class.java)
                        questionViews.add(questionView!!)
                    }

                    recyleQuestion.adapter=questionAdapter
                    questionAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
}

}
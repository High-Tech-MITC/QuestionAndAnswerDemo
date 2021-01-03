package com.example.questionandanswerdemo.Repositary

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.questionandanswerdemo.DataLoad.DataLoadListener
import com.example.questionandanswerdemo.ViewDetails.QuestionView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class FirebaseRepo2 {
    private val questionViews = ArrayList<QuestionView>()
    fun getInstance(context: Context?): FirebaseRepo2? {
        mcontext = context
        if (instanse == null) {
            instanse = FirebaseRepo2()
        }
        dataLoadListener = mcontext as DataLoadListener?
        return instanse
    }

   fun question(): MutableLiveData<ArrayList<QuestionView>> {
            loadquestions()
            val question = MutableLiveData<ArrayList<QuestionView>>()
            question.value = questionViews
            return question
        }

     fun loadquestions() {
        val firebase = FirebaseDatabase.getInstance().reference.child("Question").orderByChild("time").limitToLast(100)
        firebase.keepSynced(true)
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionViews.clear()
                for (dataSnapshot in snapshot.children) {
                    for (dataSnapshot1 in dataSnapshot.children) {
                        val questionView = dataSnapshot1.getValue(
                            QuestionView::class.java
                        )
                        questionViews.add(questionView!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    companion object {
        var instanse: FirebaseRepo2? = null
        var mcontext: Context? = null
        var dataLoadListener: DataLoadListener? = null
    }
}
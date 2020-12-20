package com.example.questionandanswerdemo.Repositary

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.questionandanswerdemo.DataLoad.DataLoadListener
import com.example.questionandanswerdemo.ViewDetails.QuestionView


class FirebaseRepo {
    lateinit var instance:FirebaseRepo
    lateinit var questionmodel:ArrayList<QuestionView>
    lateinit var mcontext:Context
    lateinit var dataLoadListener: DataLoadListener
    fun getInstanse(context: Context):FirebaseRepo{
        mcontext=context
        if (instance==null){
            instance= FirebaseRepo()
        }
        dataLoadListener= mcontext as DataLoadListener
        return instance
    }
    fun getquestion():MutableLiveData<ArrayList<QuestionView>>{
        loadquestions()
       val questions:MutableLiveData<ArrayList<QuestionView>> = MutableLiveData()
        questions.value=questionmodel
        return questions
    }

    private fun loadquestions() {
        // database input waiting for the signup task to finish
        dataLoadListener.onQuestionLoaded()
    }
}
package com.example.questionandanswerdemo.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.questionandanswerdemo.Repositary.FirebaseRepo
import com.example.questionandanswerdemo.ViewDetails.QuestionView


class QuestionViewModel: ViewModel() {
    lateinit var lists: MutableLiveData<ArrayList<QuestionView>>
    fun init(context: Context) {
        if (lists!=null)
        return
        lists= FirebaseRepo().getInstanse(context).getquestion()
    }
    public fun getquestion():LiveData<ArrayList<QuestionView>>{
        return lists
    }
}
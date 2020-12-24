package com.example.questionandanswerdemo.Adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.questionandanswerdemo.ViewDetails.MyQuestionView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.questionandanswerdemo.R

class MyQuestionAdapter(var myQuestionList:List<MyQuestionView>, var myQuestionContext: Context):RecyclerView.Adapter<MyQuestionAdapter.MyQuestionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myquesiong_grid_item, parent, false)
        return MyQuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyQuestionViewHolder, position: Int) {
        holder.myQuestion.text = myQuestionList[position].myQuestion
        //holder.myQuestionAnswerNumber.text = myQuestionList[position].myQuestionAnswerNumber
        holder.myQuestionType.text = myQuestionList[position].myQuestionType
    }

    override fun getItemCount(): Int {
        return myQuestionList.size
    }

    class MyQuestionViewHolder(myQuestionItemView: View): RecyclerView.ViewHolder(myQuestionItemView)
    {
        val myQuestion: TextView = itemView.findViewById(R.id.myQuestion)
        val myQuestionType: TextView = itemView.findViewById(R.id.myQuestionType)
        val myQuestionAnswerNumber: TextView = itemView.findViewById(R.id.myQuestionAnswerNumber)
    }
}
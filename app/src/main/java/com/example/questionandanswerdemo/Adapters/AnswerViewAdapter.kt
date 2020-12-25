package com.example.questionandanswerdemo.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.AnsweViewDetail
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class AnswerViewAdapter(val answerlist:List<AnsweViewDetail>,val context: Context): RecyclerView.Adapter<AnswerViewAdapter.viewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewAdapter.viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.view_answer,parent,false)
        return viewholder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewAdapter.viewholder, position: Int) {
        holder.usernmae.text=answerlist[position].userName
        holder.answer.text=answerlist[position].answer
        Picasso.get().load(answerlist[position].userimage).transform(CropCircleTransformation()).fit().into(holder.userimage)

    }

    override fun getItemCount(): Int {
        if (answerlist.isEmpty()){
            return 0
        }
        return answerlist.size
    }
    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answer:TextView=itemView.findViewById(R.id.answerview)
        val usernmae:TextView=itemView.findViewById(R.id.answername)
        val userimage:ImageView=itemView.findViewById(R.id.answerimage)
    }
}
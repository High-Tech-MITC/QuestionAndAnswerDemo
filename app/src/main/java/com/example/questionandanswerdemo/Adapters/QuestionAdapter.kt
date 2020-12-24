package com.example.questionandanswerdemo.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.QuestionView


import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class QuestionAdapter(var list:List<QuestionView>, var context: Context): RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.custom_layout_question, parent,false)
        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.question.text= list[position].question
        holder.questiontype.text="@ ${list[position].email}"
        holder.questiontypetop.text="#${list[position].type}"
        holder.answerprovided.text=list[position].answers.toString()
        holder.viewsprovided.text=list[position].views.toString()
        Picasso.get().load(list[position].userimage).transform(CropCircleTransformation()).fit().into(holder.imageholder)
        holder.username.text=list[position].userName
        holder.likes.text=list[position].likes.toString()
        val questionid=list[position].uid.toString()
        val questionview=list[position].question
        holder.cardView.setOnClickListener {
            val bundle= bundleOf("questionid" to questionview,"userquestionid" to questionid )
            it.findNavController().navigate(R.id.action_questionsFragment_to_viewQuestionFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size   }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questiontypetop: TextView =itemView.findViewById(R.id.questionType)
        val imageholder: ImageView =itemView.findViewById(R.id.imageofuser)
        val username: TextView =itemView.findViewById(R.id.questionby)
        val question:TextView=itemView.findViewById(R.id.questionview)
        val questiontype: TextView =itemView.findViewById(R.id.usernameof)
        val answerprovided: TextView =itemView.findViewById(R.id.answersprovided)
        val viewsprovided: TextView =itemView.findViewById(R.id.viewofquestion)
        val likes:TextView=itemView.findViewById(R.id.likescount)
        val cardView:CardView=itemView.findViewById(R.id.cardviewquestion)
    }
}
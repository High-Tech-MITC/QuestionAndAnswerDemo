package com.example.questionandanswerdemo.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.AskQuestionDetail
import com.example.questionandanswerdemo.ViewDetails.QuestionView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


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
        holder.likes.setOnClickListener {
            Toast.makeText(it.context,"Value",Toast.LENGTH_LONG).show()
            val uidofquestion=list[position].uid
            val currentuser=FirebaseAuth.getInstance().currentUser!!.uid
            val firebasedata=FirebaseDatabase.getInstance().reference.child("Question").child(currentuser).child("LikedQuestion").orderByChild("question").equalTo(questionview)
            firebasedata.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (keysnapshot in snapshot.children) {
                        if (keysnapshot.exists()) {
                            Log.d("value", "onDataChange: ${keysnapshot.key} ")
                        }else{
                        val addfirebase=FirebaseDatabase.getInstance().reference.child("Question").child(currentuser).child("LikedQuestion")
                        val keyforliked=addfirebase.push().key
                        val addquestionliked=AskQuestionDetail(questionview!!
                        ,ServerValue.TIMESTAMP,list[position].userName!!,list[position].userimage!!,list[position].type!!,list[position].email!!,questionid,list[position].answers,list[position].views,
                        list[position].likes)
                        addfirebase.child(keyforliked!!).setValue(addquestionliked).addOnCompleteListener {
                            if (it.isSuccessful){
                                val Firebasestatus=FirebaseDatabase.getInstance().reference.child("Question").child(questionid).orderByChild("question").equalTo(questionview)
                                Firebasestatus.addListenerForSingleValueEvent(object:ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (snap in snapshot.children){
                                            val keyofquestion=snap.key
                                            val classquestion=snap.getValue(QuestionView::class.java)
                                            val initlikes=classquestion!!.likes
                                            val firebase2=FirebaseDatabase.getInstance().reference.child("Question").child(questionid).child(keyofquestion!!).child("likes")
                                            val addlikes=initlikes+1
                                            firebase2.setValue(addlikes)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            }
                        }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


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
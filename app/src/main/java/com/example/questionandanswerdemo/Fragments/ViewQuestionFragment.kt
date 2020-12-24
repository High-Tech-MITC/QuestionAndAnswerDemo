package com.example.questionandanswerdemo.Fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.questionandanswerdemo.Adapters.AnswerViewAdapter
import com.example.questionandanswerdemo.Dialogs.AddAnswerDialog
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.AddAnswerDetail
import com.example.questionandanswerdemo.ViewDetails.AnsweViewDetail
import com.example.questionandanswerdemo.ViewDetails.QuestionView
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.database.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


class ViewQuestionFragment:Fragment(R.layout.view_question_fragment) {
    lateinit var questionid:String
    lateinit var useruid:String
    lateinit var answer_recyle:RecyclerView
    lateinit var nestedScrollView: NestedScrollView
    lateinit var questionof:TextView
    lateinit var userimage:ImageView
    lateinit var username:TextView
    lateinit var addanswerbutton:FloatingActionButton
    lateinit var AnswerViewAdapter:AnswerViewAdapter
    var answerlist:ArrayList<AnsweViewDetail> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionid= requireArguments().getString("questionid")!!
        useruid=requireArguments().getString("userquestionid")!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        answer_recyle=view.findViewById(R.id.recyle_answer)
        answer_recyle.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)
        answer_recyle.hasFixedSize()
        AnswerViewAdapter=AnswerViewAdapter(answerlist,requireContext())
        nestedScrollView=view.findViewById(R.id.nestedscroll)
        questionof=view.findViewById(R.id.view_questionof)
        username=view.findViewById(R.id.view_username)
        userimage=view.findViewById(R.id.view_userimage)
        addanswerbutton=view.findViewById(R.id.addyouranswer)
        questionof.text=questionid
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    addanswerbutton.hide();
                } else {
                    addanswerbutton.show();
                }
            }
        }
        Log.d("Uid of Question", "onDataChange:$useruid ")
        Log.d("questionof", "onDataChange:$questionid ")
         val firebaseDatabase:Query= FirebaseDatabase.getInstance().reference.child("Question").child(useruid).orderByChild("question").equalTo(questionid)
        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val questionView=snap.getValue(QuestionView::class.java)
                    val intvalue=questionView!!.answers
                    val key=snap.key
                    Picasso.get().load(questionView!!.userimage).transform(CropCircleTransformation()).fit().into(userimage)
                    username.text=questionView.userName
                    addanswerbutton.setOnClickListener {
                       val dialog=Dialog(context!!,R.style.ThemeOverlay_AppCompat_Dialog_Alert)
                        dialog.setContentView(R.layout.custom_add_answer_view)
                        val answeradd=dialog.findViewById<EditText>(R.id.answer_provingbox)
                        val answerbutton=dialog.findViewById<Button>(R.id.answeradd1)
                        answerbutton.setOnClickListener {

                            val answerprovider=answeradd.text.toString()
                            if (answerprovider.isEmpty()){
                                answeradd.error="Please Add answer"
                                answeradd.isFocusable=true
                                return@setOnClickListener
                            }
                            val uidof= FirebaseAuth.getInstance().currentUser!!.uid
                            val firebasedatabse1=FirebaseDatabase.getInstance().reference.child("Question").child(useruid).child(key!!).child("Answer")
                            val firebase= FirebaseAuth.getInstance().currentUser
                            val username=firebase!!.displayName
                            val email=firebase.email.toString()
                            val imageof=firebase.photoUrl.toString()
                            val answerdetail= AddAnswerDetail(username!!,imageof,answerprovider,email,uidof)
                            val keyof=firebasedatabse1.push().key
                            firebasedatabse1.child(keyof!!).setValue(answerdetail).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(requireContext(),"Answer Added", Toast.LENGTH_LONG).show()
                                    dialog.dismiss()
                                    val answedadded=intvalue+1
                                    val firebasedatabseaddanswer=FirebaseDatabase.getInstance().reference.child("Question").child(useruid).child(key!!).child("answers")
                                    firebasedatabseaddanswer.setValue(answedadded)
                                }else{
                                    Toast.makeText(requireContext(),"ERROR:${it.exception.toString()}", Toast.LENGTH_LONG).show()
                                    dialog!!.dismiss()
                                }
                            }
                        }
                        dialog.show()
                    }
                    val firebaseDatabase2=FirebaseDatabase.getInstance().reference.child("Question").child(useruid).child(key!!).child("Answer")
                    firebaseDatabase2.addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            answerlist.clear()
                            for (valanswer in snapshot.children){
                                val answerDetailview=valanswer.getValue(AnsweViewDetail::class.java)
                                answerlist.add(answerDetailview!!)
                            }
                            answer_recyle.adapter=AnswerViewAdapter
                            AnswerViewAdapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

}
package com.example.questionandanswerdemo.Dialogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.questionandanswerdemo.MainActivity
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.AskQuestionDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase

class AskQuestionDialog:DialogFragment(), View.OnClickListener {
    lateinit var  asktype:EditText
    lateinit var  askquestion:EditText
    lateinit var askbutton:Button
    lateinit var cancelbutton:Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view=layoutInflater.inflate(R.layout.customaskquestion,container,false)
        askquestion=view.findViewById(R.id.questionask)
        asktype=view.findViewById(R.id.asktype)
        askbutton=view.findViewById(R.id.askbutton)
        cancelbutton=view.findViewById(R.id.cancel_button)
        askbutton.setOnClickListener(this)
        cancelbutton.setOnClickListener {
            dismiss()
        }
        return view;
    }

    override fun onClick(v: View?) {
        val question=askquestion.text.toString()
        val type=asktype.text.toString()
        if (question.isEmpty()){
            askquestion.error="ADD Question Please"
            askquestion.isFocusable=true
            return
        }
        if (type.isEmpty()){
            asktype.error="Please Provide A Title"
            asktype.isFocusable=true
            return
        }
        val uid=FirebaseAuth.getInstance().currentUser!!.uid
        val firebaseDatabase=FirebaseDatabase.getInstance().reference.child("Question").child(uid)
        val user=FirebaseAuth.getInstance()
        val FirebaseUser: FirebaseUser? =user.currentUser
        val username=FirebaseUser!!.displayName.toString()
        val userimage=FirebaseUser.photoUrl.toString()
        val email=FirebaseUser.email.toString()
        val uidofuser=FirebaseAuth.getInstance().currentUser!!.uid
        Log.d("Uid", "onClick: $uidofuser")
        val askQuestionDetail=AskQuestionDetail(question,ServerValue.TIMESTAMP,username,userimage,type,email,uidofuser,0,0)
        val key=firebaseDatabase.push().key
        firebaseDatabase.child(key!!).setValue(askQuestionDetail).addOnCompleteListener {
            if (it.isSuccessful){
                startActivity(Intent(requireActivity(),MainActivity::class.java))
                dismiss()
            }else{
                Toast.makeText(requireContext(),"Error:${it.exception}",Toast.LENGTH_LONG).show()
            }
        }
    }
}
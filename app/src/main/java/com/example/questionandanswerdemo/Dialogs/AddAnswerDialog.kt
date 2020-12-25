package com.example.questionandanswerdemo.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.AddAnswerDetail
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddAnswerDialog:DialogFragment() {
    lateinit var answer:EditText
    lateinit var addanswer:Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=layoutInflater.inflate(R.layout.custom_add_answer_view,container,false)
        answer=view.findViewById(R.id.answer_provingbox)
        addanswer=view.findViewById(R.id.answeradd1)
        addanswer.setOnClickListener {
            answerprovided(null.toString(), null.toString())
        }

        return view
    }

     fun answerprovided(uid: String, key: String) {
            val answerprovider=answer.text.toString()
            if (answerprovider.isEmpty()){
                answer.error="Please Add answer"
                answer.isFocusable=true
                return
            }
            val uidof=FirebaseAuth.getInstance().currentUser!!.uid
            val firebasedatabse=FirebaseDatabase.getInstance().reference.child("Question").child(uid).child(key).child("Answer")
            val firebase=FirebaseAuth.getInstance().currentUser
            val username=firebase!!.displayName
            val email=firebase.email.toString()
            val imageof=firebase.photoUrl.toString()
             val answerdetail=AddAnswerDetail(username!!,imageof,answerprovider,email,uidof)
            val keyof=firebasedatabse.push().key
            firebasedatabse.child(keyof!!).setValue(answerdetail).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Answer Added",Toast.LENGTH_LONG).show()
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"ERROR:${it.exception.toString()}",Toast.LENGTH_LONG).show()
                    dialog!!.dismiss()
                }
            }

    }
}
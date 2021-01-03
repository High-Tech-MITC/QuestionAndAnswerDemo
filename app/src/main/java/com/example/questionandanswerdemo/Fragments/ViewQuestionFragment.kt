package com.example.questionandanswerdemo.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator.MATERIAL
import com.example.questionandanswerdemo.Adapters.AnswerViewAdapter
import com.example.questionandanswerdemo.HelperClass.CheckUserSignUp
import com.example.questionandanswerdemo.HelperClass.ConnectionCheck
import com.example.questionandanswerdemo.R
import com.example.questionandanswerdemo.ViewDetails.AddAnswerDetail
import com.example.questionandanswerdemo.ViewDetails.AnsweViewDetail
import com.example.questionandanswerdemo.ViewDetails.LikedQuestion
import com.example.questionandanswerdemo.ViewDetails.QuestionView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class ViewQuestionFragment:Fragment(R.layout.view_question_fragment) {
    lateinit var questionid:String
    lateinit var useruid:String
    lateinit var answer_recyle:RecyclerView
    lateinit var nestedScrollView: NestedScrollView
    lateinit var questionof:TextView
    lateinit var userimage:ImageView
    lateinit var username:TextView
    lateinit var toolbar: Toolbar
    lateinit var answercount:TextView
    lateinit var likecount:TextView
    lateinit var viewscount:TextView
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    lateinit var appBarLayout: AppBarLayout
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
         initals(view)
         viewquestion()

    }


    private fun viewquestion() {
        val firebaseDatabase:Query= FirebaseDatabase.getInstance().reference.child("Question").child(useruid).orderByChild("question").equalTo(questionid)
        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val questionView = snap.getValue(QuestionView::class.java)
                    username.text = questionView!!.userName
                    val splited = questionView.userName!!.split(" ")
                    val element1 = splited[0].first().toString().capitalize(Locale.ROOT)
                    val element2 = splited[1].first().toString().capitalize(Locale.ROOT)
                    val drawable = TextDrawable.builder()
                            .beginConfig()
                            .endConfig()
                            .buildRound(element1 + element2, MATERIAL.getColor(element1))
                    userimage.setImageDrawable(drawable)
                    val key = snap.key
                    val intvalue = questionView.answers
                    val likes=questionView.likes
                    likecount.text=likes.toString()
                    if (ConnectionCheck(requireContext()).networkconnectivity()&&CheckUserSignUp().checkUsersignedIn()){
                        addanswer(key, intvalue)
                    }else{
                        Toast.makeText(requireContext(), "no Connection or sign in first to contribute answer", Toast.LENGTH_LONG).show()
                    }
                    answerview(key)
                    answercount.text = questionView.answers.toString()
                    viewscount.text = questionView.views.toString()
                    likecount.text = questionView.likes.toString()
                    likedcliked(key,likes)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: ${error.message} ")
            }

        })
    }

    @SuppressLint("ShowToast")
    private fun likedcliked(key: String?, likes: Int) {
            likecount.setOnClickListener {
            likecount.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_thumb_up_liked_24,0)
            Log.d(TAG, "likedcliked: $key")
            val currentuser=FirebaseAuth.getInstance().currentUser!!.uid
            val firebaselikes = FirebaseDatabase.getInstance().reference.child("LikedQuestion").child(currentuser)
            val firebaseDatabase:Query= FirebaseDatabase.getInstance().reference.child("LikedQuestion").child(currentuser).orderByChild("likedquestion").equalTo(key)
            firebaseDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot2: DataSnapshot) {
                    if (!snapshot2.exists()) {
                        val addkey = firebaselikes.push().key
                        Log.d(TAG, "likedcliked: $addkey $currentuser")
                        val likedQuestion = LikedQuestion(key!!)
                        firebaselikes.push().setValue(likedQuestion).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireContext(), "Nice", Toast.LENGTH_LONG).show()
                                val likesc=likes+1
                                likecount.text=likesc.toString()
                               val likedadd= FirebaseDatabase.getInstance().reference.child("Question").child(useruid).child(key)
                                likedadd.child("likes").setValue(likesc)
                            } else {
                                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Your Already Liked This Question", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    private fun answerview(key: String?) {
        val firebaseDatabase2 =
                FirebaseDatabase.getInstance().reference.child("Question").child(
                        useruid
                ).child(key!!).child("Answer")
        firebaseDatabase2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                answerlist.clear()
                for (valanswer in snapshot.children) {
                    val answerDetailview =
                    valanswer.getValue(AnsweViewDetail::class.java)
                    answerlist.add(answerDetailview!!)
                }
                answer_recyle.adapter = AnswerViewAdapter
                AnswerViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun addanswer(key: String?, intvalue: Int) {
        addanswerbutton.setOnClickListener {
            val dialog = Dialog(requireContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert)
            dialog.setContentView(R.layout.custom_add_answer_view)
            val answeradd = dialog.findViewById<EditText>(R.id.answer_provingbox)
            val answerbutton = dialog.findViewById<Button>(R.id.answeradd1)
            answerbutton.setOnClickListener {
                val answerprovider = answeradd.text.toString()
                if (answerprovider.isEmpty()) {
                    answeradd.error = "Please Add answer"
                    answeradd.isFocusable = true
                    return@setOnClickListener
                }
                val uidof = FirebaseAuth.getInstance().currentUser!!.uid
                val firebasedatabse1 =
                        FirebaseDatabase.getInstance().reference.child("Question").child(
                                useruid
                        ).child(key!!).child("Answer")
                val firebase = FirebaseAuth.getInstance().currentUser
                val username = firebase!!.displayName
                val email = firebase.email.toString()
                val imageof = firebase.photoUrl.toString()
                val answerdetail = AddAnswerDetail(
                        username!!,
                        imageof,
                        answerprovider,
                        email,
                        uidof
                )
                val keyof = firebasedatabse1.push().key
                firebasedatabse1.child(keyof!!).setValue(answerdetail)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireContext(), "Answer Added", Toast.LENGTH_LONG).show()
                                dialog.dismiss()
                                val answedadded = intvalue + 1
                                val firebasedatabseaddanswer = FirebaseDatabase.getInstance().reference.child("Question").child(useruid).child(key).child("answers")
                                firebasedatabseaddanswer.setValue(answedadded).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        answercount.text=answedadded.toString()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                        requireContext(),
                                        "ERROR:${it.exception.toString()}",
                                        Toast.LENGTH_LONG
                                ).show()
                                dialog.dismiss()
                            }
                        }
            }
            dialog.show()
        }
    }
    private fun initals(view:View){
        nestedScrollView=view.findViewById(R.id.nestedscroll)
        questionof=view.findViewById(R.id.view_questionof)
        username=view.findViewById(R.id.view_username)
        userimage=view.findViewById(R.id.view_userimage)
        addanswerbutton=view.findViewById(R.id.addyouranswer)
        collapsingToolbarLayout=view.findViewById(R.id.collapsingtool)
        toolbar=view.findViewById(R.id.toolbar)
        answercount=view.findViewById(R.id.answer_view)
        viewscount=view.findViewById(R.id.views_view)
        likecount=view.findViewById(R.id.likes_view)
        appBarLayout=view.findViewById(R.id.appbar1)
        answer_recyle=view.findViewById(R.id.recyle_answer)
        answer_recyle.layoutManager= LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                true
        )
        answer_recyle.hasFixedSize()
        AnswerViewAdapter=AnswerViewAdapter(answerlist, requireContext())
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isVisible = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.title = "Question"
                    isVisible = true
                } else if (isVisible) {
                    toolbar.title = ""
                    isVisible = false
                }
            }
        })
        questionof.text=questionid
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    addanswerbutton.hide()
                } else {
                    addanswerbutton.show()
                }
            }
        }
    }
companion object{
    const val TAG="ERROR"
}
}
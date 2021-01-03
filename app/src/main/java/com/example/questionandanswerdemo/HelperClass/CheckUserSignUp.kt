package com.example.questionandanswerdemo.HelperClass

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class CheckUserSignUp() {
    lateinit var firebaseUser: FirebaseAuth

    public fun checkUsersignedIn():Boolean{
        firebaseUser= FirebaseAuth.getInstance()
        if (firebaseUser.currentUser!=null){
            return true
        }
        return false
    }

}
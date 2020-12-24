package com.example.questionandanswerdemo

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class FirebaseAppOffline:Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
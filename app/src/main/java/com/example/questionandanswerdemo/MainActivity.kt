package com.example.questionandanswerdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.questionandanswerdemo.Fragments.CustomSignupActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    lateinit var firbaseauth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firbaseauth= FirebaseAuth.getInstance()
        val navController = Navigation.findNavController(this, R.id.activity_main_nav_host)
        val BottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        BottomNavigation.background = null

        BottomNavigation.menu.getItem(2).isEnabled = false
        NavigationUI.setupWithNavController(BottomNavigation, navController)
        val fabicon:FloatingActionButton=findViewById(R.id.addquestion)
        fabicon.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser==null){
                val dialog= CustomSignupActivity()
                dialog.show(supportFragmentManager,"SIGNUP")

            }else{
                AddNewquestion()
            }
        }
    }

    private fun AddNewquestion() {
        Toast.makeText(applicationContext,"MESSAGE",Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        val firebaseuser: FirebaseUser? =firbaseauth.currentUser
        if (firebaseuser!=null){
            Toast.makeText(this,"Loged in ",Toast.LENGTH_LONG).show()
        }
    }
}
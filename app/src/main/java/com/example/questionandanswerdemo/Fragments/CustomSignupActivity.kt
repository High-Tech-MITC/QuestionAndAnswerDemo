package com.example.questionandanswerdemo.Fragments
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.questionandanswerdemo.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import ru.dimorinny.floatingtextbutton.FloatingTextButton
import java.util.*


class CustomSignupActivity : DialogFragment(){
    val TAG = "CustomSignUpActivity"
    lateinit var googleSignInClient:GoogleSignInClient
    private val RC_SIGN_IN = 1001
    private lateinit var facebookAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView:View = inflater.inflate(R.layout.activity_custom_signup, container, false)
        val floatingTextButtongoogle:FloatingTextButton=rootView.findViewById(R.id.floatingGoogle)
        val facebookLogBtn:LoginButton = rootView.findViewById(R.id.fb_login_button)
        facebookAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        facebookLogBtn.setPermissions("email", "public_profile")
        facebookLogBtn.setOnClickListener {
            facebookLogBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")

                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)

                }
            })
        }
        floatingTextButtongoogle.setOnClickListener {
            googleSignIn()
        }
        return rootView
    }
   private fun googleSignIn()
    {
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(requireActivity(), gso)
       val  signinitent=googleSignInClient.signInIntent
        startActivityForResult(signinitent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==RC_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("ACCOUNT", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.w("ERROR", "Google sign in failed:", e)
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                dismiss()
            } else {
                Log.w("Error", "signInWithCredential:failure", task.exception)
                // ...

            }
        }
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        facebookAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    dismiss()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val TAG = "FacebookLogin"
    }

}

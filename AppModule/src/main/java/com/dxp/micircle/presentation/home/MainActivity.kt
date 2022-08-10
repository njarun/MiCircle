package com.dxp.micircle.presentation.home

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.dxp.micircle.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForUser()
    }

    private fun checkForUser() {

        val currentUser = mAuth.currentUser

        if(currentUser != null)
            signIn(currentUser)
        else signUp()
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        currentUser?.let {

            Timber.d("login with - " + currentUser.displayName)
        }
    }

    private fun signUp() {

        val email = "njarunnj@gmail.com"
        val password = "Test@1234"

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    Timber.d("createUserWithEmail:success")
                    val user = mAuth.currentUser

                    val profUpdateReq = UserProfileChangeRequest.Builder().setDisplayName("Arun Jose").build()

                    user?.let {

                        it.updateProfile(profUpdateReq)
                        .addOnCompleteListener { updateResponse ->

                            if (updateResponse.isSuccessful) {
                                updateUI(user)
                            }
                            else {

                                Toast.makeText(this@MainActivity, "Profile Creation failed", LENGTH_LONG).show()
                                it.delete()
                            }
                        }
                    }
                }
                else {

                    Timber.d("createUserWithEmail:failure", task.exception)

                    Toast.makeText(this@MainActivity, "Authentication failed.", LENGTH_LONG).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(currentUser: FirebaseUser) {

        val email = currentUser.email
        val password = "Test@1234"

        val authCredential = EmailAuthProvider.getCredential(email!!, password)

        currentUser.reauthenticate(authCredential).addOnCompleteListener {

            if (it.isSuccessful) {

                Timber.d("signInWithEmail:success")

                val user = mAuth.currentUser
                updateUI(user)
            }
            else {

                currentUser.delete()

                Timber.d("signInWithEmail:failure", it.exception)
                Toast.makeText(this@MainActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }

        /*mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    Timber.d("signInWithEmail:success")

                    val user = mAuth.currentUser
                    updateUI(user)
                }
                else {

                    Timber.d("signInWithEmail:failure", task.exception)
                    Toast.makeText(this@MainActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }*/
    }
}
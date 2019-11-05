package com.trioafk.gombal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.lang.ref.Reference

class SignInAct : AppCompatActivity() {

    lateinit var reference: DatabaseReference

    internal var USERNAME_KEY = "username_key"
    internal var username_key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val USERNAME_KEY = "usernamekey"
        val username_key = ""

        btn_new_account.setOnClickListener(){
            val gotoregisterone = Intent(this@SignInAct,RegisterOneAct::class.java)
            startActivity(gotoregisterone)
        }

        btn_sign_in.setOnClickListener {
            //ubah state menjadi loading
            btn_sign_in.isEnabled = false
            btn_sign_in.text = "Loading ..."

            val username = xusername.text.toString()
            val password = xpassword.text.toString()

            if (username.isEmpty() && password.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Username dan password anda kosong",
                    Toast.LENGTH_SHORT
                ).show()
                btn_sign_in.isEnabled = true
                btn_sign_in.text = "SIGN IN"
            } else if (username.isEmpty()) {
                Toast.makeText(applicationContext, "Username anda kosong", Toast.LENGTH_SHORT)
                    .show()
                btn_sign_in.isEnabled = true
                btn_sign_in.text = "SIGN IN"

            } else {
                if (password.isEmpty()) {
                    Toast.makeText(applicationContext, "Password anda kosong", Toast.LENGTH_SHORT)
                        .show()
                    btn_sign_in.isEnabled = true
                    btn_sign_in.text = "SIGN IN"
                } else {
                    reference =
                        FirebaseDatabase.getInstance().reference.child("Users").child(username)

                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                //ambil data password dari firebase
                                val passwordFromFirebase =
                                    dataSnapshot.child("password").value!!.toString()

                                //validasi password dengan password firebase
                                if (password == passwordFromFirebase) {
                                    // simpan username (key) kepada local
                                    val sharedPreferences =
                                        getSharedPreferences(USERNAME_KEY, MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString(username_key, xusername.text.toString())
                                    editor.apply()

                                    //pindah activity
                                    val gotohome = Intent(this@SignInAct, HomeAct::class.java)
                                    startActivity(gotohome)
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Password salah",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    btn_sign_in.isEnabled = true
                                    btn_sign_in.text = "SIGN IN"
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Username tidak ada",
                                    Toast.LENGTH_SHORT
                                ).show()
                                btn_sign_in.isEnabled = true
                                btn_sign_in.text = "SIGN IN"
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(applicationContext, "Database Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                }
            }
        }
    }
}

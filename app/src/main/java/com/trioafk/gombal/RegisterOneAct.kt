package com.trioafk.gombal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register_one.*

class RegisterOneAct : AppCompatActivity() {

    lateinit var reference: DatabaseReference
    lateinit var reference_username:DatabaseReference

    var USERNAME_KEY = "usernamekey"
    var username_key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_one)

        btn_continue.setOnClickListener {
            //ubah state menjadi loading
            btn_continue.isEnabled = false
            btn_continue.text = "Loading ..."

            //mengambil username dari firebase
            reference_username = FirebaseDatabase.getInstance().reference.child("Users").child(username.text.toString())
            reference_username.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(applicationContext, "Username telah dipakai", Toast.LENGTH_SHORT).show()

                        //ubah state menjadi loading
                        btn_continue.isEnabled = true
                        btn_continue.text = "CONTINUE"
                    } else {
                        //menyimpan data kepada local storage (handphone)
                        val sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString(username_key, username.text.toString())
                        editor.apply()

                        //simpan ke database
                        reference = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(username.text.toString())
                        reference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                dataSnapshot.ref.child("username").setValue(username.text.toString())
                                dataSnapshot.ref.child("password").setValue(password.text.toString())
                                dataSnapshot.ref.child("email_address").setValue(email_address.text.toString())

                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })

                        //berpindah activity dengan intent
                        val gotonextregister =
                            Intent(this@RegisterOneAct, RegisterTwoAct::class.java)
                            startActivity(gotonextregister)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

        btn_back.setOnClickListener(){
            onBackPressed()
        }
    }
}

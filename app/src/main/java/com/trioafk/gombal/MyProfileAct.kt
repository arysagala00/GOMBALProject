package com.trioafk.gombal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MyProfileAct : AppCompatActivity() {

    lateinit var reference:DatabaseReference

    lateinit var nama_lengkap:TextView
    lateinit var bio:TextView
    lateinit var photo_profile:ImageView
    lateinit var btn_back_home:Button
    lateinit var btn_sign_out:Button
    lateinit var btn_edit_profile:Button

    var USERNAME_KEY = "usernamekey"
    var username_key = ""
    var username_key_new: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        getUsernameLocal()

        nama_lengkap = findViewById(R.id.nama_lengkap)
        bio = findViewById(R.id.bio)
        photo_profile = findViewById(R.id.photo_profile)
        btn_back_home = findViewById(R.id.btn_back_home)
        btn_edit_profile = findViewById(R.id.btn_edit_profile)
        btn_sign_out = findViewById(R.id.btn_sign_out)

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new!!)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").value!!.toString())
                bio.setText(dataSnapshot.child("bio").value!!.toString())
                Picasso.with(this@MyProfileAct)
                    .load(dataSnapshot.child("url_photo_profile").value!!.toString()).centerCrop()
                    .fit().into(photo_profile)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        btn_edit_profile.setOnClickListener(){
            val gotoeditprofile = Intent(baseContext,EditProfilAct::class.java)
            startActivity(gotoeditprofile)
        }

        btn_back_home.setOnClickListener(View.OnClickListener {
            val gotohome = Intent(this@MyProfileAct, HomeAct::class.java)
            startActivity(gotohome)
            finish()
        })

        btn_sign_out.setOnClickListener {
            val sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(username_key, null)
            editor.apply()

            val gotosignin = Intent(this@MyProfileAct, SignInAct::class.java)
            startActivity(gotosignin)
            finish()
        }
    }

    fun getUsernameLocal() {
        val sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE)
        username_key_new = sharedPreferences.getString(username_key, "")
    }
}

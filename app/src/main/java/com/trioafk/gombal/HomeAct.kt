package com.trioafk.gombal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.shapeofview.shapes.CircleView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*

class HomeAct : AppCompatActivity() {

    lateinit var reference: DatabaseReference
    lateinit var reference1: DatabaseReference

    lateinit var photo_home_user:ImageView
    lateinit var nama_lengkap:TextView
    lateinit var bio:TextView
    lateinit var btn_to_profile:CircleView


    var USERNAME_KEY = "usernamekey"
    var username_key = ""
    var username_key_new: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        getUsernameLocal()

        nama_lengkap = findViewById(R.id.nama_lengkap)
        bio = findViewById(R.id.bio)
        photo_home_user = findViewById(R.id.photo_home_user)
        btn_to_profile = findViewById(R.id.btn_to_profile)


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new!!)
        reference1 = FirebaseDatabase.getInstance().getReference().child("url_photo")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").value!!.toString())
                bio.setText(dataSnapshot.child("bio").value!!.toString())
                user_balance.text = "Rp " + dataSnapshot.child("user_balance").value.toString()
                    Picasso.with(this@HomeAct)
                        .load(dataSnapshot.child("url_photo_profile").value!!.toString())
                        .centerCrop()
                        .fit().into(photo_home_user)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        btn_to_profile.setOnClickListener(){
            val gotoprofile = Intent(this@HomeAct, MyProfileAct::class.java)
            startActivity(gotoprofile)
        }

        btn_profile.setOnClickListener() {
            val gotoprofile = Intent(this@HomeAct, MyProfileAct::class.java)
            startActivity(gotoprofile)
        }

        btn_order.setOnClickListener(){
            val goToOrder = Intent(this@HomeAct, MapsActivity::class.java)
            startActivity(goToOrder)
        }

        btn_wallet.setOnClickListener(){
            val GoToWallet = Intent(this@HomeAct, Wallet::class.java)
            startActivity(GoToWallet)
        }

    }

    fun getUsernameLocal(){
        val sharedPreferences:SharedPreferences = getSharedPreferences(USERNAME_KEY, Context.MODE_PRIVATE)
        username_key_new = sharedPreferences.getString(username_key,"")
    }
}

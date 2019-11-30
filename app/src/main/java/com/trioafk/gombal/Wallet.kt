package com.trioafk.gombal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.activity_wallet.nama_lengkap
import kotlinx.android.synthetic.main.activity_wallet.user_balance

class Wallet : AppCompatActivity() {

    var myBalance: Int = 0
    var valueTotalHarga: Int = 0
    var sisa_balance = 0

    lateinit var reference: DatabaseReference
    lateinit var reference2: DatabaseReference

    var USERNAME_KEY = "usernamekey"
    var username_key = ""
    var username_key_new: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        getUsernameLocal()

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new!!)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").value!!.toString())
                myBalance = Integer.valueOf(dataSnapshot.child("user_balance").value.toString())
                            user_balance.text = "Rp "+myBalance
                Picasso.with(this@Wallet)
                    .load(dataSnapshot.child("url_photo_profile").value!!.toString())
                    .centerCrop()
                    .fit().into(photo_profile)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        btn_save_wallet.setOnClickListener(){



            btn_save_wallet.isEnabled = false
            btn_save_wallet.text = "Loading ..."

            //Validasi semua field diisi
            if(TextUtils.isEmpty(nominal.editableText) && TextUtils.isEmpty(password.editableText)){
                Toast.makeText(applicationContext,"Nominal dan password harus diisi",Toast.LENGTH_SHORT).show()
                btn_save_wallet.isEnabled = true
                btn_save_wallet.text = "SAVE"
            }else if(!TextUtils.isEmpty(nominal.editableText) && TextUtils.isEmpty(password.editableText)){
                Toast.makeText(applicationContext,"Password harus diisi",Toast.LENGTH_SHORT).show()
                btn_save_wallet.isEnabled = true
                btn_save_wallet.text = "SAVE"
            }else if(TextUtils.isEmpty(nominal.editableText) && !TextUtils.isEmpty(password.editableText)){
                Toast.makeText(applicationContext,"Nominal harus diisi",Toast.LENGTH_SHORT).show()
                btn_save_wallet.isEnabled = true
                btn_save_wallet.text = "SAVE"
            }
            else{
                val nominal = Integer.valueOf(nominal.text.toString())
                reference2 = FirebaseDatabase.getInstance().reference.child("Users").child(username_key_new!!)

                reference2!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val passwordFromFirebase =
                            dataSnapshot.child("password").value!!.toString()

                        if(password.text.toString() == passwordFromFirebase){
                            sisa_balance = myBalance + nominal
                            reference2!!.getRef().child("user_balance").setValue(sisa_balance)

                            val updateWallet = Intent(baseContext, HomeAct::class.java)
                            startActivity(updateWallet)
                        }else{
                            Toast.makeText(
                                applicationContext,
                                "Password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                            btn_save_wallet.isEnabled = true
                            btn_save_wallet.text = "SAVE"
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
        }




        btn_back_home.setOnClickListener(){onBackPressed()}
    }

    fun getUsernameLocal(){
        val sharedPreferences: SharedPreferences = getSharedPreferences(USERNAME_KEY, Context.MODE_PRIVATE)
        username_key_new = sharedPreferences.getString(username_key,"")
    }
}

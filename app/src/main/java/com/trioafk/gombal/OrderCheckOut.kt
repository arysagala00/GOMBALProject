package com.trioafk.gombal

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_order_check_out.*

class OrderCheckOut : AppCompatActivity() {

    var USERNAME_KEY = "usernamekey"
    var username_key = ""
    var username_key_new = ""

    var valueJumlahBan: Int = 1
    var myBalance: Int = 0
    var valueTotalHarga: Int = 0
    var sisa_balance = 0
    var valuehargaBan: Int = 10000



    var reference: DatabaseReference? = null
    var reference2:DatabaseReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_check_out)

        getUsernameLocal()

        textjumlahtiket.setText(valueJumlahBan.toString())

        btnmines.setVisibility(View.GONE)
        btnmines.isEnabled = false
        notice_uang.visibility = View.GONE


        //mengambil data user dari firebase
        //mengambil data user dari firebase
        reference =
            FirebaseDatabase.getInstance().reference.child("Users").child(username_key_new)
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myBalance =
                    Integer.valueOf(dataSnapshot.child("user_balance").value.toString())
                    textMyBalance.text = "Rp "+myBalance
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        valueTotalHarga = valuehargaBan*valueJumlahBan
        texttotalharga.setText("Rp "+valueTotalHarga)

        btnplus.setOnClickListener {
            valueJumlahBan += 1
            textjumlahtiket.setText(valueJumlahBan.toString())
            if (valueJumlahBan > 1) {
                btnmines.setVisibility(View.VISIBLE)
                btnmines.isEnabled = true
            }
            valueTotalHarga = valueJumlahBan * valuehargaBan
            texttotalharga.setText("Rp "+valueTotalHarga)
            if (valueTotalHarga > myBalance) {
                btn_pay_ticket.animate().translationY(250f).alpha(0f).setDuration(350).start()
                notice_uang.visibility = View.VISIBLE
                btn_pay_ticket.isEnabled = false
                textMyBalance.setTextColor(Color.parseColor("#D1206B"))

            }
        }

        btnmines.setOnClickListener {
            valueJumlahBan -= 1
            textjumlahtiket.setText(valueJumlahBan.toString())
                if (valueJumlahBan < 2) {
                    btnmines.setVisibility(View.INVISIBLE)
                    btnmines.isEnabled = false
                }
            valueTotalHarga = valuehargaBan * valueJumlahBan
            texttotalharga.setText("Rp "+valueTotalHarga)
                if (valueTotalHarga < myBalance) {
                    btn_pay_ticket.animate().translationY(0f).alpha(1f).setDuration(350).start()
                    notice_uang.visibility = View.GONE
                    btn_pay_ticket.isEnabled = true
                    textMyBalance.setTextColor(Color.parseColor("#203DD1"))
                }
        }

        btn_pay_ticket.setOnClickListener(){
            //update sisa balance pada firebase
            reference2 =
                FirebaseDatabase.getInstance().reference.child("Users").child(username_key_new)
            reference2!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    sisa_balance = myBalance - valueTotalHarga
                    reference2!!.getRef().child("user_balance").setValue(sisa_balance)
                    val gotosuccesspay = Intent(this@OrderCheckOut, SuccessPay::class.java)
                    startActivity(gotosuccesspay)
                    finish()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

        btn_back_lima.setOnClickListener(){
            onBackPressed()
        }
    }

    fun getUsernameLocal() {
        val sharedPreferences =
            getSharedPreferences(USERNAME_KEY, Context.MODE_PRIVATE)
        username_key_new = sharedPreferences.getString(username_key,"").toString()
    }
}

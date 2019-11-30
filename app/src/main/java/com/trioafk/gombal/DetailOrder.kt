package com.trioafk.gombal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_order.*

class DetailOrder : AppCompatActivity() {
    object name{
        val EXTRA_NAME:String = "EXTRA_NAME"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_order)

        nama_bengkel.setText(intent.getStringExtra(name.EXTRA_NAME))

        btn_order.setOnClickListener(){
            val goToCheckout = Intent(this@DetailOrder, OrderCheckOut::class.java)
            startActivity(goToCheckout)
            finish()
        }
    }
}

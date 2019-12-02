package com.trioafk.gombal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_order.*

class DetailOrder : AppCompatActivity() {

    var name: String? = null

    companion object {
        var EXTRA_NAME:String = "EXTRA_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_order)

        if(savedInstanceState!=null){
            val nameFromBundle = savedInstanceState.getString(EXTRA_NAME)
            name = nameFromBundle
            nama_bengkel.text = name
        }

        btn_order.setOnClickListener(){
            val goToCheckout = Intent(this@DetailOrder, OrderCheckOut::class.java)
            startActivity(goToCheckout)
            finish()
        }
    }
}

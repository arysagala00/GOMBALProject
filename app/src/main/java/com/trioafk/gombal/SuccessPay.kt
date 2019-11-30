package com.trioafk.gombal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_success_pay.*

class SuccessPay : AppCompatActivity() {

    var app_splash: Animation? = null
    var btt:Animation? = null
    var ttb:Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_pay)

        //load animation
        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash)
        btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)

        //run animation
        //run animation
        icon_success.startAnimation(app_splash)
        app_title.startAnimation(ttb)
        app_subtitle.startAnimation(ttb)
        btn_dashboard.startAnimation(btt)




        btn_dashboard.setOnClickListener(View.OnClickListener {
            val gotohome = Intent(this@SuccessPay, HomeAct::class.java)
            startActivity(gotohome)
            finish()
        })
    }
}

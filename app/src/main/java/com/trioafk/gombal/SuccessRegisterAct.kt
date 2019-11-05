package com.trioafk.gombal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_success_register.*

class SuccessRegisterAct : AppCompatActivity() {

    lateinit var app_splash: Animation
    lateinit var btt: Animation
    lateinit var ttb: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_register)

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash)
        btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)

        //run animation
        icon_success.startAnimation(app_splash)
        app_title.startAnimation(ttb)
        app_subtitle.startAnimation(ttb)
        btn_explore.startAnimation(btt)

        btn_explore.setOnClickListener(){
            val gotohome = Intent(this@SuccessRegisterAct, HomeAct::class.java)
            startActivity(gotohome)
            finish()
        }
    }
}

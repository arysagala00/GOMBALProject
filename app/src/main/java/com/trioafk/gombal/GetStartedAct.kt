package com.trioafk.gombal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_get_started.*
import kotlinx.android.synthetic.main.activity_get_started.btn_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.*

class GetStartedAct : AppCompatActivity() {

    lateinit var ttb:Animation
    lateinit var  btt:Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        btt = AnimationUtils.loadAnimation(this, R.anim.btt)

        emblem_app.startAnimation(ttb)
        intro_app.startAnimation(ttb)
        btn_sign_in.startAnimation(btt)
        button_create_account.startAnimation(btt)

        btn_sign_in.setOnClickListener(){
            val gotosignin = Intent(this@GetStartedAct,SignInAct::class.java)
            startActivity(gotosignin)
        }

        button_create_account.setOnClickListener(){
            val gotoregister = Intent(this@GetStartedAct,RegisterOneAct::class.java)
            startActivity(gotoregister)
        }
    }
}

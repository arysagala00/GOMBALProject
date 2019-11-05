package com.trioafk.gombal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashAct : AppCompatActivity() {

    lateinit var app_splash:Animation
    lateinit var btt:Animation

    var USERNAME_KEY = "usernamekey"
    var username_key = ""
    var username_key_new: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //load animasi ke activity
        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash)
        btt = AnimationUtils.loadAnimation(this,R.anim.btt)

        //start animation
        app_logo.startAnimation(app_splash)
        app_subtitle.startAnimation(btt)

        getUsernameLocal()
    }

    fun getUsernameLocal() {
        val sharedPreferences = getSharedPreferences(USERNAME_KEY, Context.MODE_PRIVATE)
        username_key_new = sharedPreferences.getString(username_key, "")

        if (username_key_new?.isEmpty()!!) {
            //setting timer untuk 2 detik
            val handler = Handler()
            handler.postDelayed({
                //ganti activity
                val gogetstarted = Intent(this@SplashAct, GetStartedAct::class.java)
                startActivity(gogetstarted)
                finish()
            }, 2000)
        } else {
            //setting timer untuk 2 detik
            val handler = Handler()
            handler.postDelayed({
                //ganti activity
                val gotohome = Intent(this@SplashAct, HomeAct::class.java)
                startActivity(gotohome)
                finish()
            }, 2000)
        }
    }
}

package com.example.virginmoney.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.virginmoney.App.Companion.auth
import com.example.virginmoney.ui.base.BaseActivity
import com.example.virginmoney.databinding.ActivitySplashBinding
//import com.google.firebase.auth.FirebaseAuth

class Splash : BaseActivity() {

    lateinit var bind: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        bind = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bind.root)

        if (auth.currentUser == null) {
//            Handler().postDelayed({
            startActivity(Intent(this@Splash, Login::class.java))
            this@Splash.finish()
//            }, 3000)
        } else {
            //            Handler().postDelayed({
            startActivity(Intent(this@Splash, Home::class.java))
            this@Splash.finish()
//            }, 3000)
        }
    }
}
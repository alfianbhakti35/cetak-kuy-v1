package com.efix.cetakkuy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.auth.LoginActivity
import com.efix.cetakkuy.auth.users.HomeUserActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imageViewSplashScreen.alpha = 0f
        imageViewSplashScreen.animate().setDuration(5000).alpha(1f).withEndAction {
            val i = Intent(this, LoginActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
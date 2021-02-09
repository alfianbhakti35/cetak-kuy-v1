package com.efix.cetakkuy.auth.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.efix.cetakkuy.R
import kotlinx.android.synthetic.main.activity_pesansn_sukses.*

class PesansnSuksesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesansn_sukses)

        button.setOnClickListener {
            val i = Intent(this, HomeUserActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }
}
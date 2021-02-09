package com.efix.cetakkuy.auth.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.auth.LoginActivity
import com.efix.cetakkuy.auth.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home_user.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)

        btnCetakDokumen.setOnClickListener {
            val i = Intent(this, CetakDokumenActivity::class.java)
            startActivity(i)
        }

        btnProfile.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            startActivity(i)
        }

        btnCetakPhoto.setOnClickListener {
            val i = Intent(this, CetakPhotoActivity::class.java)
            startActivity(i)
        }

        btnStatusPesansn.setOnClickListener {
            val i = Intent(this, StatusPesananActivity::class.java)
            startActivity(i)
        }
    }

    private fun cekUser(){
        val uid = FirebaseAuth.getInstance().uid.toString()
        if (uid == null){

            val i = Intent(this, LoginActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)

        }
        else{
            val ref = FirebaseDatabase.getInstance().getReference("/users")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.child(uid).getValue(Users::class.java)
                    if (user?.role == "user"){
                        Toast.makeText(this@HomeUserActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                        val i = Intent(this@HomeUserActivity, HomeUserActivity::class.java)
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                    }else{
                        Toast.makeText(this@HomeUserActivity, "Selamad Datang Admin", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HomeUserActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
package com.efix.cetakkuy.auth.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.admin.HomeAdminActivity
import com.efix.cetakkuy.auth.model.Users
import com.efix.cetakkuy.auth.users.HomeUserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Fungsi tombol untuk pindah ke register activity
        buttonToRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }

//        Fungsi untuk Login
        buttonLogin.setOnClickListener {
            progressBarLogin.visibility = View.VISIBLE
            login()
        }
    }

    private fun login(){
        val email = editTextEmailLogin.text.toString()
        val password = editTextPasswordLogin.text.toString()

        if (email.isEmpty() or password.isEmpty()){
            progressBarLogin.visibility = View.GONE
            Toast.makeText(this, "Email atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    cekRoleId(uid.toString())
                }
                .addOnFailureListener {
                    progressBarLogin.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

    }

    private fun cekRoleId(uid : String){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.child(uid).getValue(Users::class.java)
                if (user?.role == "user"){
                    progressBarLogin.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val i = Intent(this@LoginActivity, HomeUserActivity::class.java)
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                }else{
                    progressBarLogin.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Selamad Datang Admin", Toast.LENGTH_SHORT).show()
                    val i = Intent(this@LoginActivity, HomeAdminActivity::class.java)
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBarLogin.visibility = View.GONE
                Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
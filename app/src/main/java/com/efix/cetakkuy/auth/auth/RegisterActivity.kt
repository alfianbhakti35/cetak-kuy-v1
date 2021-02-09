package com.efix.cetakkuy.auth.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textViewToLogin.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }

        buttonRegister.setOnClickListener {
            progressBarRegister.visibility = View.VISIBLE
            fungsiRegister()
        }
    }

    private fun fungsiRegister(){
        val nama = editTextNamaRegister.text.toString()
        val emal = editTextEmailRegister.text.toString()
        val noHp = editTextNoHpRegister.text.toString()
        val alamat = editTextAlamatLengkap.text.toString()
        val kodepos = editTextTextKodePos.text.toString()
        val password = editTextPasswordRegister.text.toString()

        if (nama.isEmpty() or emal.isEmpty() or noHp.isEmpty() or alamat.isEmpty() or kodepos.isEmpty() or password.isEmpty()){
            progressBarRegister.visibility = View.GONE
            Toast.makeText(this, "Data Harus diIsi Lengkap", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emal, password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    simpanUser(nama, emal, noHp, alamat, kodepos, password)
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
    }

    private fun simpanUser(nama : String, email : String, noHp : String, alamat : String, kodePos : String, password : String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val role = "user"

        val dataUser = Users(uid, nama, email, noHp, alamat, kodePos, password, role)

        ref.setValue(dataUser)
                .addOnSuccessListener {
                    progressBarRegister.visibility = View.GONE
                    val i = Intent(this, LoginActivity::class.java)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(i)
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    progressBarRegister.visibility = View.GONE
                    Toast.makeText(this, "Register Gagal", Toast.LENGTH_LONG).show()
                }
    }
}
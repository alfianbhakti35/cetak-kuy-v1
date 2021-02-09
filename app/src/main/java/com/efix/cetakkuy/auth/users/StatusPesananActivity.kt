package com.efix.cetakkuy.auth.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.adaptor.ItemTransaksi
import com.efix.cetakkuy.auth.model.CetakDokumen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_status_pesanan.*

class StatusPesananActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_pesanan)

        parsingData()
    }

    private fun parsingData(){
        val uid = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/transaksi")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adaptor = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val transaksi = it.getValue(CetakDokumen()::class.java)
                    if (transaksi != null){
                        if (transaksi.uid == uid){
                            adaptor.add(ItemTransaksi(transaksi))
                        }
                    }
                }
                adaptor.setOnItemClickListener { item, view ->
                    val treansaksiItem = item as ItemTransaksi

                    val intent = Intent(view.context, DetailStatusPesananActivity::class.java)
                    intent.putExtra(TRANS_KEY, treansaksiItem.cetakDokumen)
                    startActivity(intent)
                }
                rvStatusPesanan.adapter = adaptor
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    companion object{
        val TRANS_KEY = "TRANS_KEY"
    }
}
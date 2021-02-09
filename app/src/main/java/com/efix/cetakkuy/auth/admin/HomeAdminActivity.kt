package com.efix.cetakkuy.auth.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.adaptor.ItemTransaksi
import com.efix.cetakkuy.auth.auth.LoginActivity
import com.efix.cetakkuy.auth.model.CetakDokumen
import com.efix.cetakkuy.auth.users.DetailStatusPesananActivity
import com.efix.cetakkuy.auth.users.StatusPesananActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home_admin.*

class HomeAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        supportActionBar?.title = "Daftar Pesanan"
        ambilDataPesanan()
    }

    private fun ambilDataPesanan(){
        val ref = FirebaseDatabase.getInstance().getReference("/transaksi")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val transaksi = it.getValue(CetakDokumen::class.java)
                    if (transaksi != null){
                        adapter.add(ItemTransaksi(transaksi))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val treansaksiItem = item as ItemTransaksi

                    val intent = Intent(view.context, DetailPesananAdminActivity::class.java)
                    intent.putExtra(TRANS_KEY_ADMIN, treansaksiItem.cetakDokumen)
                    startActivity(intent)
                }
                rvListPesananAdmin.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeAdminActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btnLogoutAdmin -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object{
        val TRANS_KEY_ADMIN = "TRANS_KEY_ADMIN"
    }
}
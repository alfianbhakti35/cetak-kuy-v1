package com.efix.cetakkuy.auth.admin

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.model.CetakDokumen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail_pesanan_admin.*

private val STORAGE_PERISSION_CODE: Int = 1000

class DetailPesananAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan_admin)
        val transaksi = intent.getParcelableExtra<CetakDokumen>(HomeAdminActivity.TRANS_KEY_ADMIN)

        supportActionBar?.title = "Detail Pesanan"

//        Get Data User
        val uid = transaksi?.uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val nama = snapshot.child("nama").getValue()
                val alamat = snapshot.child("alamat").getValue()

                tVNamaPemesan.setText(nama.toString())
                tvAlamat.setText(alamat.toString())
                tvJumlahPrintAdmin.setText(transaksi?.jumlahKertas.toString())
                tVTanggalTransaksiAdmin.setText(transaksi?.tanggal.toString())
                tVTotalHargaAdmin.setText(transaksi?.totalHarga.toString())
                tvJenisKertas.setText(transaksi?.jenisKertas)
                tvJenisPesanan.setText(transaksi?.jenisPrint)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailPesananAdminActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

//        Set Tombol download
        btnDownloadFile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERISSION_CODE)
                }else{
                    starDownload(transaksi?.urlFile.toString())
                }
            } else{


            }
        }
    }

    private fun starDownload(url : String){
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle("Download")
        request.setDescription("File sedang di Download ...")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            STORAGE_PERISSION_CODE -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val transaksi = intent.getParcelableExtra<CetakDokumen>(HomeAdminActivity.TRANS_KEY_ADMIN)
                starDownload(transaksi?.urlFile.toString())
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
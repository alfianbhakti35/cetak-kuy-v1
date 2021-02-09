package com.efix.cetakkuy.auth.users

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.model.CetakDokumen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cetak_dokumen.*
import kotlinx.android.synthetic.main.activity_cetak_photo.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

var selectFile : Uri? =null

class CetakDokumenActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cetak_dokumen)

        buttonPesanSekarang.setOnClickListener {
            progressBarCetakDokumen.visibility = View.VISIBLE
            uploadFile()
        }

        buttonPilihDokumen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "*/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectFile = data.data
            textViewStatusUpload.setText("Dokumen berhasil dipilih")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inputData(fileName : String, urlFile : String){
        val uid = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/transaksi/$fileName")

//        ambil nilai input User
        val jenisKertas = spinnerJenisKertas.selectedItem.toString()
        val jilid = spinnerJilid.selectedItem.toString()
        var hargaJilid = 0

//        Get Time

        val data = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val tanggal = data.format(format)


        val jumlahPrint = editTextJumlahPrint.text.toString()

        if (jumlahPrint.isEmpty()){
            Toast.makeText(this, "Jumlah Print Harus diIsi", Toast.LENGTH_SHORT).show()
            return
        }
        var ongkir = 2000

        if (jilid == "Ya"){
            hargaJilid = 5000
        }

        val jp : Int = jumlahPrint.toInt()

        val totalHarga = (jp * 1000) + ongkir + hargaJilid

        val transaksi = CetakDokumen(fileName, uid, jenisKertas, jp, jilid, totalHarga, ongkir, "Cetak Dokumen", urlFile, "-", tanggal.toString())

        ref.setValue(transaksi)
            .addOnSuccessListener {
                val i = Intent(this, PesansnSuksesActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                Toast.makeText(this, "Berhsil", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressBarCetakPhoto.visibility = View.GONE
                Toast.makeText(this, "Transaksi Gagal", Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadFile(){
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/file/$fileName")
        ref.putFile(selectFile!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        inputData(fileName, it.toString())
                    }
                    .addOnFailureListener {
                        progressBarCetakPhoto.visibility = View.GONE
                    }
            }
            .addOnFailureListener {
                progressBarCetakPhoto.visibility = View.GONE
            }
    }
}
package com.efix.cetakkuy.auth.users

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.model.CetakDokumen
import com.efix.cetakkuy.auth.model.CetakPhoto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cetak_photo.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

var selecPhoto : Uri? =null

class CetakPhotoActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cetak_photo)

        buttonPilihPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        buttonPesanCetakPhoto.setOnClickListener {
            progressBarCetakPhoto.visibility = View.VISIBLE
            uplodPhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selecPhoto = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selecPhoto)
            imageViewPhoto.setImageBitmap(bitmap)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uplodPhoto(){
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/img/$fileName")
        ref.putFile(selecPhoto!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        Log.d("TAGS", it.toString())
                        inputData(fileName, it.toString())
                    }
                    .addOnFailureListener {
                        progressBarCetakPhoto.visibility = View.GONE
                        Log.d("TAGS", it.toString())
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "GAGAL", Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inputData(filename : String, urlFile : String){
        val ref = FirebaseDatabase.getInstance().getReference("/transaksi/$filename")
        val ukuranPhoto = spinnerUkuranPhoto.selectedItem.toString()
        val uid = FirebaseAuth.getInstance().uid.toString()
        var jumlahPrint = editTextJumlahPrintPhoto.text.toString()

        if (jumlahPrint.isEmpty()){
            Toast.makeText(this, "Jumlah Print Harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        var hargaUkuranPhoto = 0
        if (ukuranPhoto == "2R"){
            hargaUkuranPhoto = 3500
        }else if (ukuranPhoto == "3R"){
            hargaUkuranPhoto = 4000
        }else if (ukuranPhoto == "4R"){
            hargaUkuranPhoto = 5000
        }else if (ukuranPhoto == "5R"){
            hargaUkuranPhoto = 7000
        }else if (ukuranPhoto == "6R"){
            hargaUkuranPhoto = 8000
        }else if (ukuranPhoto == "10R"){
            hargaUkuranPhoto = 15000
        }else{
            hargaUkuranPhoto = 0
        }

        val jPp : Int = jumlahPrint.toInt()

        var totalHarga = jPp * hargaUkuranPhoto + 2000

        val data = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val tanggal = data.format(format)

        val transaksi = CetakPhoto(filename, uid, ukuranPhoto, "Cetak Photo", urlFile, totalHarga)
        val transaksi2 = CetakDokumen(filename, uid, ukuranPhoto, jPp, "-", totalHarga, 2000, "Cetak Photo", urlFile, ukuranPhoto, tanggal)

        ref.setValue(transaksi2)
            .addOnSuccessListener {
                progressBarCetakPhoto.visibility = View.GONE
                Toast.makeText(this, "Transaksi Berhasil", Toast.LENGTH_SHORT).show()
                val i = Intent(this, PesansnSuksesActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            }
            .addOnFailureListener {
                progressBarCetakPhoto.visibility = View.GONE
                Toast.makeText(this, "Transaksi Gagal", Toast.LENGTH_SHORT).show()
            }
    }
}

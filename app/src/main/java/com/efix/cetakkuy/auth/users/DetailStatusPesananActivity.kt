package com.efix.cetakkuy.auth.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.model.CetakDokumen
import kotlinx.android.synthetic.main.activity_detail_status_pesanan.*

class DetailStatusPesananActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_status_pesanan)

        val transaksi = intent.getParcelableExtra<CetakDokumen>(StatusPesananActivity.TRANS_KEY)

        tVTipeDokumen.setText(transaksi?.jenisPrint)
        tvJumlahPrint.setText(transaksi?.jumlahKertas.toString())
        tVTanggalTransaksi.setText(transaksi?.tanggal)
        tVTotalHarga.setText(transaksi?.totalHarga.toString())
        idTransaksiUser.setText(transaksi?.id)

    }
}
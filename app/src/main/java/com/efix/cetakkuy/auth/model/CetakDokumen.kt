package com.efix.cetakkuy.auth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class CetakDokumen(val id : String, val uid : String, val jenisKertas : String, val jumlahKertas : Int, val jilid : String, val totalHarga : Int, val ongkir : Int, val jenisPrint : String, val urlFile : String, val ukuranPhoto : String, val tanggal : String) : Parcelable {
    constructor() : this("", "", "", 1, "", 1, 1,"", "", "","")
}
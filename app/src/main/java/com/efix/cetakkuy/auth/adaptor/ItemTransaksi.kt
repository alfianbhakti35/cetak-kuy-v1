package com.efix.cetakkuy.auth.adaptor

import com.efix.cetakkuy.R
import com.efix.cetakkuy.auth.model.CetakDokumen
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.transaksi_row.view.*

class ItemTransaksi(val cetakDokumen: CetakDokumen):Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewJenisDokumenRow.text = cetakDokumen.jenisPrint
        viewHolder.itemView.editTextHaraRow.text = cetakDokumen.totalHarga.toString()
    }
    override fun getLayout(): Int {
        return R.layout.transaksi_row
    }
}
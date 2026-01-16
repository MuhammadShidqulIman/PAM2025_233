package com.example.perpustakaandigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peminjaman")
data class Peminjaman(
    @PrimaryKey(autoGenerate = true)
    val idPinjam: Int = 0,

    val idBuku: String,
    val judulBuku: String,
    val namaPeminjam: String,
    val tanggalPinjam: String,
    val batasKembali: String,
    val tanggalKembali: String,
    val status: String
)

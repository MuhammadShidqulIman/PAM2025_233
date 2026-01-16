package com.example.perpustakaandigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buku")
data class Buku(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val penulis: String,
    val kategori: String,
    val isbn: String,
    val stok: Int
)

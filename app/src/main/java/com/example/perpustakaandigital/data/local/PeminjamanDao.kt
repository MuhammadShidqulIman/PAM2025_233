package com.example.perpustakaandigital.data.local

import androidx.room.*
import com.example.perpustakaandigital.data.model.Peminjaman
import kotlinx.coroutines.flow.Flow

@Dao
interface PeminjamanDao {

    @Query("SELECT * FROM peminjaman ORDER BY tanggalPinjam DESC")
    fun getAllPeminjaman(): Flow<List<Peminjaman>>

    @Insert
    suspend fun insertPeminjaman(peminjaman: Peminjaman)

    @Update
    suspend fun updatePeminjaman(peminjaman: Peminjaman)
}

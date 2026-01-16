package com.example.perpustakaandigital.data.local

import androidx.room.*
import com.example.perpustakaandigital.data.model.Buku
import kotlinx.coroutines.flow.Flow

@Dao
interface BukuDao {

    @Query("SELECT * FROM buku WHERE judul LIKE '%' || :query || '%' OR penulis LIKE '%' || :query || '%' ORDER BY judul ASC")
    fun searchBuku(query: String): Flow<List<Buku>>
    @Query("SELECT * FROM buku ORDER BY judul ASC")
    fun getAllBuku(): Flow<List<Buku>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuku(buku: Buku)

    @Update
    suspend fun updateBuku(buku: Buku)

    @Delete
    suspend fun deleteBuku(buku: Buku)

    @Query("SELECT * FROM buku WHERE id = :id")
    suspend fun getBukuById(id: Int): Buku

    @Query("UPDATE buku SET stok = stok - 1 WHERE id = :id")
    suspend fun kurangiStok(id: Int)

    @Query("UPDATE buku SET stok = stok + 1 WHERE id = :id")
    suspend fun kembalikanStok(id: Int)
}
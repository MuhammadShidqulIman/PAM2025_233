package com.example.perpustakaandigital.data.repository

import com.example.perpustakaandigital.data.local.BukuDao
import com.example.perpustakaandigital.data.local.PeminjamanDao
import com.example.perpustakaandigital.data.model.Buku
import com.example.perpustakaandigital.data.model.Peminjaman
import kotlinx.coroutines.flow.Flow

class BukuRepository(
    private val bukuDao: BukuDao,
    private val peminjamanDao: PeminjamanDao
) {

    fun searchBuku(query: String): Flow<List<Buku>> {
        return bukuDao.searchBuku(query)
    }

    fun getAllBuku(): Flow<List<Buku>> =
        bukuDao.getAllBuku()

    suspend fun insertBuku(buku: Buku) =
        bukuDao.insertBuku(buku)

    suspend fun updateBuku(buku: Buku) =
        bukuDao.updateBuku(buku)

    suspend fun deleteBuku(buku: Buku) =
        bukuDao.deleteBuku(buku)

    suspend fun getBukuById(id: Int): Buku =
        bukuDao.getBukuById(id)


    suspend fun kurangiStok(id: Int) {
        bukuDao.kurangiStok(id)
    }

    suspend fun kembalikanStok(id: Int) {
        bukuDao.kembalikanStok(id)
    }

    fun getAllPeminjaman(): Flow<List<Peminjaman>> =
        peminjamanDao.getAllPeminjaman()

    suspend fun insertPeminjaman(peminjaman: Peminjaman) =
        peminjamanDao.insertPeminjaman(peminjaman)

    suspend fun updatePeminjaman(peminjaman: Peminjaman) =
        peminjamanDao.updatePeminjaman(peminjaman)
}
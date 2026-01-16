package com.example.perpustakaandigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaandigital.data.model.Peminjaman
import com.example.perpustakaandigital.data.repository.BukuRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PeminjamanViewModel(
    private val repository: BukuRepository
) : ViewModel() {


    val listPeminjaman: StateFlow<List<Peminjaman>> =
        repository.getAllPeminjaman()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun tambahPeminjaman(
        nama: String,
        idBuku: String,
        judulBuku: String,
        tglPinjam: String,
        batasKembali: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val idBukuInt = idBuku.toIntOrNull()

        if (idBukuInt == null) {
            onResult(false, "ID Buku tidak valid")
            return
        }

        viewModelScope.launch {
            try {
                val buku = repository.getBukuById(idBukuInt)

                if (buku != null && buku.stok > 0) {
                    val dataBaru = Peminjaman(
                        idPinjam = 0,
                        namaPeminjam = nama,
                        idBuku = idBuku,

                        judulBuku = judulBuku,

                        tanggalPinjam = tglPinjam,
                        batasKembali = batasKembali,
                        tanggalKembali = "",
                        status = "DIPINJAM"
                    )

                    repository.insertPeminjaman(dataBaru)
                    repository.kurangiStok(idBukuInt)
                    onResult(true, "Peminjaman berhasil disimpan")

                } else {
                    onResult(false, "Gagal: Stok buku habis!")
                }
            } catch (e: Exception) {
                onResult(false, "Gagal: Terjadi kesalahan database")
            }
        }
    }

    fun kembalikanBuku(peminjaman: Peminjaman) {
        viewModelScope.launch {

            val dataUpdate = peminjaman.copy(
                status = "DIKEMBALIKAN",
                tanggalKembali = getTanggalHariIni()
            )
            repository.updatePeminjaman(dataUpdate)

            val idBukuInt = peminjaman.idBuku.toIntOrNull()
            if (idBukuInt != null) {
                repository.kembalikanStok(idBukuInt)
            }
        }
    }

    private fun getTanggalHariIni(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date())
    }
}
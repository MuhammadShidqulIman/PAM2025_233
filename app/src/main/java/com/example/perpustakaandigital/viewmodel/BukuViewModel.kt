package com.example.perpustakaandigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaandigital.data.model.Buku
import com.example.perpustakaandigital.data.repository.BukuRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BukuViewModel(private val repository: BukuRepository) : ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<List<Buku>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllBuku()
            } else {
                repository.searchBuku(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onSearchTextChange(text: String) {
        _searchQuery.value = text
    }

    fun tambahBuku(judul: String, penulis: String, kategori: String, isbn: String, stok: String) {
        val stokInt = stok.toIntOrNull() ?: 0
        val bukuBaru = Buku(
            id = 0,
            judul = judul,
            penulis = penulis,
            kategori = kategori,
            isbn = isbn,
            stok = stokInt
        )
        viewModelScope.launch {
            repository.insertBuku(bukuBaru)
        }
    }

    fun hapusBuku(buku: Buku) {
        viewModelScope.launch {
            repository.deleteBuku(buku)
        }
    }

    fun updateBuku(buku: Buku) {
        viewModelScope.launch {
            repository.updateBuku(buku)
        }
    }
}
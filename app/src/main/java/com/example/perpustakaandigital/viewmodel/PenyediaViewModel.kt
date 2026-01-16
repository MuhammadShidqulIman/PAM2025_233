package com.example.perpustakaandigital.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.perpustakaandigital.data.local.AppDatabase
import com.example.perpustakaandigital.data.repository.BukuRepository

object PenyediaViewModel {
    val Factory = viewModelFactory {

        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            val db = AppDatabase.getDatabase(app)
            val repo = BukuRepository(db.bukuDao(), db.peminjamanDao())
            BukuViewModel(repo)
        }

        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            val db = AppDatabase.getDatabase(app)
            val repo = BukuRepository(db.bukuDao(), db.peminjamanDao())
            PeminjamanViewModel(repo)
        }

        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            val db = AppDatabase.getDatabase(app)
            AuthViewModel(db.userDao())
        }
    }
}
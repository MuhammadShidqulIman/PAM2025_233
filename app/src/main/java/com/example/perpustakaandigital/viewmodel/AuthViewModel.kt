package com.example.perpustakaandigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaandigital.data.local.UserDao
import com.example.perpustakaandigital.data.model.User
import kotlinx.coroutines.launch

class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    fun register(username: String, password: String, nama: String, onResult: (Boolean, String) -> Unit) {

        if (username.isBlank() || password.isBlank() || nama.isBlank()) {
            onResult(false, "Semua data wajib diisi!")
            return
        }

        if (username.length < 8 || password.length < 8) {
            onResult(false, "Username & Password minimal 8 karakter!")
            return
        }

        viewModelScope.launch {
            val adaUser = userDao.cekUsername(username)
            if (adaUser != null) {
                onResult(false, "Username sudah digunakan!")
            } else {
                val userBaru = User(username = username, password = password, namaPetugas = nama)
                userDao.insertUser(userBaru)
                onResult(true, "Registrasi Berhasil, Silakan Login")
            }
        }
    }
    fun login(username: String, password: String, onResult: (Boolean, User?) -> Unit) {

        if (username.isBlank() || password.isBlank()) {
            onResult(false, null)
            return
        }
        viewModelScope.launch {
            val user = userDao.login(username, password)
            if (user != null) {
                onResult(true, user)
            } else {
                onResult(false, null)
            }
        }
    }
}
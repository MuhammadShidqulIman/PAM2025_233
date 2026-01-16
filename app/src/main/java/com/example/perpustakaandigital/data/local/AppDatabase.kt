package com.example.perpustakaandigital.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.perpustakaandigital.data.model.Buku
import com.example.perpustakaandigital.data.model.Peminjaman
import com.example.perpustakaandigital.data.model.User

@Database(
    entities = [Buku::class, Peminjaman::class, User::class],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bukuDao(): BukuDao
    abstract fun peminjamanDao(): PeminjamanDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "perpustakaan_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
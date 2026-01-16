package com.example.perpustakaandigital.uicontroller.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.perpustakaandigital.data.model.Buku

@Composable
fun TambahBukuDialog(
    onDismiss: () -> Unit,
    onSimpan: (Buku) -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var penulis by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Buku") },
        text = {
            Column {

                OutlinedTextField(value = judul, onValueChange = { judul = it }, label = { Text("Judul") })
                OutlinedTextField(value = penulis, onValueChange = { penulis = it }, label = { Text("Penulis") })
                OutlinedTextField(value = kategori, onValueChange = { kategori = it }, label = { Text("Kategori") })
                OutlinedTextField(value = isbn, onValueChange = { isbn = it }, label = { Text("ISBN") })
                OutlinedTextField(value = stok, onValueChange = { stok = it }, label = { Text("Stok") })
            }
        },
        confirmButton = {
            Button(onClick = {
                if (judul.isNotBlank() && penulis.isNotBlank()) {
                    onSimpan(
                        Buku(
                            judul = judul,
                            penulis = penulis,
                            kategori = kategori,
                            isbn = isbn,
                            stok = stok.toIntOrNull() ?: 0
                        )
                    )
                }
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
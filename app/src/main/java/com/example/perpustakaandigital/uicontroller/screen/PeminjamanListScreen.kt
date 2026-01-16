package com.example.perpustakaandigital.uicontroller.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perpustakaandigital.data.model.Peminjaman

@Composable
fun PeminjamanListScreen(
    listPeminjaman: List<Peminjaman>,
    onTambahClick: () -> Unit,
    onDetailClick: (Peminjaman) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onTambahClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Daftar Peminjaman",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(listPeminjaman) { peminjaman ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onDetailClick(peminjaman) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Peminjam: ${peminjaman.namaPeminjam}")
                            Text("Tanggal Pinjam: ${peminjaman.tanggalPinjam}")
                            Text("Status: ${peminjaman.status}")
                        }
                    }
                }
            }
        }
    }
}

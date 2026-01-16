package com.example.perpustakaandigital.uicontroller.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perpustakaandigital.data.model.Buku

@Composable
fun BukuItem(
    buku: Buku,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = buku.judul, style = MaterialTheme.typography.titleMedium)
            Text(text = "Penulis: ${buku.penulis}")
            Text(text = "Kategori: ${buku.kategori}")
            Text(text = "Stok: ${buku.stok}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Hapus")
            }
        }
    }
}

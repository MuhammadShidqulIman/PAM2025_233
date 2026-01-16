package com.example.perpustakaandigital.uicontroller.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.perpustakaandigital.data.model.Peminjaman

private val WarnaBiruPengembalian = Color(0xFF4A90E2)
private val WarnaLatarPengembalian = Color(0xFFF8F9FA)

private fun formatTanggalIndoSimple(dateString: String): String {
    return try {
        val parts = dateString.split("-")
        if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else dateString
    } catch (e: Exception) { dateString }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengembalianScreen(
    listPeminjaman: List<Peminjaman>,
    onKembalikan: (Peminjaman) -> Unit,
    navigateBack: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf<Peminjaman?>(null) }

    var searchText by remember { mutableStateOf("") }

    val filteredList = listPeminjaman.filter {
        it.judulBuku.contains(searchText, ignoreCase = true) ||
                it.namaPeminjam.contains(searchText, ignoreCase = true)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengembalian Buku", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = WarnaBiruPengembalian
                )
            )
        },
        containerColor = WarnaLatarPengembalian
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Cari peminjam atau judul buku...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WarnaBiruPengembalian,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.SearchOff, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if(searchText.isNotEmpty()) "Data tidak ditemukan" else "Tidak ada buku dipinjam",
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { peminjaman ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, null, tint = WarnaBiruPengembalian, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(peminjaman.namaPeminjam, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    }
                                    Surface(color = Color(0xFFFFF3E0), shape = RoundedCornerShape(8.dp)) {
                                        Text("DIPINJAM", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color(0xFFEF6C00), fontWeight = FontWeight.Bold)
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))

                                Text(peminjaman.judulBuku, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))

                                Row {
                                    Icon(Icons.Default.DateRange, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Pinjam: ${formatTanggalIndoSimple(peminjaman.tanggalPinjam)} | Batas: ${formatTanggalIndoSimple(peminjaman.batasKembali)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { showConfirmDialog = peminjaman },
                                    colors = ButtonDefaults.buttonColors(containerColor = WarnaBiruPengembalian),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Terima Pengembalian Buku")
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showConfirmDialog != null) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = null },
                title = { Text("Konfirmasi") },
                text = { Text("Terima pengembalian '${showConfirmDialog?.judulBuku}'?") },
                confirmButton = {
                    Button(onClick = { showConfirmDialog?.let { onKembalikan(it) }; showConfirmDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = WarnaBiruPengembalian)) { Text("Ya") }
                },
                dismissButton = { TextButton(onClick = { showConfirmDialog = null }) { Text("Batal") } }
            )
        }
    }
}
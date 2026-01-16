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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTanggalIndo(dateString: String): String {
    return try {
        val parts = dateString.split("-")
        if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else dateString
    } catch (e: Exception) { dateString }
}


fun cekTerlambat(batasKembali: String): Boolean {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        val batas = format.parse(batasKembali)
        val hariIni = Date()

        hariIni.after(batas)
    } catch (e: Exception) {
        false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPeminjamanScreen(
    listPeminjaman: List<Peminjaman>,
    navigateBack: () -> Unit
) {
    val PrimaryBlue = Color(0xFF4A90E2)
    val BgColor = Color(0xFFF8F9FA)
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchText by remember { mutableStateOf("") }

    val filteredList = listPeminjaman.filter {
        it.judulBuku.contains(searchText, ignoreCase = true) ||
                it.namaPeminjam.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Transaksi", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = PrimaryBlue)
            )
        },
        containerColor = BgColor
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Cari riwayat...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (searchText.isNotEmpty()) "Data tidak ditemukan" else "Belum ada riwayat", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { peminjaman ->

                        val isKembali = peminjaman.status == "DIKEMBALIKAN"
                        val isTerlambat = !isKembali && cekTerlambat(peminjaman.batasKembali)

                        val mainColor = when {
                            isKembali -> Color(0xFF2E7D32)
                            isTerlambat -> Color(0xFFB91C1C)
                            else -> PrimaryBlue
                        }

                        val statusText = when {
                            isKembali -> "SELESAI"
                            isTerlambat -> "TERLAMBAT"
                            else -> "DIPINJAM"
                        }

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.height(IntrinsicSize.Min)) {

                                Box(
                                    modifier = Modifier
                                        .width(6.dp)
                                        .fillMaxHeight()
                                        .background(mainColor)
                                )

                                Column(modifier = Modifier.padding(16.dp).weight(1f)) {

                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = peminjaman.judulBuku,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Surface(
                                            color = mainColor.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = statusText,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = mainColor,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "Peminjam: ${peminjaman.namaPeminjam}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.DateRange, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))

                                        Column {
                                            Text("Pinjam: ${formatTanggalIndo(peminjaman.tanggalPinjam)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                                            Text(
                                                text = "Batas:   ${formatTanggalIndo(peminjaman.batasKembali)}",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = if(isTerlambat) FontWeight.Bold else FontWeight.Medium,
                                                color = if(isTerlambat) Color.Red else Color.Gray
                                            )
                                        }
                                    }

                                    if (isKembali) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Check, null, tint = mainColor, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Dikembalikan: ${formatTanggalIndo(peminjaman.tanggalKembali ?: "-")}",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Bold,
                                                color = mainColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
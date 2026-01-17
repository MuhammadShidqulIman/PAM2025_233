package com.example.perpustakaandigital.uicontroller.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.perpustakaandigital.data.model.Buku


val PrimaryBlue = Color(0xFF4A90E2)
val BackgroundGray = Color(0xFFF8F9FA)
val BookColors = listOf(Color(0xFFD32F2F), Color(0xFF1976D2), Color(0xFF388E3C), Color(0xFFFBC02D), Color(0xFF5D4037), Color(0xFF7B1FA2))
fun getBookColor(id: Int): Color = BookColors[id % BookColors.size]

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaBukuScreen(
    listBuku: List<Buku>,
    onTambahBuku: (String, String, String, String, String) -> Unit,
    onEditBuku: (Buku) -> Unit,
    onHapusBuku: (Buku) -> Unit,
    navigateBack: () -> Unit,
    searchText: String,
    onSearchChange: (String) -> Unit
) {

    var idBukuDipilih by remember { mutableStateOf<Int?>(null) }
    var judul by remember { mutableStateOf("") }
    var penulis by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }


    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Buku?>(null) }


    var expandedKategori by remember { mutableStateOf(false) }
    val opsiKategori = listOf("Fiksi", "Sains", "Sejarah", "Teknologi", "Biografi", "Komik", "Agama", "Lainnya")

    val context = LocalContext.current


    val categories = listOf("Semua", "Fiksi", "Sains", "Sejarah", "Teknologi", "Stok Menipis")
    var selectedCategory by remember { mutableStateOf("Semua") }


    val filteredList = listBuku.filter { buku ->
        val matchSearch = buku.judul.contains(searchText, ignoreCase = true) ||
                buku.penulis.contains(searchText, ignoreCase = true) ||
                buku.isbn.contains(searchText, ignoreCase = true)

        val matchCategory = when (selectedCategory) {
            "Semua" -> true
            "Stok Menipis" -> buku.stok < 3
            else -> buku.kategori.equals(selectedCategory, ignoreCase = true)
        }

        matchSearch && matchCategory
    }

    fun resetForm() {
        idBukuDipilih = null; judul = ""; penulis = ""; kategori = ""; isbn = ""; stok = ""
        expandedKategori = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Buku", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = navigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = PrimaryBlue)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { resetForm(); showFormDialog = true },
                containerColor = PrimaryBlue, contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, null) }, text = { Text("Tambah Buku") }
            )
        },
        containerColor = BackgroundGray
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {


            SearchBarCustom(searchText, onSearchChange)


            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(50)).clickable { selectedCategory = cat },
                        color = if (isSelected) PrimaryBlue else Color.White,
                        contentColor = if (isSelected) Color.White else Color.Gray,
                        shape = RoundedCornerShape(50),
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text(cat, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.labelLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            if (filteredList.isEmpty()) {
                EmptyStateView(searchText, selectedCategory)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(filteredList) { buku ->
                        BukuItemCard(
                            buku = buku,
                            onEdit = {
                                idBukuDipilih = buku.id; judul = buku.judul; penulis = buku.penulis; kategori = buku.kategori; isbn = buku.isbn; stok = buku.stok.toString()
                                showFormDialog = true
                            },
                            onDelete = { showDeleteDialog = buku }
                        )
                    }
                }
            }
        }

        if (showFormDialog) {
            AlertDialog(
                onDismissRequest = { showFormDialog = false; resetForm() },
                title = { Text(if (idBukuDipilih == null) "Tambah Buku Baru" else "Edit Buku") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(judul, { judul = it }, label = { Text("Judul") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(penulis, { penulis = it }, label = { Text("Penulis") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                        ExposedDropdownMenuBox(
                            expanded = expandedKategori,
                            onExpandedChange = { expandedKategori = !expandedKategori },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = kategori,
                                onValueChange = { kategori = it },
                                label = { Text("Kategori (Pilih/Ketik)") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                singleLine = true
                            )

                            ExposedDropdownMenu(
                                expanded = expandedKategori,
                                onDismissRequest = { expandedKategori = false }
                            ) {
                                opsiKategori.forEach { opsi ->
                                    DropdownMenuItem(
                                        text = { Text(opsi) },
                                        onClick = {
                                            kategori = opsi
                                            expandedKategori = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = isbn,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() || it == '-' || it.uppercaseChar() == 'X' }) {
                                    isbn = input.uppercase()
                                }
                            },
                            label = { Text("ISBN (Unik)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next),
                            placeholder = { Text("Contoh: 978-602-...") }
                        )

                        OutlinedTextField(
                            value = stok,
                            onValueChange = { if (it.all { char -> char.isDigit() }) stok = it },
                            label = { Text("Stok") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (judul.isBlank() || penulis.isBlank() || kategori.isBlank() || isbn.isBlank() || stok.isBlank()) {
                                Toast.makeText(context, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val isDuplicateISBN = listBuku.any { existingBuku ->
                                existingBuku.isbn == isbn && existingBuku.id != (idBukuDipilih ?: -1)
                            }
                            if (isDuplicateISBN) {
                                Toast.makeText(context, "ISBN sudah terdaftar! Gunakan ISBN lain.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (idBukuDipilih == null) {
                                onTambahBuku(judul, penulis, kategori, isbn, stok)
                            } else {
                                onEditBuku(Buku(idBukuDipilih!!, judul, penulis, kategori, isbn, stok.toIntOrNull()?:0))
                            }
                            resetForm(); showFormDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) { Text("Simpan") }
                },
                dismissButton = { TextButton(onClick = { showFormDialog = false; resetForm() }) { Text("Batal") } }
            )
        }

        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Hapus Buku?") },
                text = { Text("Apakah Anda yakin ingin menghapus buku '${showDeleteDialog?.judul}'? Tindakan ini tidak dapat dibatalkan.") },
                confirmButton = { Button(onClick = { showDeleteDialog?.let { onHapusBuku(it) }; showDeleteDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))) { Text("Hapus") } },
                dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Batal") } }
            )
        }
    }
}


@Composable
fun SearchBarCustom(text: String, onTextChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = text, onValueChange = onTextChange,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        placeholder = { Text("Cari judul, penulis...") },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
        shape = RoundedCornerShape(12.dp), singleLine = true,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
    )
}

@Composable
fun BukuItemCard(buku: Buku, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Surface(modifier = Modifier.width(60.dp).height(85.dp), shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp), color = getBookColor(buku.id), shadowElevation = 4.dp) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxHeight().width(6.dp).background(Color.Black.copy(alpha = 0.2f)).align(Alignment.CenterStart))
                    Icon(Icons.Default.MenuBook, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(24.dp).align(Alignment.Center))
                    Box(modifier = Modifier.fillMaxHeight().width(2.dp).background(Color.White.copy(alpha = 0.4f)).align(Alignment.CenterEnd))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(buku.judul, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color(0xFF2C3E50))
                Text("ISBN: ${buku.isbn}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text("Penulis: ${buku.penulis}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = PrimaryBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp)) { Text(buku.kategori, Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = PrimaryBlue, fontWeight = FontWeight.Medium) }
                    Spacer(modifier = Modifier.width(12.dp))
                    val stokColor = if (buku.stok > 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (buku.stok > 0) Icons.Default.CheckCircle else Icons.Default.Warning, null, tint = stokColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Stok: ${buku.stok}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = stokColor)
                    }
                }
            }
            Column {
                IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.Edit, "Edit", tint = Color.Gray) }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.Delete, "Hapus", tint = Color(0xFFEF5350)) }
            }
        }
    }
}

@Composable
fun EmptyStateView(searchText: String, category: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Search, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            Text(if (searchText.isEmpty()) "Belum ada buku" else "Tidak ditemukan", color = Color.Gray)
        }
    }
}
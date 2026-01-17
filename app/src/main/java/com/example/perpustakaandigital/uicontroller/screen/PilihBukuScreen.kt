package com.example.perpustakaandigital.uicontroller.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perpustakaandigital.data.model.Buku

val SelectPrimaryBlue = Color(0xFF4A90E2)
val SelectBackgroundGray = Color(0xFFF8F9FA)
val SelectBookColors = listOf(Color(0xFFD32F2F), Color(0xFF1976D2), Color(0xFF388E3C), Color(0xFFFBC02D), Color(0xFF5D4037), Color(0xFF7B1FA2))
fun getSelectBookColor(id: Int): Color = SelectBookColors[id % SelectBookColors.size]

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PilihBukuScreen(
    listBuku: List<Buku>,
    onBukuSelected: (Buku) -> Unit,
    navigateBack: () -> Unit,
    searchText: String,
    onSearchChange: (String) -> Unit
) {
    val categories = listOf("Semua", "Fiksi", "Sains", "Sejarah", "Teknologi", "Lainnya")
    var selectedCategory by remember { mutableStateOf("Semua") }

    val filteredList = listBuku.filter { buku ->
        val matchSearch = buku.judul.contains(searchText, ignoreCase = true) ||
                buku.penulis.contains(searchText, ignoreCase = true) ||
                buku.isbn.contains(searchText, ignoreCase = true)

        val matchCategory = if (selectedCategory == "Semua") true else buku.kategori.equals(selectedCategory, ignoreCase = true)

        matchSearch && matchCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pilih Buku", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = navigateBack) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = SelectPrimaryBlue)
            )
        },
        containerColor = SelectBackgroundGray
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            PilihBukuSearchBar(searchText, onSearchChange)

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(50)).clickable { selectedCategory = cat },
                        color = if (isSelected) SelectPrimaryBlue else Color.White,
                        contentColor = if (isSelected) Color.White else Color.Gray,
                        shape = RoundedCornerShape(50),
                        border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text(cat, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.labelLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Buku tidak ditemukan", color = Color.Gray)
                }
            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { buku ->
                        PilihBukuGridItem(buku = buku, onClick = { onBukuSelected(buku) })
                    }
                }
            }
        }
    }
}

@Composable
fun PilihBukuGridItem(buku: Buku, onClick: () -> Unit) {
    val isHabis = buku.stok <= 0
    val cardAlpha = if (isHabis) 0.6f else 1.0f

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .alpha(cardAlpha)
            .clickable(enabled = !isHabis) { onClick() }
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(if (isHabis) Color.Gray else getSelectBookColor(buku.id)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(48.dp)
                )

                Surface(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = buku.kategori,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = buku.judul,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2,
                    color = if (isHabis) Color.Gray else Color.Black,
                    textDecoration = if (isHabis) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Penulis: ${buku.penulis}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "ISBN: ${buku.isbn}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray,
                    maxLines = 1,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(8.dp))


                if (isHabis) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Cancel, null, tint = Color.Red, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tidak Tersedia", fontSize = 11.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sisa: ${buku.stok}", fontSize = 11.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }
                        Text("Pilih >", fontSize = 11.sp, color = SelectPrimaryBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PilihBukuSearchBar(text: String, onTextChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = text, onValueChange = onTextChange,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        placeholder = { Text("Cari judul, penulis...") },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
        shape = RoundedCornerShape(12.dp), singleLine = true,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SelectPrimaryBlue, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
    )
}
package com.example.perpustakaandigital.uicontroller.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val BrandBlue = Color(0xFF2563EB)
private val BrandDarkBlue = Color(0xFF1E3A8A)
private val BackgroundWhite = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextLightGray = Color(0xFF64748B)

@Composable
fun DashboardScreen(
    jumlahBuku: Int,
    jumlahDipinjam: Int,
    onKelolaBuku: () -> Unit,
    onPeminjaman: () -> Unit,
    onPengembalian: () -> Unit,
    onRiwayat: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val headerBrush = Brush.verticalGradient(
        colors = listOf(BrandBlue, BrandDarkBlue)
    )

    Scaffold(
        containerColor = BackgroundWhite
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(headerBrush)
            ) {

                Box(
                    modifier = Modifier
                        .offset(x = 220.dp, y = (-80).dp)
                        .size(350.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                )
                Box(
                    modifier = Modifier
                        .offset(x = (-100).dp, y = 50.dp)
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.05f))
                )

                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(50.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.4f))
                            ) {
                                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Halo, Petugas", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(0.9f))
                                Text("Selamat Bekerja!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }


                        IconButton(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.White)
                        }
                    }
                }
            }


            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(110.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CleanStatCard(
                        label = "Total Buku",
                        count = jumlahBuku.toString(),
                        icon = Icons.Outlined.LibraryBooks,
                        iconColor = BrandBlue,
                        modifier = Modifier.weight(1f)
                    )
                    CleanStatCard(
                        label = "Dipinjam",
                        count = jumlahDipinjam.toString(),
                        icon = Icons.Outlined.Book,
                        iconColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                val menuItems = listOf(
                    CleanMenuData("Kelola Buku", Icons.Default.MenuBook, BrandBlue, onKelolaBuku),
                    CleanMenuData("Peminjaman", Icons.Default.AddCircleOutline, Color(0xFF10B981), onPeminjaman),
                    CleanMenuData("Pengembalian", Icons.Default.SwapHoriz, Color(0xFFF59E0B), onPengembalian),
                    CleanMenuData("Riwayat", Icons.Default.History, Color(0xFF8B5CF6), onRiwayat)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(menuItems) { menu ->
                        CleanMenuCard(menu)
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar?", color = TextLightGray) },
            confirmButton = {
                Button(
                    onClick = { showLogoutDialog = false; onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Keluar") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Batal", color = BrandBlue) }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}


@Composable
private fun CleanStatCard(
    label: String,
    count: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Blue.copy(0.1f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(count, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextDark)
                Text(label, style = MaterialTheme.typography.bodySmall, color = TextLightGray)
            }
        }
    }
}

@Composable
private fun CleanMenuCard(menu: CleanMenuData) {
    Card(
        onClick = menu.onClick,
        modifier = Modifier
            .height(140.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp), spotColor = Color.Black.copy(0.05f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(menu.color.copy(alpha = 0.08f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = menu.icon,
                    contentDescription = null,
                    tint = menu.color,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = menu.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
        }
    }
}

private data class CleanMenuData(val title: String, val icon: ImageVector, val color: Color, val onClick: () -> Unit)
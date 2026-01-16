package com.example.perpustakaandigital.uicontroller.navigation

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.perpustakaandigital.uicontroller.screen.*
import com.example.perpustakaandigital.viewmodel.*

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            val authViewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val context = LocalContext.current

            LoginScreen(
                onLoginClick = { username, password ->
                    authViewModel.login(username, password) { sukses, user ->
                        if (sukses) {
                            Toast.makeText(context, "Selamat datang, ${user?.namaPetugas}", Toast.LENGTH_SHORT).show()
                            navController.navigate("dashboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onRegisterLinkClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            val authViewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val context = LocalContext.current

            RegisterScreen(
                onRegisterClick = { nama, username, password ->
                    authViewModel.register(username, password, nama) { sukses, pesan ->
                        Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show()
                        if (sukses) { navController.popBackStack() }
                    }
                },
                onLoginLinkClick = { navController.popBackStack() }
            )
        }


        composable("dashboard") {

            val bukuViewModel: BukuViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val listBuku by bukuViewModel.uiState.collectAsState()

            val peminjamanViewModel: PeminjamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val listPeminjaman by peminjamanViewModel.listPeminjaman.collectAsState()

            val totalBuku = listBuku.size
            val sedangDipinjam = listPeminjaman.count { it.status == "DIPINJAM" }

            DashboardScreen(
                jumlahBuku = totalBuku,
                jumlahDipinjam = sedangDipinjam,
                onKelolaBuku = { navController.navigate("kelola_buku") },
                onPeminjaman = { navController.navigate("peminjaman") },
                onPengembalian = { navController.navigate("pengembalian") },
                onRiwayat = { navController.navigate("riwayat") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("kelola_buku") {
            val viewModel: BukuViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val listBuku by viewModel.uiState.collectAsState()
            val searchText by viewModel.searchQuery.collectAsState()

            KelolaBukuScreen(
                listBuku = listBuku,
                searchText = searchText,
                onSearchChange = { viewModel.onSearchTextChange(it) },
                onTambahBuku = { judul, penulis, kategori, isbn, stok ->
                    viewModel.tambahBuku(judul, penulis, kategori, isbn, stok)
                },
                onEditBuku = { buku -> viewModel.updateBuku(buku) },
                onHapusBuku = { buku -> viewModel.hapusBuku(buku) },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable("peminjaman") {
            val bukuViewModel: BukuViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val listBuku by bukuViewModel.uiState.collectAsState()
            val searchText by bukuViewModel.searchQuery.collectAsState()

            PilihBukuScreen(
                listBuku = listBuku,
                searchText = searchText,
                onSearchChange = { bukuViewModel.onSearchTextChange(it) },
                navigateBack = { navController.popBackStack() },
                onBukuSelected = { buku ->
                    navController.navigate("form_pinjam/${buku.id}/${buku.judul}")
                }
            )
        }

        composable(
            route = "form_pinjam/{idBuku}/{judulBuku}",
            arguments = listOf(
                navArgument("idBuku") { type = NavType.StringType },
                navArgument("judulBuku") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idBuku = backStackEntry.arguments?.getString("idBuku") ?: ""
            val judulBuku = backStackEntry.arguments?.getString("judulBuku") ?: ""

            val peminjamanViewModel: PeminjamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val context = LocalContext.current

            PeminjamanFormScreen(
                awalIdBuku = idBuku,
                awalJudulBuku = judulBuku,
                navigateBack = { navController.popBackStack() },
                onSimpan = { nama, id, judul, tgl, batas ->
                    peminjamanViewModel.tambahPeminjaman(nama, id, judul, tgl, batas) { berhasil, pesan ->
                        Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show()
                        if (berhasil) { navController.popBackStack() }
                    }
                }
            )
        }

        composable("pengembalian") {
            val viewModel: PeminjamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val listPeminjaman by viewModel.listPeminjaman.collectAsState()

            PengembalianScreen(
                listPeminjaman = listPeminjaman.filter { it.status == "DIPINJAM" },
                onKembalikan = { peminjaman -> viewModel.kembalikanBuku(peminjaman) },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable("riwayat") {
            val viewModel: PeminjamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val listPeminjaman by viewModel.listPeminjaman.collectAsState()

            RiwayatPeminjamanScreen(
                listPeminjaman = listPeminjaman,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
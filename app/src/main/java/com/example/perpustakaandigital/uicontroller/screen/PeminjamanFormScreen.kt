package com.example.perpustakaandigital.uicontroller.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeminjamanFormScreen(
    awalIdBuku: String,
    awalJudulBuku: String,
    onSimpan: (String, String, String, String, String) -> Unit,
    navigateBack: () -> Unit
) {

    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val hariIni = remember { formatter.format(Date()) }

    var namaPeminjam by remember { mutableStateOf("") }
    var tanggalPinjam by remember { mutableStateOf(hariIni) }
    var batasKembali by remember { mutableStateOf("") }

    var showDatePickerPinjam by remember { mutableStateOf(false) }
    var showDatePickerKembali by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val PrimaryBlue = Color(0xFF4A90E2)
    val BgColor = Color(0xFFF8F9FA)


    fun convertMillisToDate(millis: Long): String {
        return formatter.format(Date(millis))
    }

    fun isTanggalValid(tglPinjam: String, tglKembali: String): Boolean {
        return try {
            val datePinjam = formatter.parse(tglPinjam)
            val dateKembali = formatter.parse(tglKembali)
            if (dateKembali != null && datePinjam != null) {
                !dateKembali.before(datePinjam)
            } else false
        } catch (e: Exception) { false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulir Peminjaman", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = PrimaryBlue
                )
            )
        },
        containerColor = BgColor
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {

            Text("Buku yang akan dipinjam:", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MenuBook, null, tint = Color.White, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = awalJudulBuku, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "ID Buku: $awalIdBuku", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(24.dp))

            Text("Data Peminjam", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            CustomInput(namaPeminjam, { namaPeminjam = it }, "Nama Lengkap Peminjam", Icons.Default.Person)
            Spacer(modifier = Modifier.height(12.dp))

            ReadonlyDateInput(tanggalPinjam, "Tanggal Pinjam (YYYY-MM-DD)", { showDatePickerPinjam = true })
            Spacer(modifier = Modifier.height(12.dp))

            ReadonlyDateInput(batasKembali, "Batas Kembali (YYYY-MM-DD)", { showDatePickerKembali = true })

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    keyboardController?.hide()

                    if (namaPeminjam.isBlank() || tanggalPinjam.isBlank() || batasKembali.isBlank()) {
                        Toast.makeText(context, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (!isTanggalValid(tanggalPinjam, batasKembali)) {
                        Toast.makeText(context, "❌ Error: Tanggal kembali tidak boleh lebih dulu dari tanggal pinjam!", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    onSimpan(namaPeminjam, awalIdBuku, awalJudulBuku, tanggalPinjam, batasKembali)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("KONFIRMASI PEMINJAMAN", fontWeight = FontWeight.Bold)
            }
        }

        if (showDatePickerPinjam) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = System.currentTimeMillis(),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                        return utcTimeMillis >= System.currentTimeMillis() - (24 * 60 * 60 * 1000)
                    }
                }
            )

            DatePickerDialog(
                onDismissRequest = { showDatePickerPinjam = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            tanggalPinjam = convertMillisToDate(it)
                        }
                        showDatePickerPinjam = false
                    }) { Text("Pilih") }
                }
            ) { DatePicker(state = datePickerState) }
        }

        if (showDatePickerKembali) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000) // Default +7 hari
            )
            DatePickerDialog(
                onDismissRequest = { showDatePickerKembali = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            batasKembali = convertMillisToDate(it)
                        }
                        showDatePickerKembali = false
                    }) { Text("Pilih") }
                }
            ) { DatePicker(state = datePickerState) }
        }
    }
}

@Composable
fun CustomInput(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) }, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(icon, null, tint = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF4A90E2), focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
    )
}
@Composable
fun ReadonlyDateInput(value: String, label: String, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable { onClick() }) {
        OutlinedTextField(
            value = value, onValueChange = {}, label = { Text(label) },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = Color.Gray) },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.Gray,
                disabledContainerColor = Color.White,
                disabledLabelColor = Color.Black
            )
        )

        Box(modifier = Modifier.matchParentSize().clickable(onClick = onClick))
    }
}
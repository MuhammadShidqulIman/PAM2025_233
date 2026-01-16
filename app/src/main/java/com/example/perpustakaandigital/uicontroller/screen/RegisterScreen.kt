package com.example.perpustakaandigital.uicontroller.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String) -> Unit,
    onLoginLinkClick: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val PrimaryBlue = Color(0xFF4A90E2)
    val BgColor = Color(0xFFF8F9FA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(PrimaryBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.PersonAdd, null, tint = PrimaryBlue, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Buat Akun Baru", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Daftar sebagai petugas perpustakaan", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nama, onValueChange = { nama = it },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Badge, null) }, singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Person, null) }, singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Lock, null) }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(image, null) }
            },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { if (nama.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) onRegisterClick(nama, username, password) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("DAFTAR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sudah punya akun? ", color = Color.Gray)
            TextButton(onClick = onLoginLinkClick) {
                Text("Login di sini", color = PrimaryBlue, fontWeight = FontWeight.Bold)
            }
        }
    }
}
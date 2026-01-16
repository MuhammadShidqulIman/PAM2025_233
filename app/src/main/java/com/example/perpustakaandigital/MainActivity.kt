package com.example.perpustakaandigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.perpustakaandigital.ui.theme.PerpustakaanDigitalTheme
import com.example.perpustakaandigital.uicontroller.navigation.AppNavGraph



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PerpustakaanDigitalTheme {
                AppNavGraph()
            }
        }
    }
}

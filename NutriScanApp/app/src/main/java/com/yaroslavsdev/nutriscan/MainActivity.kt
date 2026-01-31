package com.yaroslavsdev.nutriscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yaroslavsdev.nutriscan.data.test.TestFoodDiaryData
import com.yaroslavsdev.nutriscan.data.test.TestHistoryData
import com.yaroslavsdev.nutriscan.ui.NutriScanApp
import com.yaroslavsdev.nutriscan.ui.theme.NutriScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Подгрузка данных для тестирования
        TestHistoryData.addTestHistory()
        TestFoodDiaryData.addTestDiaryData()

        setContent {
            NutriScanTheme {
                NutriScanApp()
            }
        }
    }
}
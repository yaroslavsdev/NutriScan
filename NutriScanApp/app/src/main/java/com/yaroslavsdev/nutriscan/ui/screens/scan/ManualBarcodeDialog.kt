package com.yaroslavsdev.nutriscan.ui.screens.scan

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.yaroslavsdev.nutriscan.domain.usecase.BarcodeValidator.isValidBarcodeSum

@Composable
fun ManualBarcodeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var code by remember { mutableStateOf("") }

    val isValid = code.length == 8 || code.length == 13
    val isCheckSumValid = isValid && isValidBarcodeSum(code)
    val showError = code.isNotEmpty() && !isCheckSumValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ручной ввод") },
        text = {
            OutlinedTextField(
                value = code,
                onValueChange = { newText ->
                    if (newText.length <= 13 && newText.all { it.isDigit() }) {
                        code = newText
                    }
                },
                label = { Text("Начните вводить код") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = showError,
                supportingText = {
                    if (!isValid) {
                        Text("Нужно 8 или 13 цифр")
                    } else if (!isCheckSumValid) {
                        Text("Неверная контрольная сумма")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(code) },
                enabled = isCheckSumValid
            ) {
                Text("Найти")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
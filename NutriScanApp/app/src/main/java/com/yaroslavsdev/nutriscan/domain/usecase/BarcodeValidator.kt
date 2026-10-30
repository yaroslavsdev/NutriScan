package com.yaroslavsdev.nutriscan.domain.usecase

object BarcodeValidator {
    fun isValidBarcodeSum(barcode: String?): Boolean {
        if (barcode == null) {
            return false
        }
        if (barcode.length != 8 && barcode.length != 13) {
            return false
        }
        if (!barcode.all { it.isDigit() }) {
            return false
        }

        val checkDigit = barcode.last() - '0'
        val payload = barcode.substring(0, barcode.length - 1).reversed().map{ it - '0' }

        var sum = 0
        for (i in 0..<payload.size) {
            val weight = if (i % 2 == 0) 3 else 1
            sum += payload[i] * weight
        }

        val needCheckDigit = (10 - (sum % 10)) % 10
        return checkDigit == needCheckDigit
    }
}
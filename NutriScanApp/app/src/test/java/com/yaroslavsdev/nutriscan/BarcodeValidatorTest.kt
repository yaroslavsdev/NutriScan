package com.yaroslavsdev.nutriscan

import com.yaroslavsdev.nutriscan.domain.usecase.BarcodeValidator.isValidBarcodeSum
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BarcodeValidatorTest {
    @Test
    fun nullBarcodeReturnsFalse() {
        assertFalse({ isValidBarcodeSum(null) })
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "123", "1234567", "123456789012", "12345678901234"])
    fun wrongLengthReturnsFalse(barcode: String) {
        assertFalse({ isValidBarcodeSum(barcode) })
    }

    @ParameterizedTest
    @ValueSource(strings = ["abacaba", "текст-?/", "a1234567", "1234567a", "1234567!"])
    fun incorrectFormatReturnsFalse(barcode: String) {
        assertFalse({ isValidBarcodeSum(barcode) })
    }

    @ParameterizedTest
    @ValueSource(strings = ["4006381333931", "9780201379624", "73513537"])
    fun shouldReturnTrueForValidBarcodes(barcode: String) {
        assertTrue({ isValidBarcodeSum(barcode) })
    }

    @ParameterizedTest
    @ValueSource(strings = ["00000001", "1234567890123", "73513530"])
    fun shouldReturnFalseForInvalidBarcodes(barcode: String) {
        assertFalse({ isValidBarcodeSum(barcode) })
    }
}
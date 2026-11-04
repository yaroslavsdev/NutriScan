package com.yaroslavsdev.nutriscan

import com.yaroslavsdev.nutriscan.ui.model.AllergenLabels
import com.yaroslavsdev.nutriscan.ui.model.MealTypeLabels
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LabelsTest {

    @Test
    fun knownAllergenHasRussianName() {
        assertEquals("Глютен", AllergenLabels.displayName("gluten"))
    }

    @Test
    fun unknownAllergenReturnsKey() {
        assertEquals("new_allergen", AllergenLabels.displayName("new_allergen"))
    }

    @Test
    fun fourMealTypes() {
        assertEquals(4, MealTypeLabels.all.size)
    }
}
package com.yaroslavsdev.nutriscan.ui.model

object AllergenLabels {
    private val displayNames = mapOf(
        "lactose" to "Лактоза",
        "eggs" to "Яйца",
        "gluten" to "Глютен",
        "peanuts" to "Арахис",
        "tree_nuts" to "Орехи",
        "soya" to "Соя",
        "fish" to "Рыба",
        "seafood" to "Морепродукты",
        "sesame" to "Кунжут"
    )

    fun displayName(key: String): String = displayNames[key] ?: key
}
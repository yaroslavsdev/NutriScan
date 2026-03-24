package com.yaroslavsdev.nutriscan.ui.screens.allergens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.domain.model.Allergen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllergensScreen(
    navController: NavController,
    viewModel: AllergensViewModel = koinViewModel()
) {
    val allergens by viewModel.allergens.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Выберите ваши аллергены", style = MaterialTheme.typography.headlineMedium)
        Text("Это поможет нам предупреждать вас об опасности", color = Color.Gray)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allergens) { allergen ->
                AllergenCard(allergen) { viewModel.toggleAllergen(allergen.id) }
            }
        }

        Button(
            onClick = { viewModel.saveAndContinue { navController.navigate("main_root") } },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Готово")
        }
    }
}

@Composable
fun AllergenCard(allergen: Allergen, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable { onClick() },
        border = BorderStroke(
            2.dp,
            if (allergen.isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (allergen.isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = allergen.iconRes),
                contentDescription = allergen.name,
                modifier = Modifier.size(64.dp),
                tint = if (allergen.isSelected) MaterialTheme.colorScheme.primary else Color.Black
            )
            Text(allergen.name, fontWeight = FontWeight.Medium)
        }
    }
}
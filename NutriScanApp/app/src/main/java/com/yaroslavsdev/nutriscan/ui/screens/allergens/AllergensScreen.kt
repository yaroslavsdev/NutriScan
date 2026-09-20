package com.yaroslavsdev.nutriscan.ui.screens.allergens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.R
import com.yaroslavsdev.nutriscan.domain.model.Allergen
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllergensScreen(
    navController: NavController,
    fromRegistration: Boolean,
    viewModel: AllergensViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val allergens by viewModel.allergens.collectAsState()
    val isError by viewModel.isError.collectAsState()
    val networkErrorMessage = stringResource(R.string.error_network_save)

    LaunchedEffect(isError) {
        if (isError && allergens.isNotEmpty()) {
            Toast.makeText(context, networkErrorMessage, Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    AllergensContent(
        allergens = allergens,
        onAllergenClick = { viewModel.toggleAllergen(it) },
        onDoneClick = {
            viewModel.saveAndContinue {
                if (fromRegistration) {
                    navController.navigate(Screen.Main.route) {
                        popUpTo("auth") { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
            }
        }
    )
}


@Composable
fun AllergensContent(
    allergens: List<Allergen>,
    onAllergenClick: (String) -> Unit,
    onDoneClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        Text("Выберите ваши аллергены", style = MaterialTheme.typography.headlineSmall)
        Text("Это поможет предупреждать об опасности", color = Color.Gray)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allergens) { allergen ->
                AllergenCard(allergen) { onAllergenClick(allergen.id) }
            }
        }

        Button(
            onClick = onDoneClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Готово")
        }
    }
}

@Composable
fun AllergenCard(allergen: Allergen, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(150.dp).clickable { onClick() },
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
            Image(
                painter = painterResource(allergen.iconRes),
                contentDescription = allergen.name,
                modifier = Modifier.size(64.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(allergen.name, fontWeight = FontWeight.Medium)
        }
    }
}
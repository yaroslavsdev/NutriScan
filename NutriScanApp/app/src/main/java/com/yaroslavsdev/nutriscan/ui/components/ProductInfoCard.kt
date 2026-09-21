package com.yaroslavsdev.nutriscan.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yaroslavsdev.nutriscan.ui.model.AllergenLabels
import com.yaroslavsdev.nutriscan.ui.theme.SafeGreen
import com.yaroslavsdev.nutriscan.ui.theme.SafeGreenContainer
import com.yaroslavsdev.nutriscan.ui.theme.UnsafeRed
import com.yaroslavsdev.nutriscan.ui.theme.UnsafeRedContainer

@Composable
fun ProductInfoCard(
    name: String,
    brand: String?,
    ingredients: String,
    calories: Float,
    proteins: Float,
    fats: Float,
    carbs: Float,
    matchedAllergens: List<String>,
    onAddToDiaryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSafe = matchedAllergens.isEmpty()
    var ingredientsExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {

           Surface(
               color = if (isSafe) {
                   SafeGreenContainer
               } else {
                   UnsafeRedContainer
               },
               shape = RoundedCornerShape(8.dp)
           ) {
               Text(
                   text = if (isSafe) "Безопасно" else "Опасно",
                   color = if (isSafe) {
                       SafeGreen
                   } else {
                       UnsafeRed
                   },
                   fontWeight = FontWeight.Bold,
                   style = MaterialTheme.typography.titleMedium,
                   modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
               )
           }

            Spacer(Modifier.height(12.dp))

            Text(name, style = MaterialTheme.typography.titleLarge)
            Text("Бренд: ${brand ?: "Не указан"}")

            if (!isSafe) {
                Text(
                    text = "Содержит аллергены: ${matchedAllergens.joinToString(", ") { AllergenLabels.displayName(it) } }",
                    color = UnsafeRed,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(Modifier.height(12.dp))

            Text("Калории: $calories", modifier = Modifier.padding(top = 8.dp))
            Text("Белки: $proteins")
            Text("Жиры: $fats")
            Text("Углеводы: $carbs")

            Spacer(Modifier.height(12.dp))

            Text(
                text = ingredients,
                maxLines = if (ingredientsExpanded) 200 else 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )

            TextButton(
                onClick = { ingredientsExpanded = !ingredientsExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ingredientsExpanded) "Свернуть" else "Показать состав полностью")
            }

            Button(
                onClick = onAddToDiaryClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Добавить в дневник питания")
            }
        }
    }
}
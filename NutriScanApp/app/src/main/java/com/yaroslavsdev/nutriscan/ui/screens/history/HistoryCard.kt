import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaroslavsdev.nutriscan.ui.model.ScannedProductUi
import java.time.format.DateTimeFormatter

@Composable
fun HistoryCard(item: ScannedProductUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            // Название
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            // Штрих-код
            Text(
                text = "Штрих-код: ${item.barcode}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            Text("Калории: ${item.calories}")
            val capitalizedIngredients = item.ingredients.replaceFirstChar { it.uppercase() }
            Text(capitalizedIngredients)

            Spacer(Modifier.height(8.dp))

            Text(
                text = item.scannedAt.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                ),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}

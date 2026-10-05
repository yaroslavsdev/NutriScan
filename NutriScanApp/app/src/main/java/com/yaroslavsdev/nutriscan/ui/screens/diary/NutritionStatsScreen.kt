package com.yaroslavsdev.nutriscan.ui.screens.diary

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaroslavsdev.nutriscan.ui.theme.CarbColor
import com.yaroslavsdev.nutriscan.ui.theme.FatColor
import com.yaroslavsdev.nutriscan.ui.theme.ProteinColor

@Composable
fun NutritionStatsScreen(
    proteins: Float,
    fats: Float,
    carbs: Float,
    totalWeight: Float,
    onBack: () -> Unit
) {
    val total = proteins + fats + carbs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 10.dp)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            Text("Статистика БЖУ", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (total <= 0) {
                Text("Нет данных за этот день")
            } else {
                Canvas(
                    modifier = Modifier
                        .size(220.dp)
                        .padding(20.dp)
                ) {
                    val strokeWidth = 40.dp.toPx()
                    var startAngle = -90f

                    val slices = listOf(
                        proteins to ProteinColor,
                        fats to FatColor,
                        carbs to CarbColor
                    )

                    slices.forEach { (value, color) ->
                        val currentAngle = 360f * (value / total)

                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = currentAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth)
                        )

                        startAngle += currentAngle
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Общий вес: $totalWeight г.",
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(14.dp))

        DiagramLabel("Белки", proteins, total, ProteinColor)
        Spacer(Modifier.height(12.dp))
        DiagramLabel("Жиры", fats, total, FatColor)
        Spacer(Modifier.height(12.dp))
        DiagramLabel("Углеводы", carbs, total, CarbColor)
    }
}


@Composable
private fun DiagramLabel(
    label: String,
    grams: Float,
    total: Float,
    color: Color
) {
    val percent = if (total > 0f) (grams / total * 100).toInt() else 0

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "$label: ${grams.toInt()} г. ($percent%)",
            fontWeight = FontWeight.Medium
        )
    }
}
package com.yaroslavsdev.nutriscan.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    var isNavigating by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, "Не удалось загрузить профиль", Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text("Мой Профиль", style = MaterialTheme.typography.headlineMedium, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        profile?.let {
            Text("Имя: ${it.username}", fontWeight = FontWeight.Bold)
            Text("Почта: ${it.email}", fontWeight = FontWeight.Bold)
            Text("Лимит калорий: ${it.dailyCalorieGoal}", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (!isNavigating) {
                    isNavigating = true
                    navController.navigate(Screen.EditNameScreen.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Изменить имя")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (!isNavigating) {
                    isNavigating = true
                    navController.navigate(Screen.ChangePasswordScreen.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Сменить пароль")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (!isNavigating) {
                    isNavigating = true
                    navController.navigate(Screen.AllergensScreen.createRoute(false))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Настроить аллергены")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (!isNavigating) {
                    isNavigating = true
                    navController.navigate(Screen.NutritionScreen.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Параметры питания")
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = {
                viewModel.logout {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Выйти")
        }
    }
}
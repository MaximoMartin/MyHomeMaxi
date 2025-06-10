package com.example.myhomemaxi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myhomemaxi.ui.screens.AccionesScreen
import com.example.myhomemaxi.ui.screens.HomeScreen
import com.example.myhomemaxi.viewmodel.MainViewModel
import com.example.myhomemaxi.ui.screens.HistorialScreen

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("acciones/{entidadId}") { backStackEntry ->
            val entidadId = backStackEntry.arguments?.getString("entidadId")?.toIntOrNull()
            entidadId?.let {
                AccionesScreen(navController, viewModel, it)
            }
        }
        composable("historial") {
            HistorialScreen(navController, viewModel)
        }
    }
}

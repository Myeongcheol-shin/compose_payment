package com.shino72.wallet.navigator

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.shino72.wallet.screen.AddScreen
import com.shino72.wallet.screen.InfoScreen
import com.shino72.wallet.screen.ListScreen
import com.shino72.wallet.screen.MainScreen
import com.shino72.wallet.viewmodels.InfoViewModel


@Composable
fun PaymentNavigation(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Screens.MainScreen.name){
        composable(Screens.MainScreen.name){
            MainScreen(navController = navController)
        }
        composable(Screens.AddScreen.name){
            AddScreen(navController =  navController)
        }
        composable(Screens.ListScreen.name){
            ListScreen(navController = navController)
        }
        composable("InfoScreen/{ott}",
            listOf(navArgument("ott"){type = NavType.StringType})
            ){
            val viewModel = viewModel<InfoViewModel>()
            InfoScreen(navController = navController, viewModel = viewModel)
        }
    }
}
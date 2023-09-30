package com.shino72.compose_payment.screen

enum class Screens {
    MainScreen,
    AddScreen;
    companion object{
        fun fromRoute(route : String?) : Screens
            = when(route){
                MainScreen.name -> MainScreen
                AddScreen.name -> AddScreen
                else -> MainScreen
            }
    }
}
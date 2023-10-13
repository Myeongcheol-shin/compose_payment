package com.shino72.wallet.navigator

enum class Screens {
    MainScreen,
    AddScreen,
    ListScreen,
    InfoScreen;
    companion object{
        fun fromRoute(route : String?) : Screens
            = when(route){
                MainScreen.name -> MainScreen
                AddScreen.name -> AddScreen
                InfoScreen.name -> InfoScreen
                ListScreen.name -> ListScreen
                else -> MainScreen
            }
    }
}
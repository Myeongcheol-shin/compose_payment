package com.shino72.wallet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.shino72.wallet.navigator.PaymentNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyApp : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // db 데이터 가져오기

            val controller = rememberNavController()
            PaymentNavigation(navController = controller)
        }
    }
}


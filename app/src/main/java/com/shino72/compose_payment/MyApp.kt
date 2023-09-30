package com.shino72.compose_payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.shino72.compose_payment.navigator.PaymentNavigation

class MyApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberNavController()
            PaymentNavigation(navController = controller)
        }
    }
}

// db 데이터 가져오는 과정


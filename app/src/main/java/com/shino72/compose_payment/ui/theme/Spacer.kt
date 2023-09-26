package com.shino72.compose_payment.ui.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 여백 10dp짜리
@Composable
fun setSpacer10() {
    Spacer(modifier = Modifier.height(15.dp))
}

// 여백 20dp짜리
@Composable
fun setSpacer20() {
    Spacer(modifier = Modifier.height(15.dp))
}

// 여백 5dp짜리
@Composable
fun setSpacer5() {
    Spacer(modifier = Modifier.height(5.dp))
}
package com.shino72.compose_payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shino72.compose_payment.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_paymentTheme {
                Column(modifier =
                Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                ) {
                    setMyPaymentList()
                    setThisMonthPaymentView()
                    setThisPaymentPriceView()
                }
            }
        }
    }

}
// 내 정기결제 목록
@Composable
fun setMyPaymentList() {
    val scrollState = rememberScrollState()
    setSubTitleTextView(msg = "내 정기결제 목록")
    setSpacer10()
    Row(modifier = Modifier
        .background(color = DarkGrey, RoundedCornerShape(8.dp))
        .fillMaxWidth()
        .padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
        .horizontalScroll(scrollState)) {
            for(i in (0..10)){
                Box(modifier = Modifier
                    .padding(start = 10.dp)
                    .height(80.dp)
                    .width(80.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(5.dp))) {
                    Text(i.toString())
                }
            }

    }
    setSpacer20()
}
// 이번달 결제 예정 금액
@Composable
fun setThisPaymentPriceView() {
    setSubTitleTextView(msg = "이번 달 결제 금액")
    setSpacer10()
    Row(modifier = Modifier
        .background(color = DarkGrey, RoundedCornerShape(8.dp))
        .fillMaxHeight()
        .fillMaxWidth() ,
        verticalAlignment = Alignment.CenterVertically
        ) {

        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally

            ) {
            Text(text = "총 결제 금액", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            setSpacer5()
            Text(text = "20000원", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 28.sp)

        }
        Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "남은 결제 금액", color = Color.White, fontWeight = FontWeight.Bold,fontSize = 17.sp)
            setSpacer5()
            Text(text = "20000원", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 28.sp)

        }
    }

    setSpacer20()
}



// 이번달 결제 예정
@Composable
fun setThisMonthPaymentView(){
    val scrollState = rememberScrollState()
    setSubTitleTextView(msg = "이번 달 결제 예정")
    setSpacer10()
    Column(modifier = Modifier
        .background(color = DarkGrey, RoundedCornerShape(8.dp))
        .fillMaxWidth()
        .height(350.dp)
        .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
        .verticalScroll(scrollState)
    ) {
        for(i in (0..10)){
            thisMonthPaymentItem()
        }
    }
    setSpacer20()
}

// 이번달 아이템
@Composable
fun thisMonthPaymentItem(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)) {
        Box(modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(5.dp))
            .width(60.dp)
            .height(60.dp)) {}
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = "1", color = Color.White, fontSize = 20.sp)
            Text(text = "1", color = Color.White, fontSize = 15.sp)
        }
        Text(text = "1")
    }
}



// sub 제목
@Composable
fun setSubTitleTextView(msg: String) {
    Text(msg,
        style = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    )
}


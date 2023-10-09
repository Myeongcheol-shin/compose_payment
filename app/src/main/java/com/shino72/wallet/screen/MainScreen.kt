package com.shino72.wallet.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.shino72.wallet.data.OttData
import com.shino72.wallet.data.OttState
import com.shino72.wallet.db.entity.OttDB
import com.shino72.wallet.ui.theme.Compose_paymentTheme
import com.shino72.wallet.ui.theme.DarkGrey
import com.shino72.wallet.ui.theme.setSpacer10
import com.shino72.wallet.ui.theme.setSpacer20
import com.shino72.wallet.ui.theme.setSpacer5
import com.shino72.wallet.viewmodels.DBViewModel
import java.time.LocalDate

@Composable
fun MainScreen(navController: NavController) {

    val viewModel : DBViewModel = hiltViewModel()
    // observe data
    viewModel.getAllDBData()
    val dbState by viewModel.dbState.collectAsState()
    Compose_paymentTheme {
        Column(modifier =
        Modifier
            .padding(15.dp)
            .fillMaxWidth()
        ) {
            setMyPaymentList(navController, dbState)
            setThisMonthPaymentView(dbState)
            setThisPaymentPriceView(dbState)
        }
    }
}
// 내 정기결제 목록
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun setMyPaymentList(navController: NavController, dbState: OttState) {
    val scrollState = rememberScrollState()
    val item = dbState.db
    Row(verticalAlignment = Alignment.CenterVertically) {
        setSubTitleTextView(msg = "내 정기결제 목록")
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            navController.navigate("AddScreen")
        }){
            androidx.compose.material.Icon(imageVector = Icons.Filled.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.width(40.dp))
        }
    }
    setSpacer10()
    Row(modifier = Modifier
        .background(color = DarkGrey, RoundedCornerShape(8.dp))
        .fillMaxWidth()
        .padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
        .horizontalScroll(scrollState)) {
        item?.forEach {
            val dt = OttData.getOttWithName(it.platform)
            if(dt != null){
                val uri = getImageUriForName(name = dt.name)
                Box(modifier = Modifier
                    .padding(start = 10.dp)
                    .height(80.dp)
                    .width(80.dp)
                    .clip(shape = RoundedCornerShape(10.dp))) {
                    GlideImage(
                        model = uri,
                        contentScale = ContentScale.FillHeight,
                        contentDescription = dt.name
                    )
                }
            }
        }
    }
    setSpacer20()
}
// 이번달 결제 예정 금액
@Composable
fun setThisPaymentPriceView(dbState: OttState) {
    val today = LocalDate.now().dayOfMonth
    val price = dbState.db?.filter { it.duedate >= today }?.sumOf { it.price }
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
            Text(text = "${dbState.db?.sumOf { it.price }}원", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 28.sp)

        }
        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "남은 결제 금액", color = Color.White, fontWeight = FontWeight.Bold,fontSize = 17.sp)
            setSpacer5()
            Text(text = "${dbState.db?.filter { it.duedate >= today }?.sumOf { it.price }}원", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 28.sp)

        }
    }
    setSpacer20()
}



// 이번달 결제 예정
@Composable
fun setThisMonthPaymentView(dbState: OttState){
    val today = LocalDate.now().dayOfMonth
    val item = dbState.db
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
        item?.filter { it.duedate >= today }?.forEach {
            thisMonthPaymentItem(it)
        }
    }
    setSpacer20()
}

// 이번달 아이템
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun thisMonthPaymentItem(ottDB : OttDB){
    val uri = getImageUriForName(name = ottDB.platform)
    val today = LocalDate.now().dayOfMonth
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)) {
        Box(modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .width(60.dp)
            .height(60.dp)) {
            GlideImage(
                model = uri,
                contentScale = ContentScale.FillHeight,
                contentDescription = ottDB.name
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier.padding(start = 10.dp).align(Alignment.CenterVertically)) {
            Text(text = ottDB.korean, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "${ottDB.price}원", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(text =  getDate(ottDB.duedate), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}


private fun getDate(date : Int) : String {
    val today = LocalDate.now().dayOfMonth
    if(date == today) return "오늘"
    return "${date - today}일 후"
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


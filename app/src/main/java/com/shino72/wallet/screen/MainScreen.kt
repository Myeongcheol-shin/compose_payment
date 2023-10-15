package com.shino72.wallet.screen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.shino72.wallet.MyApplication
import com.shino72.wallet.MyApplication.Companion.prefs
import com.shino72.wallet.data.OttData
import com.shino72.wallet.data.OttState
import com.shino72.wallet.db.entity.OttDB
import com.shino72.wallet.receiver.AlarmReceiver
import com.shino72.wallet.ui.theme.Compose_paymentTheme
import com.shino72.wallet.ui.theme.DarkGrey
import com.shino72.wallet.ui.theme.setSpacer10
import com.shino72.wallet.ui.theme.setSpacer20
import com.shino72.wallet.ui.theme.setSpacer5
import com.shino72.wallet.viewmodels.DBViewModel
import java.time.LocalDate
import java.util.Calendar
import javax.annotation.meta.When

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun setMyPaymentList(navController: NavController, dbState: OttState) {
    val scrollState = rememberScrollState()
    val item = dbState.db
    val context = LocalContext.current
    val iconColor = when(prefs.EditStatus) {
        true -> {Color.Red}
        false -> {Color.White}
    }
    val alarmState = remember {
        mutableStateOf(iconColor)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        setSubTitleTextView(msg = "내 정기결제 목록")
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            val prefs = MyApplication.prefs
            when(prefs.EditStatus) {
                true -> {
                    alarmState.value = Color.White
                    setAlarm(type = false, context = context)
                }
                false -> {
                    setAlarm(type = true, context = context)
                    alarmState.value = Color.Red
                }
            }
            prefs.EditStatus = !prefs.EditStatus
        }) {
            androidx.compose.material.Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Notifications", tint = alarmState.value, modifier = Modifier.width(40.dp))
        }
        IconButton(onClick = {
            navController.navigate("ListScreen")
        }){
            androidx.compose.material.Icon(imageVector = Icons.Filled.List, contentDescription = "List", tint = Color.White, modifier = Modifier.width(40.dp))
        }
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
        .height(80.dp)
        .padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
        .horizontalScroll(scrollState)) {

        item?.forEach {
            val dt = OttData.getOttWithName(it.platform)
            if(dt != null){
                val uri = getImageUriForName(name = dt.name)
                Box(modifier = Modifier
                    .padding(start = 10.dp)
                    .height(80.dp)
                    .aspectRatio(1f)
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
        Column(modifier = Modifier
            .padding(start = 10.dp)
            .align(Alignment.CenterVertically)) {
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

// false : 알람 해제 / ture : 알람 설정
fun setAlarm(type : Boolean, context : Context)
{
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    var pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_IMMUTABLE
    )
    when(type)
    {
        false -> {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(context,"알람 OFF", Toast.LENGTH_SHORT).show()
        }
        true -> {
            // 권한 체크 API 33 이상
            val cd = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY,9)
                set(Calendar.MINUTE, 0)
            }
            if(cd.before(Calendar.getInstance())) {
                cd.add(Calendar.DAY_OF_MONTH,1)
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                cd.timeInMillis,
                pendingIntent
            )
            Toast.makeText(context,"알람 ON", Toast.LENGTH_SHORT).show()
        }
    }
}

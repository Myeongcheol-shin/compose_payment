package com.shino72.wallet.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.shino72.wallet.data.OttData
import com.shino72.wallet.db.entity.OttDB
import com.shino72.wallet.ui.theme.Compose_paymentTheme
import com.shino72.wallet.ui.theme.DarkGrey
import com.shino72.wallet.ui.theme.setSpacer10
import com.shino72.wallet.ui.theme.setSpacer20
import com.shino72.wallet.ui.theme.setSpacer7
import com.shino72.wallet.viewmodels.DBViewModel
import com.shino72.wallet.viewmodels.InfoViewModel

@Composable
fun InfoScreen(navController: NavController, viewModel: InfoViewModel){
    val dbViewModel : DBViewModel = hiltViewModel()
    val name = viewModel.ott ?: ""
    val context = LocalContext.current
    if (name == "") navController.popBackStack()
    var ts by remember {
        mutableStateOf("")
    }
    Compose_paymentTheme {
        Column(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()) {
            appNameAndImageView(name = viewModel.ott ?: "")
            setSpacer20()
            content(name = "요금제",viewModel)
            Spacer(modifier = Modifier.weight(1f))
            content(name = "가격",viewModel)
            Spacer(modifier = Modifier.weight(1f))
            content(name = "결제일",viewModel)
            Spacer(modifier = Modifier.weight(1f))
            content(name = "메모",viewModel)
            Spacer(modifier = Modifier.weight(3f))
            TextField(value = ts, onValueChange = {t -> ts = t} )
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (contentsCheck(viewModel)) {
                            dbViewModel.insertDB(
                                OttDB(
                                    name = viewModel.name.value!!,
                                    price = viewModel.price.value!!.toInt(),
                                    duedate = viewModel.duedate.value!!.toInt(),
                                    memo = viewModel.memo.value,
                                    platform = viewModel.ott!!,
                                    korean = OttData.getOttWithName(viewModel.ott!!)!!.korean
                                )
                            )
                            Toast
                                .makeText(context, "등록 완료", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                            navController.popBackStack()
                        } else {
                            Toast
                                .makeText(context, "등록 실패 : 정확한 데이터를 입력해주세요", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .background(shape = RoundedCornerShape(10.dp), color = DarkGrey)) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "등록", modifier = Modifier.padding(15.dp),style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
                Box(
                    Modifier
                        .weight(1f)
                        .clickable {
                            navController.popBackStack()
                        },contentAlignment = Alignment.Center) {
                    Text(text = "취소", modifier = Modifier.padding(15.dp),style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
fun contentsCheck(viewModel: InfoViewModel) : Boolean {
    if(viewModel.price.value == null || viewModel.price.value == "") return false
    if(viewModel.name.value == null || viewModel.name.value == "") return false
    if(viewModel.duedate.value == null || viewModel.duedate.value == "" || viewModel.duedate.value.toInt() !in (1..31)) return false
    return true
}
@Composable
fun content(name : String, viewModel : InfoViewModel) {
    setSpacer10()
   Column(modifier = Modifier.fillMaxWidth()) {
       Text(text = name, style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))
       setSpacer7()
       Box(modifier = Modifier
           .height(50.dp)
           .fillMaxWidth()
           .background(shape = RoundedCornerShape(15.dp), color = DarkGrey)) {
           when(name){
               "요금제" ->{
                   val name = viewModel.name.collectAsState()
                   TextField(modifier = Modifier.fillMaxWidth() ,value = name.value, textStyle = TextStyle(color = Color.White), onValueChange = viewModel::setName)
               }
               "가격" ->{
                   val price = viewModel.price.collectAsState()
                   TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),modifier = Modifier.fillMaxWidth() ,value = price.value, textStyle = TextStyle(color = Color.White), onValueChange = viewModel::setPrice)}
               "결제일" ->{
                   val duedate = viewModel.duedate.collectAsState()
                   TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),modifier = Modifier.fillMaxWidth() ,value = duedate.value,textStyle = TextStyle(color = Color.White), onValueChange = viewModel::setDuedata)}
               "메모" ->{
                   val memo = viewModel.memo.collectAsState()
                   TextField(modifier = Modifier.fillMaxWidth() ,value = memo.value, textStyle = TextStyle(color = Color.White), onValueChange = viewModel::setMemo)}
           }
       }
   }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun appNameAndImageView(name: String){
    val uri = getImageUriForName(name = name)
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Box(modifier = Modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .width(80.dp)
            .height(80.dp)
            ) {
            GlideImage(
                model = uri,
                contentScale = ContentScale.FillHeight,
                contentDescription = name
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .height(80.dp)
            .padding(start = 15.dp)
            .background(color = DarkGrey, shape = RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center
        ) {
            Text(text = OttData.getOttWithName(name)?.korean ?: "", modifier = Modifier.padding(bottom = 5.dp), style = TextStyle(Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun getImageUriForName(name : String) : Uri{
    val context = LocalContext.current
    val imgId = context.resources.getIdentifier("appicon_$name", "drawable","com.shino72.wallet")
    val uri = Uri.parse("android.resource://" + context.packageName + "/" + imgId);
    return uri
}
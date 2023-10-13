package com.shino72.wallet.screen

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.shino72.wallet.db.entity.OttDB
import com.shino72.wallet.ui.theme.Compose_paymentTheme
import com.shino72.wallet.ui.theme.DarkGrey
import com.shino72.wallet.ui.theme.setSpacer10
import com.shino72.wallet.viewmodels.DBViewModel


@Composable
fun ListScreen(navController: NavController){
    val viewModel : DBViewModel = hiltViewModel()

    Compose_paymentTheme {
        Column(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()) {

            setTitleView(navController = navController,"나의 OTT 목록")
            ottList(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ottList(viewModel : DBViewModel){
    var deleteState by remember {
        mutableStateOf(false)
    }
    /*
    var editState by remember{
        mutableStateOf(false)
    }

     */
    var nowOtt by remember{
        mutableStateOf(OttDB(
            -1,"","","",0,0,""
        ))
    }
    if(deleteState){swipeDialog(DialogType.Remove, ott = nowOtt, viewModel = viewModel){deleteState = false} }
    // if(editState){ swipeDialog(DialogType.Edit,  ott = nowOtt, viewModel =  viewModel) {editState = false}}
    val db = viewModel.getAllDBData()
    val context = LocalContext.current
    val dbState by viewModel.dbState.collectAsState()
    if(dbState.db != null){
        LazyColumn(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(5.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){itemsIndexed(items = dbState.db ?: emptyList(), key = { _, listItem ->
            listItem.hashCode()
        }){
            _, item ->
            val state = rememberDismissState(
                confirmStateChange = {
                    nowOtt = item
                    if(it == DismissValue.DismissedToStart){
                        deleteState = true
                        false
                    }
                    /*
                    else if(it == DismissValue.DismissedToEnd){
                        editState = true
                        false
                    }

                     */
                    else true
                }
            )
            SwipeToDismiss(state = state, directions = setOf(DismissDirection.EndToStart), dismissThresholds = {direction-> FractionalThreshold(0.5f)  }, background ={
                val color = Color.Red.copy(alpha = 0.7f)
                /*
               val color = when(state.dismissDirection){
                   DismissDirection.StartToEnd -> Color.Green.copy(alpha = 0.7f)
                   DismissDirection.EndToStart -> Color.Red.copy(alpha = 0.7f)
                   null -> Color.Transparent
               }
                 */
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = color, RoundedCornerShape(8.dp))) {
                    /*
                    Row(modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp)){
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                        Text(text = "수정", color = Color.White, fontSize = 15.sp)
                    }

                     */
                    Row(modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp)){
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        Text(text = "삭제", color = Color.White, fontSize = 15.sp)
                    }
                }
            }, dismissContent = {
                listInnerBox(ottDB = item)
            })
        }
    }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun listInnerBox(ottDB: OttDB){
    Box(
        Modifier
            .background(color = DarkGrey, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(10.dp)) {
        val uri = getImageUriForName(name = ottDB.platform)
        Row(Modifier.fillMaxWidth()) {
            Box(modifier = Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .width(70.dp)
                .height(70.dp)) {
                GlideImage(
                    model = uri,
                    contentScale = ContentScale.FillHeight,
                    contentDescription = ottDB.name
                )
            }
            Column(Modifier.padding(start = 10.dp)) {
                Text(text = ottDB.korean, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
                Text(text = "${ottDB.price}원", color = Color.White, fontSize = 15.sp,textAlign = TextAlign.Center)
                ottDB.memo?.let {
                    if(it.isNotBlank()) Text(text = "메모 : $it",color = Color.White, fontSize = 15.sp,textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "매월 ${ottDB.duedate}일", Modifier.padding(end = 5.dp) ,color = Color.White, fontSize = 18.sp)

        }
    }
    setSpacer10()
}
@Composable
fun swipeDialog(type : DialogType, viewModel : DBViewModel, ott : OttDB?,onChangeState: () -> Unit){
    AlertDialog(
        onDismissRequest = {onChangeState()},
        title = {
            val title = when(type) {
                DialogType.Remove ->{"삭제하시겠습니까?"}
                //DialogType.Edit ->{"수정하시겠습니까?"}
            }
            Text(text = title)
        },
        text = {
            val text = when(type){
                DialogType.Remove->{
                    "해당 OTT 데이터가 삭제됩니다."
                }
                /*
                DialogType.Edit->{
                    "수정 화면으로 이동합니다."
                }

                 */
            }
            Text(text = text)
        },
        buttons = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = {
                    when(type) {
                        DialogType.Remove -> {
                            ott?.let {
                                if(ott.uid != -1){
                                    viewModel.deleteDB(ott)
                                }
                            }
                        }
                        else -> {

                        }
                    }
                    onChangeState()
                }) {
                    Text("확인")
                }
                TextButton(onClick = {
                    onChangeState()
                }) {
                    Text("취소")
                }
            }
        },
        shape = RoundedCornerShape(10.dp)
    )
}

enum class DialogType{
    Remove,
    //Edit
}
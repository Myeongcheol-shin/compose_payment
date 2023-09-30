package com.shino72.compose_payment.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.shino72.compose_payment.data.OttData
import com.shino72.compose_payment.data.OttWithId
import com.shino72.compose_payment.ui.theme.Compose_paymentTheme
import com.shino72.compose_payment.ui.theme.DarkGrey
import kotlin.random.Random

@Composable
fun AddScreen(navController: NavController) {
    Compose_paymentTheme {
        Column(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()) {
                setTitleView(navController = navController)
                setPaymentView()
        }
    }
}

@Composable
fun setTitleView(navController: NavController){
    Row(verticalAlignment = Alignment.CenterVertically) {
        setSubTitleTextView(msg = "결제 정보 추가하기")
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            navController.popBackStack()
        }){
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Add", tint = Color.White, modifier = Modifier.width(40.dp))
        }
    }
}

// 결제 목록
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun setPaymentView(){
    val otts = getItem()

    val scrollState = rememberScrollState()
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = DarkGrey, RoundedCornerShape(8.dp))){
        LazyVerticalGrid(columns = GridCells.Fixed(3),  modifier = Modifier.fillMaxSize()){
            items(otts) {ott ->
                Box(Modifier.padding(5.dp)) {
                    Box(modifier = Modifier
                        .aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(20.dp))) {
                        GlideImage(
                            model = ott.ottUri,
                            contentScale = ContentScale.FillHeight,
                            contentDescription = ott.ottName
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun getItem() : MutableList<OttWithId>{
    val context = LocalContext.current
    val ottItem = OttData.ottData
    val otts = mutableListOf<OttWithId>()
    ottItem.forEach{ott ->
        val imgId = context.resources.getIdentifier("appicon_"+ ott.name, "drawable","com.shino72.compose_payment")
        val uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + imgId);

        otts.add(OttWithId(ott.name, uri))
    }
    return otts
}
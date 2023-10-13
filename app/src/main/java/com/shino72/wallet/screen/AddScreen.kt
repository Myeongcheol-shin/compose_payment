package com.shino72.wallet.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.shino72.wallet.data.OttData
import com.shino72.wallet.data.OttWithId
import com.shino72.wallet.navigator.Screens
import com.shino72.wallet.ui.theme.Compose_paymentTheme
import com.shino72.wallet.ui.theme.DarkGrey

@Composable
fun AddScreen(navController: NavController) {
    // observe data
    Compose_paymentTheme {
        Column(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()) {
                setTitleView(navController = navController,"결제 정보 추가하기")
                setPaymentView(navController = navController)
        }
    }
}

@Composable
fun setTitleView(navController: NavController, title: String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        setSubTitleTextView(msg = title)
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
fun setPaymentView(navController: NavController){
    val otts = getItem()

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = DarkGrey, RoundedCornerShape(8.dp))){
        LazyVerticalGrid(columns = GridCells.Fixed(3),  modifier = Modifier.fillMaxSize()){
            items(otts) {ott ->
                Box(Modifier.padding(5.dp)) {
                    Box(modifier = Modifier
                        .aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable {
                            navController.navigate("${Screens.InfoScreen.name}/${ott.ottName}")
                        }) {
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
        val imgId = context.resources.getIdentifier("appicon_"+ ott.name, "drawable","com.shino72.wallet")
        val uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + imgId);

        otts.add(OttWithId(ott.name, uri))
    }
    return otts
}
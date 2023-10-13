package com.shino72.wallet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.shino72.wallet.navigator.PaymentNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyApp : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // db 데이터 가져오기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermission(applicationContext)
            }
            // 오버레이 권한
            if (!Settings.canDrawOverlays(this)){
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${packageName}"))
                startActivity(intent)
            }
            val controller = rememberNavController()
            PaymentNavigation(navController = controller)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun requestPermission(context : Context) {
    TedPermission.create()
        .setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {
            }            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(context, "알람 요청을 위해 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }        }).setDeniedMessage("알람 권한을 허용해주세요.").setPermissions(android.Manifest.permission.POST_NOTIFICATIONS,android.Manifest.permission.SYSTEM_ALERT_WINDOW
        ).check()
}




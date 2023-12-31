package com.shino72.wallet.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import androidx.core.app.NotificationCompat
import com.shino72.wallet.MyApp
import com.shino72.wallet.MyApplication
import com.shino72.wallet.R
import com.shino72.wallet.data.Status
import com.shino72.wallet.repo.DBRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint

class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var dbRepository: DBRepository
    lateinit var notificationManager:NotificationManager

    override fun onReceive(context: Context, intent:  Intent) {
        // 재부팅 시 알람 재등록
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            // 알람 데이터 가져오기
            runBlocking {
                val prefs = MyApplication.prefs
                if(prefs.EditStatus){
                    setAlarm(context = context)
                }
            }
        }
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager

        // 알림 체널 설정
        createNotificationChannel()
        deliverNotification(context = context)

    }
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(CHANNEL_ID,"정기결제 알림",NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(false) // 불빛
        notificationChannel.enableVibration(false) // 진동
        notificationChannel.description = "알림"
        notificationManager.createNotificationChannel(notificationChannel)
    }
    private fun deliverNotification(context : Context) {
        val db = dbRepository.getAllOttData()
        runBlocking(Dispatchers.IO){
            db.collectLatest {
                when(it){
                    is Status.Success -> {
                        val nowDate = LocalDate.now().dayOfMonth
                        val filterPlan = it.data?.filter { it.duedate > nowDate }
                        val contentIntent = Intent(context, MyApp::class.java)
                        val contentPendingIntent = PendingIntent.getActivity(
                            context,
                            get_requestcode,
                            contentIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        val title = when(filterPlan?.map {f -> f.price }?.sum() ?: 0) {
                            0 -> {"내일 결제 예정인 OTT는 없습니다!"}
                            null -> {"내일 결제 예정인 OTT는 없습니다!"}
                            else -> {"내일 %d원 결제 예정".format(filterPlan?.map {f -> f.price }?.sum() ?: 0)}
                        }
                        val listText = when(filterPlan?.map {f-> f.korean }?.joinToString("")){
                            "" -> {""}
                            null -> {""}
                            else -> {"결제 목록 : %s".format(filterPlan?.map {f-> f.korean }?.toSet()?.joinToString(""))}
                        }

                        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(listText)
                            .setContentIntent(contentPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setChannelId(CHANNEL_ID)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                        notificationManager.notify(101, builder.build())
                        setAlarm(context = context)

                    }
                    is Status.Error -> {}
                    is Status.Loading -> {}
                    else -> {}
                }
            }
        }
    }
    fun setAlarm(context: Context) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,9)
            set(Calendar.MINUTE, 0)
        }
        if(calendar.before(java.util.Calendar.getInstance())) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH,1)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    companion object{
        const val get_requestcode = 0
        const val CHANNEL_ID = "SHINO72"
    }
}
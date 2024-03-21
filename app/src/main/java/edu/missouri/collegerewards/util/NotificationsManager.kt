package edu.missouri.collegerewards.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.objects.User
import java.util.UUID


class NotificationsManager: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        if(Firebase.auth.currentUser != null) {
            User.updateFCMToken(token) { /* Token Sent */ }
        }
    }

    companion object {
        const val routeOne = "routeOne"

        fun generateToken(completion: (String) -> Unit) {
            Firebase.messaging.token.addOnSuccessListener { token ->
                User.updateFCMToken(token) {
                    Log.d("TOKEN FCM:", token)
                    completion(token)
                }
            }.addOnFailureListener {
                Log.d("Token Error!", "Fetching Token Failed: $it")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Change to this once the notification object is removed from the google cloud function
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "College Rewards"
            val message = remoteMessage.data["body"]
            val route = remoteMessage.data["route"] ?: ""
            message?.let { sendNotification(title, it, getRoute(route), remoteMessage.data) }
        }
    }

    private fun sendNotification(messageTitle: String, messageBody: String, pushNotificationRoute: PushNotificationRoute, extras: Map<String, String>) {
        val destinations = mutableListOf<Int>()
        val bundle = bundleOf()

        when(pushNotificationRoute) {
            PushNotificationRoute.RouteOne -> {
                destinations.add(R.id.homeFragment)
            }
            PushNotificationRoute.Unknown -> {
                destinations.add(R.id.homeFragment)
            }
        }

        val pendingIntentBuilder = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.content_nav_graph)


        destinations.forEach {
            pendingIntentBuilder.addDestination(it)
        }
        pendingIntentBuilder.createTaskStackBuilder()
        pendingIntentBuilder.setArguments(bundle)
        val pendingIntent = pendingIntentBuilder.createPendingIntent()

        val channelId = "college_rewards_notifications"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setColor(getColor(R.color.yellow))
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = UUID.randomUUID().hashCode()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun getRoute(route: String): PushNotificationRoute {
        return when(route) {
            routeOne -> PushNotificationRoute.RouteOne
            else -> PushNotificationRoute.Unknown
        }
    }

}
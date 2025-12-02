package com.semicolon.dhaiban.presentation.main

import ChatScreen
import android.Manifest
import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.core.screen.Screen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.semicolon.dhaiban.presentation.app.MainApp
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import com.semicolon.dhaiban.presentation.utils.applyEdgeToEdge
import com.semicolon.dhaiban.presentation.utils.applySplashScreenWithAnimation
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel by inject<MainActivityViewModel>()

    companion object {
        const val TAG = "Current Firebase Token"
    }
    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent)
        Log.d("onNewIntent","onNewIntent")
        intent?.let {

            // Handle the new intent, extract data, and navigate accordingly
            val destination = it.getStringExtra("destination") ?: return
            val inboxId= intent.getIntExtra("inboxId",0)

            Log.d("destination",destination)
            Log.d("inboxId",inboxId.toString())


            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent): Screen {
        val inboxId= intent.getIntExtra("inboxId",0)
        Log.d("inboxIdintent",inboxId.toString())
        return when (intent.getStringExtra("destination")) {

            "ChatInbox" -> ChatScreen(inboxID =inboxId )
            else -> {
                MainContainer()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applySplashScreenWithAnimation()
        }
        installSplashScreen()
        applyEdgeToEdge()
        super.onCreate(savedInstanceState)
        val destination= intent.getStringExtra("inboxID")
        Log.d("setContent",destination.toString())

        if (intent.getStringExtra("destination") != null) {
            val inboxId= intent.getIntExtra("inboxID",0)
            val destination= intent.getStringExtra("destination")
Log.d("setContent",destination.toString())

            setContent {
                MainApp(typeofnotifcation = destination.toString(), inboxID =inboxId ).Content()


            }

        }
        else{
            Log.d("not null", "not null")


            setContent {
                MainApp().Content()


            }

        }

        requestNotificationPermission()
        Firebase.messaging.token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                Log.d(TAG, token)
                viewModel.updateFcmToken(token)

            }
        )
    }
//    private fun handleIntent(intent: Intent): Screen {
//        return when (intent.getStringExtra("destination")) {
//            "YourDestinationScreen" -> CHAT
//            else -> HomeScreen
//        }
//    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}
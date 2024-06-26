package com.islamelmrabet.cookconnect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.islamelmrabet.cookconnect.navigation.AppNavigation
import com.islamelmrabet.cookconnect.ui.CookConnectContent
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.InvoiceViewModel
import com.islamelmrabet.cookconnect.viewModel.MainViewModel
import com.islamelmrabet.cookconnect.viewModel.OrderCookerViewModel
import com.islamelmrabet.cookconnect.viewModel.OrderViewModel
import com.islamelmrabet.cookconnect.viewModel.PreferencesViewModel
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import com.islamelmrabet.cookconnect.viewModel.TableViewModel

/**
 * Class MainActivity
 *
 */
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTablet(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        askNotificationPermission()
        tokenNew()
        val authViewModel by viewModels<AuthViewModel>()
        val productViewModel by viewModels<ProductViewModel>()
        val tableViewModel by viewModels<TableViewModel>()
        val mainViewModel by viewModels<MainViewModel>()
        val orderViewModel by viewModels<OrderViewModel>()
        val orderCookerViewModel by viewModels<OrderCookerViewModel>()
        val invoiceViewModel by viewModels<InvoiceViewModel>()
        val preferencesViewModel by viewModels<PreferencesViewModel>()

        setContent {
            CookConnectContent {
               AppNavigation(this,authViewModel,productViewModel,tableViewModel,mainViewModel,orderViewModel, orderCookerViewModel,invoiceViewModel, preferencesViewModel)
            }
        }

    }


    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Permission", "Is Granted")
        } else {
            Log.d("Permission", "Is Not Granted")
        }
    }

    private fun tokenNew() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("FCM TOKEN", token.toString())
        })
    }

    private fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}



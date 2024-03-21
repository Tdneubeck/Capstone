package edu.missouri.collegerewards

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.databinding.ActivityMainBinding
import edu.missouri.collegerewards.objects.User
import edu.missouri.collegerewards.util.NavigationType
import edu.missouri.collegerewards.util.Navigator
import edu.missouri.collegerewards.util.NotificationsManager
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var permissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->

        val list = result.values
        val permissionList = mutableListOf<String>()
        var permissionCount = 0

        for ((index, _) in list.withIndex()) {
            if (shouldShowRequestPermissionRationale(this.permissions[index])) {
                permissionList.add(this.permissions[index])
            } else if (!hasPermission(this, this.permissions[index])) {
                permissionCount++
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askForPermissions(permissions)
        statusCheck()


        var firstLoad = true

        val authNavHostFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentNavHost) as NavHostFragment
        val authNavController = authNavHostFragment.navController
        val graph = authNavController.navInflater.inflate(R.navigation.main_nav_graph)
        if (Firebase.auth.currentUser != null) {
            User.loadUser { user ->
                intent.extras?.getString("route")?.let {
                    Log.e("Route", it)
                }
                graph.setStartDestination(R.id.mainContentFragment)
                authNavController.graph = graph
                NotificationsManager.generateToken { User.updateFCMToken(it) { /* Token Sent*/ } }
            }
        } else {
            graph.setStartDestination(R.id.loginFragment)
            authNavController.graph = graph
        }

        Firebase.auth.addAuthStateListener { auth ->
            Firebase.crashlytics.setCustomKey("userId", Firebase.auth.currentUser?.uid ?: "No userId found")
            if (!firstLoad) {
                if (auth.currentUser == null) {
                    Navigator.navigate(NavigationType.Auth, R.id.action_mainContentFragment_to_loginFragment)
                    //authNavController.clearBackStack(R.id.profile_tab_nav)
                }

            }
            firstLoad = false
        }

        // Used to collect the navigation changes
        lifecycleScope.launch {
            Navigator.navigateState.collect { state ->
                state.mainDestination?.let {
                    authNavController.navigate(it)
                    Navigator.navigate(NavigationType.Auth, null)
                }
            }
        }
    }

    private fun hasPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun askForPermissions(permissions: List<String>) {
        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Location is disabled. Location is required for Bluetooth connections and to confirm attendance. Do you want to enable it now? (May require app restart after enabling)")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }


    // Closes keyboard when tapped outside
    var pressTime: Long = 0
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            pressTime = System.currentTimeMillis()
        } else if (ev.action == MotionEvent.ACTION_UP) {
            val releaseTime = System.currentTimeMillis()
            if (releaseTime - pressTime < 200) {
                if (currentFocus != null) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}
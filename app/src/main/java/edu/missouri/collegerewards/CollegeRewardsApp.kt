package edu.missouri.collegerewards

import android.app.Application
import android.os.Build
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CollegeRewardsApp: Application() {


    override fun onCreate() {
        super.onCreate()
        Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).setCacheSizeBytes(100000000).build()
    }

}
package edu.missouri.collegerewards.objects

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.util.NotificationsManager


class User() {
    var email: String = ""
    var uid: String = ""
    var name: String = ""
    var fcmToken: String = ""
    var points: Int = 0
    var password: String = ""
    var role: Boolean = false

    constructor(data: Map<String, Any>) : this() {
        this.name = data["name"] as? String ?: ""
        this.uid = data["uid"] as? String ?: ""
        this.email = data["email"] as? String ?: ""
        this.points = (data["points"] as? Long)?.toInt() ?: 0
        this.fcmToken = data["fcmToken"] as? String ?: ""
        this.role = data["role"] as? Boolean ?: false
    }

    constructor(
        email: String,
        password: String,
        uid: String,
        name: String,
        fcmToken: String = "",
        role: Boolean
    ) : this() {
        this.email = email
        this.password = password
        this.uid = uid
        this.name = name
        this.fcmToken = fcmToken
        this.role = role
    }

    companion object {

        fun login(email: String, password: String, completion: (Boolean) -> Unit) {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    completion(true)
                }
                .addOnFailureListener {
                    completion(false)
                }
        }

        fun loadUser(completion: (User) -> Unit) {
            val database = Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)

            database.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val data = snapshot.data
                    Log.d("User Data:",  "$data")
                    val user = User(data!!)
                    SingletonData.shared.currentUser = user
                    completion(user)
                } else {
                    val user = User()
                    user.saveInitialUser {
                        completion(user)
                    }
                }
            }.addOnFailureListener {
                // Offline
            }
        }


        fun loadUser(selectedUid: String, completion: (User?) -> Unit) {
            val database = Firebase.firestore.collection("Users").document(selectedUid)
            database.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val data = snapshot.data
                    val user = User(data!!)
                    completion(user)
                } else {
                    print("Document doesn't exist")
                    completion(null)
                }
            }.addOnFailureListener {
                completion(null)
            }
        }

        fun updateFCMToken(token: String, completion: (Boolean) -> Unit) {
            val database = Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)
            val databaseDictionary = mapOf("fcmToken" to token)
            database.set(databaseDictionary, SetOptions.merge()).addOnCompleteListener { task ->
                completion(task.isSuccessful)
            }
        }

        fun logOut() {
            Firebase.auth.signOut()
            SingletonData.shared.clear()
        }
    }


    fun register(completion: (Boolean) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(this.email, this.password)
            .addOnCompleteListener { task ->
                completion(task.isSuccessful)
            }
    }

    fun saveUser(completion: (Boolean) -> Unit) {
        val database = Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)
        val auth = Firebase.auth
        var token = ""
        NotificationsManager.generateToken {
            token = it
        }
        val databaseDictionary = mutableMapOf(
            "email" to auth.currentUser?.email,
            "uid" to auth.currentUser?.uid,
            "points" to this.points,
            "fcmToken" to token,
            "name" to this.name,
            "role" to this.role
        )

        database.set(databaseDictionary, SetOptions.merge()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true)
            } else {
                Log.d("ERROR", "Error on User Save! ${task.exception}")
                completion(false)
            }
        }
    }

    fun saveInitialUser(completion: (Boolean) -> Unit) {
        val database = Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)
        val auth = Firebase.auth
        var token = ""
        NotificationsManager.generateToken {
            token = it
        }
        val databaseDictionary = mutableMapOf(
            "email" to auth.currentUser?.email,
            "uid" to auth.currentUser?.uid,
            "points" to this.points,
            "fcmToken" to token,
            "name" to this.name,
            "role" to false
        )


        database.set(databaseDictionary, SetOptions.merge()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true)
            } else {
                print("Error on User Save! ${task.exception}")
                val user = User()
                user.email = auth.currentUser?.email!!
                user.uid = auth.currentUser?.uid!!
                SingletonData.shared.currentUser = user
                completion(false)
            }
        }
    }


}

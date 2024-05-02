package edu.missouri.collegerewards.ui.conditionalnav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.objects.User
import edu.missouri.collegerewards.util.NavigationType
import edu.missouri.collegerewards.util.Navigator

class LoginFragment : Fragment() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.enter_button)
        registerButton = view.findViewById(R.id.register_link)
        loginButton.setOnClickListener {
            loginUser(username.text.toString(), password.text.toString())
        }
        registerButton.setOnClickListener {
            Navigator.navigate(NavigationType.Auth, R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }


    private fun loginUser(email: String, password: String) {
        if (password.isBlank() || email.isBlank()) return
        User.login(email, password) { wasSuccessful ->
            if (wasSuccessful) {
                // Logged in
                User.loadUser(Firebase.auth.currentUser!!.uid) { user ->
                    if (user == null) {
                        val newUser = User(
                            email = Firebase.auth.currentUser!!.email!!,
                            password = "",
                            uid = Firebase.auth.currentUser!!.uid,
                            name = "",
                            fcmToken = "",
                            role = false
                        )
                        newUser.saveInitialUser { success ->
                            if (success) {
                                // Navigate to Home
                                SingletonData.shared.currentUser = newUser
                                Navigator.navigate(
                                    NavigationType.Auth,
                                    R.id.action_loginFragment_to_mainContentFragment
                                )
                            }
                        }
                    } else {
                        SingletonData.shared.currentUser = user
                        // Navigate based on user's role
                        Navigator.navigate(
                            NavigationType.Auth,
                            R.id.action_loginFragment_to_mainContentFragment
                        )
                    }
                }
            }
        }
    }
}
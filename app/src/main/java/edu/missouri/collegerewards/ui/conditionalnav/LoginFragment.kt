package edu.missouri.collegerewards.ui.conditionalnav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import edu.missouri.collegerewards.MainActivity
import edu.missouri.collegerewards.R

class LoginFragment : Fragment() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        mAuth = FirebaseAuth.getInstance()

        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.enter_button)
        loginButton.setOnClickListener {
            loginUser(username.text.toString(), password.text.toString())
        }

        return view
    }

    private fun loginUser(username: String, password: String) {
        mAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    // Navigate user to another screen
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }
}
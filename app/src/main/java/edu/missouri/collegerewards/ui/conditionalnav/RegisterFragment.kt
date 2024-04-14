package edu.missouri.collegerewards.ui.conditionalnav


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.util.NavigationType
import edu.missouri.collegerewards.util.Navigator

class RegisterFragment : Fragment() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registerpage, container, false)

        firstNameEditText = view.findViewById(R.id.firstname)
        lastNameEditText = view.findViewById(R.id.lastname)
        emailEditText = view.findViewById(R.id.email)
        passwordEditText = view.findViewById(R.id.password)
        confirmPasswordEditText = view.findViewById(R.id.confirm_password)
        registerButton = view.findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            registerUser(
                firstNameEditText.text.toString(),
                lastNameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
                confirmPasswordEditText.text.toString()
            )
        }

        return view
    }

    private fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    // Save additional user details to Firebase Realtime Database
                    val userRef = Firebase.firestore.collection("Users").document(Firebase.auth.currentUser?.uid ?: "")
                    val userData = mapOf(
                        "name" to "$firstName $lastName",
                        "email" to email,
                        "uid" to Firebase.auth.currentUser!!.uid,
                        "points" to 0
                    )
                    userRef.set(userData)
                    Navigator.navigate(NavigationType.Auth, R.id.action_registerFragment_to_mainContentFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        requireContext(), "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
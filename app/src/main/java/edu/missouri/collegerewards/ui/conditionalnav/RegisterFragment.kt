package edu.missouri.collegerewards.ui.conditionalnav


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import edu.missouri.collegerewards.R

class RegisterFragment : Fragment() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registerpage, container, false)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        firstNameEditText = view.findViewById(R.id.firstname)
        lastNameEditText = view.findViewById(R.id.lastname)
        emailEditText = view.findViewById(R.id.email)
        usernameEditText = view.findViewById(R.id.username)
        passwordEditText = view.findViewById(R.id.password)
        confirmPasswordEditText = view.findViewById(R.id.confirm_password)
        registerButton = view.findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            registerUser(
                firstNameEditText.text.toString(),
                lastNameEditText.text.toString(),
                emailEditText.text.toString(),
                usernameEditText.text.toString(),
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
        username: String,
        password: String,
        confirmPassword: String
    ) {
        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()
                    user?.updateProfile(userProfileChangeRequest)

                    // Save additional user details to Firebase Realtime Database
                    val userRef = database.reference.child("users").child(user?.uid ?: "")
                    val userData = HashMap<String, String>()
                    userData["firstName"] = firstName
                    userData["lastName"] = lastName
                    userData["email"] = email
                    userData["username"] = username
                    userRef.setValue(userData)

                    // Navigate user to another screen
                    startActivity(Intent(requireActivity(), LoginFragment::class.java))
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
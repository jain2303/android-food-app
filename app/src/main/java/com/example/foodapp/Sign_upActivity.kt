package com.example.foodapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Sign_upActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var signupbt: Button
    private lateinit var nameEt: EditText
    private lateinit var phonenumberEt: EditText

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEt = findViewById(R.id.email2)
        passwordEt = findViewById(R.id.password2)
        signupbt = findViewById(R.id.button2)
        nameEt = findViewById(R.id.name)
        phonenumberEt = findViewById(R.id.phone)



        auth = FirebaseAuth.getInstance()

        signupbt.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            val name = nameEt.text.toString()
            val phonenumber = phonenumberEt.text.toString()
            val intent = Intent(this@Sign_upActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

            signUp(email, password,name,phonenumber)

        }
    }

    private fun signUp(email: String, password: String, name : String,phonenumber : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User creation successful
                    val user = auth.currentUser
                    user?.let {
                        val userId = it.uid
                        val userData = hashMapOf(
                            "name" to  name,
                            "phonenumber" to phonenumber,
                            // Add any additional user data as needed
                        )

                        firestore.collection("users")
                            .document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                val intent = Intent(this@Sign_upActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                                // Optional: Finish the current Signup activity to prevent going back to it with the back button
                            }
                            .addOnFailureListener { e ->
                                // Error saving user data
                                Toast.makeText(
                                    this,
                                    "Error saving user data: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    // User creation failed
                    Toast.makeText(
                        this,
                        "Signup failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
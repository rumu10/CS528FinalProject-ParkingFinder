package com.example.firebasep

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var textEmail: EditText
    private lateinit var textPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textEmail = findViewById(R.id.textEmail)
        textPassword = findViewById(R.id.textPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            if (validateUsername() && validatePassword()) {
                checkUser(object : LoginSuccessCallback {
                    override fun onLoginSuccess() {
                        // This is called if login is successful
                        Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity , ParkingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }

                    override fun onLoginFailure(message: String) {
                        // This is called if login fails
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    }

    private fun validateUsername(): Boolean {
        val valUsername = textEmail.text.toString()
        if (valUsername.isEmpty()) {
            textEmail.error = "Email cannot be empty!"
            return false
        } else if (!valUsername.endsWith("@wpi.edu")) {
            textEmail.error = "Login with your WPI credential!"
            return false
        } else {
            textEmail.error = null
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val valPassword = textPassword.text.toString()
        if (valPassword.isEmpty()) {
            textPassword.error = "Password cannot be empty!"
            return false
        } else if (valPassword.length < 6) {
            textPassword.error = "Password must be at least 6 characters!"
            return false
        } else {
            textPassword.error = null
            return true
        }
    }
    interface LoginSuccessCallback {
        fun onLoginSuccess()
        fun onLoginFailure(message: String)
    }
    private fun checkUser(callback: LoginSuccessCallback) {
        val userUsername = textEmail.text.toString().trim()
        val userPassword = textPassword.text.toString().trim()

        val reference = FirebaseDatabase.getInstance().getReference("Drivers")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isUsernameFound = false
                var storedPassword: String? = null

                for (driverSnapshot in dataSnapshot.children) {
                    val uEmail = driverSnapshot.child("uemail").getValue(String::class.java)
                    if (uEmail != null && uEmail.toLowerCase() == userUsername.toLowerCase()) {
                        storedPassword = driverSnapshot.child("upass").getValue(String::class.java)
                        isUsernameFound = true
                        break
                    }
                }
                
                if (isUsernameFound) {
                    Log.d("myLoginStatus", "Username found: $userUsername")
                    if (storedPassword != null && storedPassword == userPassword) {
                        Log.d("LoginStatus", "Credentials are correct. Welcome to our GUI!")
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Credentials are correct. Welcome to our GUI!", Toast.LENGTH_SHORT).show()
                        }
                        callback.onLoginSuccess()
                    } else {
                        Log.d("LoginStatus", "Password does not match.")
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Password does not match.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.d("LoginStatus", "Username(Email) not found")
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Username(Email) does not match.", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.w("DatabaseError", "Failed to read value.", databaseError.toException())
            }
        })
    }
}

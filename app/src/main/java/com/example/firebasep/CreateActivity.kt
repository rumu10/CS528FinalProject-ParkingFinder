package com.example.firebasep

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.example.firebasep.DataModels.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateActivity : AppCompatActivity() {

    private lateinit var usName: EditText
    private lateinit var usPass: EditText
    private lateinit var usEmail: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        usName = findViewById(R.id.usName)
        usPass = findViewById(R.id.usPass)
        usEmail = findViewById(R.id.usEmail)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Drivers")


        btnSaveData.setOnClickListener {
            saveDriversData()
        }
    }

    private fun saveDriversData() {

        //getting values
        val uName = usName.text.toString()
        val uPass = usPass.text.toString()
        val uEmail = usEmail.text.toString()

        if (uName.isEmpty()) {
            usName.error = "Please enter your name..."
        }
        if (uPass.isEmpty()) {
            usPass.error = "Please enter your password..."
        }
        if (uPass.length < 6) {
            usPass.error = "Password must have at least 6 characters!"

        }
        if (uEmail.isEmpty()) {
            usEmail.error = "Please enter the E-mail..."
        }
        if (uEmail.endsWith("@wpi.edu")) {
            usEmail.error = "Login with your WPI credential!"
        }
        Toast.makeText(this, "We got your data and will save it soon!", Toast.LENGTH_SHORT).show()
        val uId = dbRef.push().key!!

        val driver = UserModel(uId , uName, uEmail, uPass)

        dbRef.child(uId).setValue(driver)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
                    usName.text.clear()
                    usPass.text.clear()
                    usEmail.text.clear()
                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

    }
}



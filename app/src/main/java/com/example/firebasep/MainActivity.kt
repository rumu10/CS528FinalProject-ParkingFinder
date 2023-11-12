package com.example.firebasep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.example.firebasep.DataModels.ParkingSpotsModel
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize dbRef with the path to "parkingSpots"
        dbRef = FirebaseDatabase.getInstance().getReference("parkingSpots")
        insertParkingSpotToFirebase()

        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)

        btnInsertData.setOnClickListener {
            insertParkingSpotToFirebase() // Call the function to insert data
            val intent = Intent (this, CreateActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent (this , LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun insertParkingSpotToFirebase() {
        val dummyDataList = listOf(
            ParkingSpotsModel("A1", "available", -74.0060, 40.7128),
            ParkingSpotsModel("A2", "occupied", -74.0070, 40.7138),
            ParkingSpotsModel("A3", "available", -74.0080, 40.7148),
            ParkingSpotsModel("B1", "available", -74.0060, 40.7128),
            ParkingSpotsModel("B2", "occupied", -74.0070, 40.7138),
            ParkingSpotsModel("B3", "available", -74.0080, 40.7148),
            ParkingSpotsModel("C1", "available", -74.0060, 40.7128),
            ParkingSpotsModel("C2", "occupied", -74.0070, 40.7138),
            ParkingSpotsModel("C3", "available", -74.0080, 40.7148),
            ParkingSpotsModel("D1", "available", -74.0060, 40.7128),
            ParkingSpotsModel("D2", "occupied", -74.0070, 40.7138),
            ParkingSpotsModel("D3", "available", -74.0080, 40.7148)
            // later, should add 40 parking spots with accuate plong and plat..
        )

        dummyDataList.forEach { spot ->
            dbRef.child(spot.pId).setValue(spot)
                .addOnSuccessListener {
                    Log.d("FirebaseSuccessss", "Data added successfully for: ${spot.pId}")
                }
                .addOnFailureListener {
                    Log.e("FirebaseErrorrr", "Error adding data for: ${spot.pId}", it)
                }

        }
    }
}


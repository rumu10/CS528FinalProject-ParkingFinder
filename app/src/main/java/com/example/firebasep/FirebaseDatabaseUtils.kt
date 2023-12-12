package com.example.firebasep
import com.example.firebasep.DataModels.ParkingSpotsModel
import com.google.firebase.database.FirebaseDatabase

fun updateFirebaseDatabase(parkingSpots: List<ParkingSpotsModel>) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("parkingSpots")

    parkingSpots.forEach { spot ->
        ref.child(spot.pId).setValue(spot)
    }
}
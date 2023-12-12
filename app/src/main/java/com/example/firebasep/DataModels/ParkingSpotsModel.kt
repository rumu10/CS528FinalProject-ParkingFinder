package com.example.firebasep.DataModels

data class ParkingSpotsModel (
    var pId: String,
    var Paval: String,
    var plong: Double,
    var plat: Double
)


fun validateData(parkingSpots: List<ParkingSpotsModel>): Boolean {
    // Basic validation: check if the list is not empty
    if (parkingSpots.isEmpty()) return false

    // Further validation: check each parking spot for required fields
    return parkingSpots.all { spot ->
        spot.pId.isNotEmpty() &&
                spot.Paval.isNotEmpty()

    }
}
package com.example.firebasep
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasep.DataModels.ParkingSpotsModel
import android.graphics.Color
import android.view.View
import android.widget.GridLayout

class DisplayParkingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ParkingAdapter: ParkingAdapter
    private var parkingList: MutableList<ParkingSpotsModel> = mutableListOf()
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_parking)
        Log.e("ccc INsidee Fetch data", "Inside FtechData")

        recyclerView = findViewById(R.id.recyclerViewParkings)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ParkingAdapter = ParkingAdapter(parkingList)
        recyclerView.adapter = ParkingAdapter

        // Fetch data from Firebase
        Log.e("bbb INsidee Fetch data", "Inside FtechData")
        fetchDataFromFirebase()

    }
    private fun fetchDataFromFirebase() {
        Log.e("INsidee Fetch data", "Inside FtechData")
        val databaseReference = FirebaseDatabase.getInstance().getReference("parkingSpots")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("DataSnapSHOTTT", "Reading Data from ")
                if (!dataSnapshot.exists()) {
                    Log.e("FirebaseConnection", "No data found in the database.")
                    return
                }

                Log.d("FirebaseConnection", "Successfully connected to Firebase database.")

                parkingList.clear()
                for (ParkingSnapshot in dataSnapshot.children) {
                    val PId = ParkingSnapshot.child("pid").getValue(String::class.java) ?: ""
                    val Paval = ParkingSnapshot.child("paval").getValue(String::class.java) ?: ""
                    val plong = ParkingSnapshot.child("plong").getValue(Double::class.java) ?: 0.0
                    val plat = ParkingSnapshot.child("plat").getValue(Double::class.java) ?: 0.0
                   // Log.d("FirebaseData", "Snapshot Key: ${parkingSnapshot.key}, Value: ${parkingSnapshot.value}")

                    val parking = ParkingSpotsModel(PId, Paval, plong, plat)
                    parkingList.add(parking)
                    Log.d("ParkingModelData", "Created ParkingModel - PID: $PId, availabity: $Paval, longtitude: Plong, lattitude: Plat")


                    Log.e("Now read the data in fun ...", "Reading22 Data from ")
                }

                val linearLayout: GridLayout = findViewById(R.id.gridLayoutForButtons)

                var currentColumn = 0
                var currentRow = 0

                parkingList.forEach { parking ->
                    val button = Button(this@DisplayParkingActivity).apply {
                        text = parking.pId
                        setBackgroundColor(if (parking.Paval.equals("available", ignoreCase = true)) Color.GREEN else Color.RED)
                        layoutParams = GridLayout.LayoutParams().apply {
                            columnSpec = GridLayout.spec(currentColumn)
                            rowSpec = GridLayout.spec(currentRow)
                            width = GridLayout.LayoutParams.WRAP_CONTENT
                            height = GridLayout.LayoutParams.WRAP_CONTENT
                            setMargins(8, 8, 8, 8)
                        }
                        setOnClickListener {
                            if (parking.Paval == "available") {
                                println("available")
                                // Perform actions for available parking
//                                text = "Coords: (${parking.pId} : ${parking.plong}, ${parking.plat})"
//                                linearLayout.visibility = View.GONE
////
//                                val transaction = supportFragmentManager.beginTransaction()
//                                transaction.replace(R.id.map, MapFragment())
//                                transaction.addToBackStack(null)

                                val intent = Intent(this@DisplayParkingActivity, NavigationView::class.java)
                                intent.putExtra("parkingId", parking.pId)
                                startActivity(intent)
                            } else {

                                Toast.makeText(this@DisplayParkingActivity, "This location is already occupied", Toast.LENGTH_SHORT).show()
                            }
                        }

                        }



                    linearLayout.addView(button)
                    currentColumn++
                    if (currentColumn == 4) {
                        currentColumn = 0
                        currentRow++
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Error fetching data: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("DisplayParkingActivity", "Database error", databaseError.toException())
            }
        })
    }
}


package com.example.firebasep


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasep.DataModels.UserModel


class DisplayUsersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList: MutableList<UserModel> = mutableListOf()
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_users)
        Log.e("ccc INsidee Fetch data", "Inside FtechData")

        recyclerView = findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        // Fetch data from Firebase
        Log.e("bbb INsidee Fetch data", "Inside FtechData")
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        Log.e("INsidee Fetch data", "Inside FtechData")
        val databaseReference = FirebaseDatabase.getInstance().getReference("Drivers")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("DataSnapSHOTTT", "Reading Data from ")
                if (!dataSnapshot.exists()) {
                    Log.e("FirebaseConnection", "No data found in the database.")
                    return
                }

                Log.d("FirebaseConnection", "Successfully connected to Firebase database.")

                userList.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val uId = userSnapshot.child("uid").getValue(String::class.java) ?: ""
                    val uName = userSnapshot.child("uname").getValue(String::class.java) ?: ""
                    val uEmail = userSnapshot.child("uemail").getValue(String::class.java) ?: ""
                    val uPass = userSnapshot.child("upass").getValue(String::class.java) ?: ""
                    Log.d("FirebaseData", "Snapshot Key: ${userSnapshot.key}, Value: ${userSnapshot.value}")

                    val user = UserModel(uId, uName, uEmail, uPass)
                    userList.add(user)
                    Log.d("UserModelData", "Created UserModel - ID: $uId, Name: $uName, Email: $uEmail, Password: $uPass")


                    Log.e("Now read the data in fun ...", "Reading22 Data from ")
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Error fetching data: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("DisplayUsersActivity", "Database error", databaseError.toException())
            }
        })
    }
}


package com.example.firebasep

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firebasep.Geofence.GeofenceHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private lateinit var textEmail: EditText
    private lateinit var textPassword: EditText
    private lateinit var btnFetchData: Button
    private lateinit var btnInserData: Button
    private lateinit var btnManagementUser: Button


    //geofence
    private val TAG = "MainActivity"
    private lateinit var mMap: GoogleMap
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var locationManager: LocationManager
    private val locationCode = 2000
    private val GEOFENCE_RADIUS = 500
    private lateinit var geofenceHelper: GeofenceHelper
    private val GEOFENCE_ID = "WPI"
    //NE
    // Define your destination locations globally if they are fixed and reused
    private val around_wpi = LatLng(42.27384685833052, -71.80971805810996)
    //private val around_wpi = LatLng(42.282129, -71.754384)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textEmail = findViewById(R.id.textEmail)
        textPassword = findViewById(R.id.textPassword)
        btnFetchData = findViewById(R.id.btnFetchData)
        btnInserData = findViewById(R.id.btnInsertData)
        btnManagementUser = findViewById(R.id.btnManagementUsers)

        btnFetchData.setOnClickListener {
            if (validateUsername() && validatePassword()) {
                checkUser(object : LoginSuccessCallback {
                    override fun onLoginSuccess() {
                        // This is called if login is successful
                        Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@MainActivity , ParkingActivity::class.java)
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

        btnInserData.setOnClickListener {

            val intent = Intent(this@MainActivity, CreateActivity::class.java)
            startActivity(intent)
        }

        btnManagementUser.setOnClickListener {
            val intent = Intent(this@MainActivity, DisplayUsersActivity::class.java)
            startActivity(intent)
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
                var userType: String? = null


                for (driverSnapshot in dataSnapshot.children) {
                    val uEmail = driverSnapshot.child("uemail").getValue(String::class.java)
                    if (uEmail != null && uEmail.toLowerCase() == userUsername.toLowerCase()) {
                        storedPassword = driverSnapshot.child("upass").getValue(String::class.java)
                        userType = driverSnapshot.child("uname").getValue(String::class.java)
                        Log.d("UserTypeCheck", "User type: $userType")
                        isUsernameFound = true
                        break
                    }
                }

                if (isUsernameFound) {
                    if (storedPassword != null && storedPassword == userPassword) {
                        if (userType.equals("Admin", ignoreCase = true)) {
                            val adminIntent = Intent(this@MainActivity, DisplayUsersActivity::class.java)
                            startActivity(adminIntent)
                        } else {
                            val userIntent = Intent(this@MainActivity, ParkingActivity::class.java)
                            startActivity(userIntent)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Password does not match.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Username(Email) not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.w("DatabaseError", "Failed to read value.", databaseError.toException())
            }
        })
    }


    //Geofence
    private fun addGeofence(latLng: LatLng, radius: Int, ID: String) {
        val geofence = geofenceHelper.getGeofence(ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        val geofencingRequest = geofenceHelper.getGeofencingRequest(geofence)
        val pendingIntent = geofenceHelper.CreatePendingIntent()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener {
                Log.d(TAG, "onSuccess: Geofence Added...")
            }
            .addOnFailureListener { e ->
                val errorMessage = geofenceHelper.getErrorString(e)
                Log.d(TAG, "onFailure: $errorMessage")
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if ((ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED) &&
                    (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED)
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap!!
        mMap.clear()
        val zoom0nmap = LatLng(42.273844150238006, -71.80969436603154)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoom0nmap, 15F))
        val geofence_address = around_wpi
        addCircle(geofence_address, GEOFENCE_RADIUS)
        getmylocation()
        addGeofence(around_wpi, GEOFENCE_RADIUS, GEOFENCE_ID)

        //NE
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMapLongClickListener(this)
        //NE
    }

    private fun handleMapLongClick(latLng: LatLng) {
        mMap.clear()
        addMarker(latLng)
        addCircle(latLng, GEOFENCE_RADIUS)
        addGeofence(latLng, GEOFENCE_RADIUS, GEOFENCE_ID)

    }

    private fun addMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng)
        mMap.addMarker(markerOptions)
    }

    private fun addCircle(latLng: LatLng, radius: Int) {
        val circleOptions = CircleOptions()
        circleOptions.center(latLng)
        circleOptions.radius(radius.toDouble()) // Convert the radius to Double
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0))
        circleOptions.fillColor(Color.argb(64, 255, 0, 0))
        circleOptions.strokeWidth(4f)
        mMap.addCircle(circleOptions)
    }

    private fun getmylocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission( this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            mMap.isMyLocationEnabled = true
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationCode)
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationCode)
            }
        }
    }

    override fun onMapLongClick(p0: LatLng) {
        if (Build.VERSION.SDK_INT>=29)
        {
            if (ContextCompat.checkSelfPermission( this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                handleMapLongClick(p0)
            }
            else
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationCode)
                    Toast.makeText(this@MainActivity, "For triggering Geofencing we need your background location permission", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationCode)
                }
            }
        }
        else
        {
            handleMapLongClick(p0)
        }

    }
}

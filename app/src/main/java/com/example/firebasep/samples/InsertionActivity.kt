package com.example.firebasep.samples

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.example.firebasep.DataModels.EmployeeModel
import com.example.firebasep.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etEmpName: EditText
    private lateinit var etEmpAge: EditText
    private lateinit var etEmpSalary: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etEmpName = findViewById(R.id.etEmpName)
        etEmpAge = findViewById(R.id.etEmpAge)
        etEmpSalary = findViewById(R.id.etEmpSalary)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")


        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val empName = etEmpName.text.toString()
        val empAge = etEmpAge.text.toString()
        val empSalary = etEmpSalary.text.toString()

        if (empName.isEmpty()) {
            etEmpName.error = "please enter the name"
        }
        if (empAge.isEmpty()) {
            etEmpAge.error = "please enter the age"
        }
        if (empSalary.isEmpty()) {
            etEmpSalary.error = "please enter the Salary"
        }
        Toast.makeText(this, "we got the data, we will save it soon", Toast.LENGTH_LONG).show()
        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId , empName, empAge, empSalary)

        dbRef.child(empId).setValue(employee)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                    etEmpName.text.clear()
                    etEmpAge.text.clear()
                    etEmpSalary.text.clear()
                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

    }
}



package com.example.firebasep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the Spinner in the layout
       val spinner: Spinner = view.findViewById(R.id.spinner)
//            val options = resources.getStringArray(R.array.dropdown_items)

        // Create an ArrayAdapter using the string array from resources
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dropdown_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Set a listener for item selections
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item
                val selectedItem = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing here
            }
        }

        return view
    }
}


//class HistoryFragment : Fragment() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_home)
//
//        // Define an array of options
//        val options = arrayOf("Option 1", "Option 2", "Option 3", "Option 4")
//
//        // Create an ArrayAdapter
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        // Set the ArrayAdapter to the Spinner
//        Spinner.adapter = adapter
//
//        // Handle Spinner item selection
//        Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
//                // Handle the selected item here
//                val selectedItem = options[position]
//                // Do something with the selected item
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                // Do nothing here
//            }
//        }
//    }
//
//}
package com.thecodefire.brannigans.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.databinding.ActivityManageSalesBinding
import java.util.*

class ManageSales : AppCompatActivity() {

    private lateinit var binding: ActivityManageSalesBinding
    var filter = ""
    var dateArray = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageSalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // spinner for sale type
        val spinner: Spinner = binding.course
        ArrayAdapter.createFromResource(
            this,
            R.array.sales,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filter = spinner.getItemAtPosition(p2).toString()

                if(filter == "Select Specific Date"){
                    binding.dateId.visibility = View.VISIBLE
                    binding.dateSpinner.visibility = View.VISIBLE
//                    binding.tvSales.visibility = View.GONE
                    binding.btnSubmit.isEnabled = false

                    // clear previous array and find new one from storage
                    FirebaseDatabase.getInstance().getReference("totalSales").get().addOnSuccessListener {
                        dateArray.clear()
                        for(snap in it.children){
                            dateArray.add(snap.key.toString())
                        }

                        val dateSpinner: Spinner = binding.dateSpinner

                        val aa = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, dateArray)
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        dateSpinner.setAdapter(aa)

                        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                var date = dateSpinner.getItemAtPosition(p2).toString()

                                binding.btnSubmit.isEnabled = true

                                binding.btnSubmit.setOnClickListener {
                                    FirebaseDatabase.getInstance().getReference("totalSales").child(date).get().addOnSuccessListener {
                                        var total = 0F
                                        for(snap in it.children){
                                            total += snap.child("Total").value.toString().toFloat()
                                        }

                                        binding.tvSales.setText("TOTAL SALES ON "+date+" : "+ total)
                                    }
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }
                    }
                }else{
                    binding.dateId.visibility = View.GONE
                    binding.dateSpinner.visibility = View.GONE
//                    binding.tvSales.visibility = View.GONE
                    binding.btnSubmit.isEnabled = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.btnSubmit.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR).toString()
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val time = year+"-"+month+"-"+day

            // sale for today
            if(filter == "Total Sales Today"){
                FirebaseDatabase.getInstance().getReference("totalSales").child(time).get().addOnSuccessListener {
                    var total = 0F
                    for(snap in it.children){
                        total += snap.child("Total").value.toString().toFloat()
                    }

                    binding.tvSales.setText("TOTAL SALES TODAY : "+ total)
                }
                // food sale
            }else if(filter == "Total Food Sales Today"){
                FirebaseDatabase.getInstance().getReference("totalSales").child(time).get().addOnSuccessListener {
                    var total = 0F
                    for(snap in it.children){
                        total += snap.child("Foods").value.toString().toFloat()
                    }

                    binding.tvSales.setText("TOTAL FOOD SALES TODAY : "+ total)
                }
                // drink sale
            }else if(filter == "Total Drinks Sales Today"){
                FirebaseDatabase.getInstance().getReference("totalSales").child(time).get().addOnSuccessListener {
                    var total = 0F
                    for(snap in it.children){
                        total += snap.child("Drinks").value.toString().toFloat()
                    }

                    binding.tvSales.setText("TOTAL DRINKS SALES TODAY : "+ total)
                }
            }
        }
    }
}
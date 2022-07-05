package com.thecodefire.brannigans.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.databinding.ActivityBookAtableBinding
import kotlin.math.log

class BookATable : AppCompatActivity() {

    private lateinit var binding: ActivityBookAtableBinding

    var tableArray = ArrayList<String>()
    var table = ""
    var user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookAtableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // if table is marked as active after booking outputt message booking accepted
        FirebaseDatabase.getInstance().getReference("Tables").get().addOnSuccessListener {
            for(snap in it.children){
                if(snap.child("customerId").value.toString() == user){
                    if(snap.child("tableStatus").value.toString() == "Active"){
                        binding.tvWarn.setText("Your Booking for Table no "+snap.child("tableNo").value.toString()+" is Accepted!")
                        binding.btnMenu.visibility = View.VISIBLE

                        // not in use
                        binding.btnMenu.setOnClickListener {
                            val intent = Intent(this, FoodMenu::class.java)
                            intent.putExtra("TABLENO", snap.child("tableNo").value.toString())
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        // note
        FirebaseDatabase.getInstance().getReference("TablesBookings").get().addOnSuccessListener {
            for(snap in it.children){
                Log.d("CHK5", snap.value.toString())
                if(snap.child("customerId").value.toString() == user){
                    binding.tvWarn.setText("You Already Made a Booking Request for Table no " + snap.child("tableNo").value.toString() +" But You can re-request to increase the capacity of table")
                }
            }
        }

        FirebaseDatabase.getInstance().getReference("Tables").get().addOnSuccessListener {
            tableArray.clear()
            for(snap in it.children){
                if(snap.child("tableStatus").value.toString() == "inActive"){
                    tableArray.add(snap.key.toString())
                    Log.d("CHK", snap.key.toString())
                }
            }
            // select table
            val dateSpinner: Spinner = binding.tableSpinner

            val aa = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, tableArray)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dateSpinner.setAdapter(aa)

            dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    table = dateSpinner.getItemAtPosition(p2).toString()

                    for(snap in it.children){
                        if(snap.child("tableNo").value.toString() == table){
                            binding.etTableCapacity.setText(snap.child("tableCapacity").value.toString())
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

        // check foe all fields before requesting
        binding.btnBookNow.setOnClickListener {
            if(binding.etTableCapacity.text.isNotEmpty()){
                FirebaseDatabase.getInstance().getReference("TablesBookings").child(table).child("customerId").setValue(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@"))
                FirebaseDatabase.getInstance().getReference("TablesBookings").child(table).child("capacity").setValue(binding.etTableCapacity.text.toString())
                FirebaseDatabase.getInstance().getReference("TablesBookings").child(table).child("status").setValue("inActive")
                FirebaseDatabase.getInstance().getReference("TablesBookings").child(table).child("tableNo").setValue(table)
//                FirebaseDatabase.getInstance().getReference("Tables").child(table).child("customerId").setValue(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@"))
//                FirebaseDatabase.getInstance().getReference("Tables").child(table).child("tableStatus").setValue("Active").addOnSuccessListener {
                    binding.tvWarn.setText("Table Booking Request Send")
                }
            }
        }
    }
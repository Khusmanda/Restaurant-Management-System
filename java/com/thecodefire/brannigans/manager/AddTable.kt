package com.thecodefire.brannigans.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.databinding.ActivityAddTableBinding

class AddTable : AppCompatActivity() {
    var tableNo = 0L
    private lateinit var binding: ActivityAddTableBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // add table
        FirebaseDatabase.getInstance().getReference("Tables").get().addOnSuccessListener {
            binding.etTableNumber.setText((it.childrenCount + 1).toString())
            tableNo = it.childrenCount+1
        }
    // set as inactive
        binding.etTableStatus.setText("inActive")
        binding.etCustomerID.setText("0")

        // add table button listener
        binding.btnAddTable.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Tables").get().addOnSuccessListener {
                binding.etTableNumber.setText((it.childrenCount + 1).toString())
                tableNo = it.childrenCount+1
            }

            FirebaseDatabase.getInstance().getReference("Tables").child(tableNo.toString()).setValue(TableInfo(binding.etTableNumber.text.toString(), binding.etTableStatus.text.toString(), binding.etCustomerID.text.toString(), binding.etTableCapacity.text.toString())).addOnSuccessListener {
                binding.feedback.text = "Table Added Successfully"
            }

            if(binding.etTableCapacity.text?.isEmpty()==true){
                FirebaseDatabase.getInstance().getReference("Tables").child(tableNo.toString()).child("tableCapacity").setValue("4")
            }
        }
    }
}
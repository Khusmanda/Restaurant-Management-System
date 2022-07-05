package com.thecodefire.brannigans.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.MainActivity
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.databinding.ActivityCustomerDashboardBinding
import kotlin.math.roundToInt

class CustomerDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseDatabase.getInstance().getReference("Tables").get().addOnSuccessListener {
            var occupancy = 0F
            for(snap in it.children){
                if(snap.child("tableStatus").value.toString() == "Active"){
                    occupancy++
                }
            }

            binding.tvOccupancy.text = "Current Occupancy : " + ((occupancy/it.childrenCount)*100).roundToInt().toString() + "%"
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnBookAtable.setOnClickListener {
            val intent = Intent(this, BookATable::class.java)
            startActivity(intent)
        }

        binding.btnMenu.setOnClickListener {
            val intent = Intent(this, FoodMenu::class.java)
            startActivity(intent)
        }
    }
}
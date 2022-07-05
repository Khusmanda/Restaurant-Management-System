package com.thecodefire.brannigans.waiter

import android.content.Intent
import com.thecodefire.brannigans.R

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.thecodefire.brannigans.MainActivity
import com.thecodefire.brannigans.databinding.ActivityWaiterDashboardBinding
import com.thecodefire.brannigans.manager.ManageTables

class WaiterDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityWaiterDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //logout button
        binding.btnLogOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

            // manage tables button
        binding.btnManageTables.setOnClickListener {
            val intent = Intent(this, ManageTables::class.java)
            startActivity(intent)
        }

        // manage orders button
        binding.btnManageOrders.setOnClickListener {
            val intent = Intent(this, ManageOrders::class.java)
            startActivity(intent)
        }

        // manage bills table
        binding.btnManageBills.setOnClickListener {
            val intent = Intent(this, ManageBills::class.java)
            startActivity(intent)
        }

        //remove item from table
        binding.btnRemoveItemFromTable.setOnClickListener { 
            val intent = Intent(this, RemoveItemFromTable::class.java)
            startActivity(intent)
        }
    }
}
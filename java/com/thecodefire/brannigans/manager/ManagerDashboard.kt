package com.thecodefire.brannigans.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.MainActivity
import com.thecodefire.brannigans.databinding.ActivityManagerDashboardBinding
import com.thecodefire.brannigans.waiter.ManageBills
import com.thecodefire.brannigans.waiter.ManageOrders

class ManagerDashboard : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseDatabase.getInstance()
    private lateinit var binding: ActivityManagerDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // booking button
        binding.btnBooking.setOnClickListener {
            val intent = Intent(applicationContext, ManageBookings::class.java)
            startActivity(intent)
        }

        // sales button
        binding.btnSales.setOnClickListener {
            val intent = Intent(applicationContext, ManageSales::class.java)
            startActivity(intent)
        }

        // name and email on top of page
        db.getReference("UserInfo").child("Manager").child(auth.currentUser?.email.toString().substringBefore("@")).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    if(child.key.toString() == "name"){
                        binding.tvMName.text = child.value.toString()
                        binding.tvMEmail.text = auth.currentUser?.email.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { TODO("Not yet implemented") }
        })

        // logout page
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // manage staff button
        binding.btnStaff.setOnClickListener {
            val intent = Intent(applicationContext, ManageStaff::class.java)
            startActivity(intent)
        }

        // manage table button
        binding.btnTable.setOnClickListener {
            val intent = Intent(applicationContext, ManageTables::class.java)
            startActivity(intent)
        }

        // manage menu
        binding.btnMenu.setOnClickListener {
            val intent = Intent(applicationContext, ManageMenu::class.java)
            startActivity(intent)
        }

        // manage bill
        binding.btnBill.setOnClickListener { 
            val intent = Intent(this, ManageBills::class.java)
            startActivity(intent)
        }

        // manage order
        binding.btnOrders.setOnClickListener { 
            val intent = Intent(this, ManageOrders::class.java)
            startActivity(intent)
        }
    }
}
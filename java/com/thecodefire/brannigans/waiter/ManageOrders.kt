package com.thecodefire.brannigans.waiter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thecodefire.brannigans.databinding.ActivityManageOrdersBinding
import com.thecodefire.brannigans.databinding.ActivityWaiterDashboardBinding

class ManageOrders : AppCompatActivity() {
    private lateinit var binding: ActivityManageOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //check status of order
        binding.btnChkOrderStatus.setOnClickListener {
            val intent = Intent(this, OrderStatus::class.java)
            startActivity(intent)
        }

        // take order
        binding.btnTakeOrders.setOnClickListener { 
            val intent = Intent(this, TakeOrders::class.java)
            startActivity(intent)
        }
    }
}
package com.thecodefire.brannigans.waiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.databinding.ActivityOrderStatusBinding
import com.thecodefire.brannigans.manager.OrderStatusRVadapter

class OrderStatus : AppCompatActivity() {
    private lateinit var binding: ActivityOrderStatusBinding
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<DataSnapshot>
    private lateinit var adapter: OrderStatusRVadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuList = ArrayList()
        adapter = OrderStatusRVadapter(this, menuList)
        menuRecyclerView = binding.rvStuView
        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        menuRecyclerView.adapter = adapter

    // get active order
        FirebaseDatabase.getInstance().getReference("ActiveChefOrders").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuList.clear()
                for(postSnapshot in snapshot.children){
                    for(snap in postSnapshot.children){
                        menuList.add(snap)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
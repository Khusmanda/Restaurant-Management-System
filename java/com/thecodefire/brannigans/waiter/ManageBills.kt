package com.thecodefire.brannigans.waiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.databinding.ActivityManageBillsBinding
import com.thecodefire.brannigans.manager.ManageBillsAdapter
import com.thecodefire.brannigans.manager.PlaceOrderAdapter

class ManageBills : AppCompatActivity() {
    private lateinit var binding: ActivityManageBillsBinding

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<DataSnapshot>
    private lateinit var adapter: ManageBillsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuList = ArrayList()
        adapter = ManageBillsAdapter(this, menuList)
        menuRecyclerView = binding.rvStuView
        menuRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        menuRecyclerView.adapter = adapter

        // if status is unpaid put in list
        FirebaseDatabase.getInstance().getReference("Bills").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuList.clear()
                for(postSnapshot in snapshot.children){
                    if(postSnapshot.child("status").value.toString() == "UnPaid"){
                        menuList.add(postSnapshot)
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
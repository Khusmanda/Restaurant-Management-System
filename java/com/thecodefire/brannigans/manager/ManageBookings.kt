package com.thecodefire.brannigans.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.customer.CMenuViewRVadapter
import com.thecodefire.brannigans.databinding.ActivityManageBookingsBinding

class ManageBookings : AppCompatActivity() {
    private lateinit var binding: ActivityManageBookingsBinding
    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableList: ArrayList<TableBookingInfo>
    private lateinit var adapter: TableBookingsRVadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tableList = ArrayList()
        adapter = TableBookingsRVadapter(this, tableList)
        tableRecyclerView = binding.rvStuView
        tableRecyclerView.layoutManager = LinearLayoutManager(this)
        tableRecyclerView.adapter = adapter


        // create tablelist with inactive table
        FirebaseDatabase.getInstance().getReference("TablesBookings").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tableList.clear()
                for(postSnapshot in snapshot.children){
                    if(postSnapshot.child("status").value.toString() == "inActive"){
                        val currentMenu = postSnapshot.getValue(TableBookingInfo::class.java)
                        tableList.add(currentMenu!!)
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
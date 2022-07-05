package com.thecodefire.brannigans.waiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.databinding.ActivityRemoveItemFromTableBinding
import com.thecodefire.brannigans.manager.PlaceOrderAdapter
import com.thecodefire.brannigans.manager.TableNoAdapter

class RemoveItemFromTable : AppCompatActivity() {
    private lateinit var binding: ActivityRemoveItemFromTableBinding

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<DataSnapshot>
    private lateinit var adapter: TableNoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveItemFromTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuList = ArrayList()
        adapter = TableNoAdapter(this, menuList)
        menuRecyclerView = binding.rvStuView
        menuRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        menuRecyclerView.adapter = adapter

        // update order and add food in list again
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
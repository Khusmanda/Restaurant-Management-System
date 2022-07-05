package com.thecodefire.brannigans.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.databinding.ActivityFoodMenuBinding

// display food menu
class FoodMenu : AppCompatActivity() {
    private lateinit var binding: ActivityFoodMenuBinding
    var filter = ""
    var menuType = ""
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<com.thecodefire.brannigans.customer.MenuInfo>
    private lateinit var adapter: CMenuViewRVadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var tableNo = intent.getStringExtra("TABLENO")
        Log.d("CHK2", tableNo.toString())

        // checkout
        binding.btnCheckOut.setOnClickListener {
            val intent = Intent(applicationContext, CheckOut::class.java)
            intent.putExtra("TABLENO2", tableNo.toString())
            startActivity(intent)
        }

        binding.foodFilterSpinner.isEnabled = false
        binding.btnSubmit.isEnabled = false
        // spinner to select food or drink
        val spinner: Spinner = binding.menuTypeSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.foodMenu,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                menuType = spinner.getItemAtPosition(p2).toString()
                // filter type
                if(menuType != "Select Menu Item"){
                    binding.foodFilterSpinner.isEnabled = true
                    binding.btnSubmit.isEnabled = true
                    val spinner: Spinner = binding.foodFilterSpinner
                    ArrayAdapter.createFromResource(
                        applicationContext,
                        R.array.foodFilter,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                    }

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            filter = spinner.getItemAtPosition(p2).toString()
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        // not in use
        menuList = ArrayList() // place checkout
        adapter = CMenuViewRVadapter(this, menuList)
        menuRecyclerView = binding.rvStuView
        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        menuRecyclerView.adapter = adapter

        binding.btnSubmit.setOnClickListener {
            Log.d("CHK", menuType)
            Log.d("CHK", filter)
            FirebaseDatabase.getInstance().getReference("Menu").child(menuType).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    menuList.clear()
                    for(postSnapshot in snapshot.children){
                        if(postSnapshot.child("menuLog").value.toString() == filter){
                            val currentMenu = postSnapshot.getValue(com.thecodefire.brannigans.customer.MenuInfo::class.java)
                            menuList.add(currentMenu!!)
                        }

                        if(filter == "Select Filters" && postSnapshot.child("menuLog").value.toString() != filter){
                            val currentMenu = postSnapshot.getValue(com.thecodefire.brannigans.customer.MenuInfo::class.java)
                            menuList.add(currentMenu!!)
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
}
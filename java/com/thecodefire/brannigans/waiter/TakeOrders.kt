package com.thecodefire.brannigans.waiter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.thecodefire.brannigans.databinding.ActivityTakeOrdersBinding
import com.thecodefire.brannigans.manager.MenuInfo
import com.thecodefire.brannigans.manager.WaiterMenuViewAdapter

class TakeOrders : AppCompatActivity() {
    private lateinit var binding: ActivityTakeOrdersBinding
    var table = ""
    var menuType = ""
    var dateArray = ArrayList<String>()

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<MenuInfo>
    private lateinit var adapter: WaiterMenuViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakeOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // take order on an active table
        FirebaseDatabase.getInstance().getReference("Tables").get().addOnSuccessListener {
            for(snap in it.children){
                if(snap.child("tableStatus").value.toString() == "Active"){
                    dateArray.add(snap.child("tableNo").value.toString())
                }
            }

            // spinner for table
            val dateSpinner: Spinner = binding.course

            val aa = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, dateArray)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dateSpinner.setAdapter(aa)

            dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    table = dateSpinner.getItemAtPosition(p2).toString()

                    val spinner: Spinner = binding.menuType
                    ArrayAdapter.createFromResource(
                        applicationContext,
                        R.array.foodMenu,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                    }

                    // food or drink
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            menuType = spinner.getItemAtPosition(p2).toString()

                            menuList = ArrayList()
                            adapter = WaiterMenuViewAdapter(this@TakeOrders, menuList)
                            menuRecyclerView = binding.rvStuView
                            menuRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                            menuRecyclerView.adapter = adapter

                            binding.btnSubmit.setOnClickListener {
                                FirebaseDatabase.getInstance().getReference("Menu").child(menuType).addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        menuList.clear()
                                        for(postSnapshot in snapshot.children){
                                            val currentMenu = postSnapshot.getValue(MenuInfo::class.java)
                                            menuList.add(currentMenu!!)
                                        }
                                        adapter.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })

                                binding.btnWaiterPLaceOrder.isEnabled = true
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

        //take to next page
        binding.btnWaiterPLaceOrder.setOnClickListener {
            val intent = Intent(this, PlaceOrder::class.java)
            intent.putExtra("TABLENO", table)
            startActivity(intent)
        }
    }
}
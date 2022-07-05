package com.thecodefire.brannigans.manager

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
import com.thecodefire.brannigans.databinding.ActivityManageMenuBinding
import com.thecodefire.brannigans.userInfo

class ManageMenu : AppCompatActivity() {

    private lateinit var binding: ActivityManageMenuBinding
    var menuType = ""

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<MenuInfo>
    private lateinit var adapter: MenuViewAdapterNew
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // listen to add food button
        binding.btnAddMenu.setOnClickListener {
            val intent = Intent(applicationContext, AddMenu::class.java)
            startActivity(intent)
        }
        // food or drink category spinner
        val spinner: Spinner = binding.course
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


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        menuList = ArrayList()
        adapter = MenuViewAdapterNew(this, menuList)
        menuRecyclerView = binding.rvStuView
        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        menuRecyclerView.adapter = adapter

        // bring food list and drink list
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
        }
    }
}
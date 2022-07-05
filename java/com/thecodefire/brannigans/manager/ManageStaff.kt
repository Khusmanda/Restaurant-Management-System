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
import com.thecodefire.brannigans.RegistrationActivity
import com.thecodefire.brannigans.databinding.ActivityManageStaffBinding
import com.thecodefire.brannigans.userInfo

class ManageStaff : AppCompatActivity() {

    private lateinit var binding: ActivityManageStaffBinding
    private lateinit var staffRecyclerView: RecyclerView
    private lateinit var staffList: ArrayList<userInfo>
    private lateinit var adapter: StaffViewRVadapter
    var staffRole = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // spinner for role type
        val spinner: Spinner = binding.course
        ArrayAdapter.createFromResource(
            this,
            R.array.staff,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                staffRole = spinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        staffList = ArrayList()
        adapter = StaffViewRVadapter(this, staffList)
        staffRecyclerView = binding.rvStuView
        staffRecyclerView.layoutManager = LinearLayoutManager(this)
        staffRecyclerView.adapter = adapter

        // select active staff
        binding.btnSubmit.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("UserInfo").child(staffRole).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    staffList.clear()
                    for(postSnapshot in snapshot.children){
                        if(postSnapshot.child("status").value.toString() == "Active"){
                            val currentStudent = postSnapshot.getValue(userInfo::class.java)
                            staffList.add(currentStudent!!)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        binding.btnAddStaff.setOnClickListener {
            val intent = Intent(applicationContext, AddStaff::class.java)
            startActivity(intent)
        }
    }
}
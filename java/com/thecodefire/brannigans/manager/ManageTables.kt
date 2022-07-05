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
import com.thecodefire.brannigans.databinding.ActivityManageTablesBinding
import com.thecodefire.brannigans.userInfo

class ManageTables : AppCompatActivity() {
    private lateinit var binding: ActivityManageTablesBinding
    var tableStatus = ""

    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableList: ArrayList<TableInfo>
    private lateinit var adapter: TableViewRVadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTablesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // view table
        val spinner: Spinner = binding.spStatus
        ArrayAdapter.createFromResource(
            this,
            R.array.tableStatus,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tableStatus = spinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        // get total member of table
        FirebaseDatabase.getInstance().getReference("Tables").get().addOnSuccessListener {
            binding.tvTotalTables.text = "Total Tables : ${it.childrenCount.toString()}"
        }

        tableList = ArrayList()
        adapter = TableViewRVadapter(this, tableList)
        tableRecyclerView = binding.rvStuView
        tableRecyclerView.layoutManager = LinearLayoutManager(this)
        tableRecyclerView.adapter = adapter

        // find table status
        binding.btnSubmit.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Tables").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tableList.clear()
                    for(postSnapshot in snapshot.children){
                        if(postSnapshot.child("tableStatus").value.toString() == tableStatus){
                            val currentTable = postSnapshot.getValue(TableInfo::class.java)
                            tableList.add(currentTable!!)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        // add table button
        binding.btnAddTable.setOnClickListener {
            val intent = Intent(applicationContext, AddTable::class.java)
            startActivity(intent)
        }
    }
}
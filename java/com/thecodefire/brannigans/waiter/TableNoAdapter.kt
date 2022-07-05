package com.thecodefire.brannigans.manager

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.R

class TableNoAdapter(val context: Context, val menuList: ArrayList<DataSnapshot>): RecyclerView.Adapter<TableNoAdapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.table_no_custom_layour, parent, false)
        return menuViewHolder(view)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        var table = ""
        for(snap in currentMenu.children){
            holder.tableNo.text = snap.child("table").value.toString()
            table = snap.child("table").value.toString()
        }

        // view item
        holder.viewItems.setOnClickListener {
            customDialogFunction(table)
        }
    }

    private fun customDialogFunction(table: String) {
        val customDialog = Dialog(context)
        customDialog.setContentView(R.layout.delete_items_custom_dialog)

         lateinit var menuRecyclerView: RecyclerView
         lateinit var menuList: ArrayList<DataSnapshot>
         lateinit var adapter: DeleteItemFromTableAdapter

        menuList = ArrayList()
        adapter = DeleteItemFromTableAdapter(context, menuList)
        menuRecyclerView = customDialog.findViewById(R.id.RVDeleteItemFromTable)
        menuRecyclerView.layoutManager = LinearLayoutManager(context)
        menuRecyclerView.adapter = adapter


        FirebaseDatabase.getInstance().getReference("ActiveChefOrders").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuList.clear()
                for(postSnapshot in snapshot.children){
                    for(snap in postSnapshot.children){
                        for(sn in snap.children){
                            if(sn.child("table").value.toString() == table){
                                menuList.add(sn)
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        customDialog.show()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tableNo = itemView.findViewById<TextView>(R.id.tv_TableNo)
        val viewItems = itemView.findViewById<Button>(R.id.btnViewItems)
    }
}
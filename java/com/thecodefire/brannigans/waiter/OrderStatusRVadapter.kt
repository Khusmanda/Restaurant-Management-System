package com.thecodefire.brannigans.manager

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.userInfo

class OrderStatusRVadapter(val context: Context, val menuList: ArrayList<DataSnapshot>): RecyclerView.Adapter<OrderStatusRVadapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.order_status_custom_layout, parent, false)
        return menuViewHolder(view)
    }

    // find food that is ready to serve and put in array
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        for(snap in currentMenu.children){
            holder.status.text = snap.child("status").value.toString()
            holder.chefId.text = snap.child("chef").value.toString()

            if(snap.child("status").value.toString() == "Ready To Serve..."){
                holder.btnReadyToServe.visibility = View.VISIBLE

                holder.btnReadyToServe.setOnClickListener {
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(snap.child("chef").value.toString()).child(currentMenu.key.toString()).removeValue()
                    notifyDataSetChanged()
                }
            }
        }

        holder.customerID.text = currentMenu.key.toString()

    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val chefId = itemView.findViewById<TextView>(R.id.tv_chef)
        val customerID = itemView.findViewById<TextView>(R.id.tv_customer)
        val status = itemView.findViewById<TextView>(R.id.tv_statusOS)
        val btnReadyToServe = itemView.findViewById<Button>(R.id.btnMarkAsServed)
    }
}
package com.thecodefire.brannigans.customer

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.userInfo
import java.util.*
import kotlin.collections.ArrayList

class ChefOrdersAdapter(val context: Context, val menuList: ArrayList<DataSnapshot>): RecyclerView.Adapter<ChefOrdersAdapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.chef_orders_custom_layout, parent, false)
        return menuViewHolder(view)
    }

    // bring active orders from database
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        holder.menuName.text = currentMenu.child("menuName").value.toString()
        holder.quantity.text = currentMenu.child("quantity").value.toString()

        holder.btnUpdate.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).child(currentMenu.child("customer").value.toString()).child(currentMenu.key.toString()).child("status").setValue("Ready To Serve...")
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
    // buttons on list
    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.tv_mNameCL)
        val quantity = itemView.findViewById<TextView>(R.id.tv_mQuantityCL)
        val btnUpdate = itemView.findViewById<TextView>(R.id.updateStatusBtn)
    }
}
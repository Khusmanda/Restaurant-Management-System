package com.thecodefire.brannigans.manager

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R

class ManageBillsAdapter(val context: Context, val menuList: ArrayList<DataSnapshot>): RecyclerView.Adapter<ManageBillsAdapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.manage_bills_custom_layout, parent, false)
        return menuViewHolder(view)
    }

    //bind everything together
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        holder.foods.text = currentMenu.child("foods").value.toString()
        holder.drinks.text = currentMenu.child("drinks").value.toString()
        holder.total.text = currentMenu.child("total").value.toString()
        holder.cName.text = currentMenu.child("cName").value.toString()

        holder.btnMarkAsPaid.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Bills").child(currentMenu.child("cName").value.toString()).child("status").setValue("Paid")
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val foods = itemView.findViewById<TextView>(R.id.tv_foods)
        val drinks = itemView.findViewById<TextView>(R.id.tv_drinks)
        val total = itemView.findViewById<TextView>(R.id.tv_total)
        val cName = itemView.findViewById<TextView>(R.id.tv_customerMB)
        val btnMarkAsPaid = itemView.findViewById<Button>(R.id.btnMarkAsPaid)
    }
}
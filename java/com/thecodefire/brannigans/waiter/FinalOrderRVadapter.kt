package com.thecodefire.brannigans.customer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.thecodefire.brannigans.R
import kotlin.collections.ArrayList

class FinalOrderRVadapter(val context: Context, val orderList: ArrayList<DataSnapshot>): RecyclerView.Adapter<FinalOrderRVadapter.menuViewHolder>() {
// not in use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.waiter_final_place_order_custom_layout, parent, false)
        return menuViewHolder(view)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = orderList[position]

        holder.mName.text = currentMenu.child("menuName").value.toString()
        holder.mPrice.text = currentMenu.child("menuPrice").value.toString()
        holder.quantity.text = currentMenu.child("quantity").value.toString()
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mName = itemView.findViewById<TextView>(R.id.tv_menuName)
        val mPrice = itemView.findViewById<TextView>(R.id.tv_menuPrice)
        val quantity = itemView.findViewById<TextView>(R.id.tv_quantity)
    }
}
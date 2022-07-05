package com.thecodefire.brannigans.manager

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R

class TableBookingsRVadapter(val context: Context, val tableList: ArrayList<TableBookingInfo>): RecyclerView.Adapter<TableBookingsRVadapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.table_booking_custom_layout, parent, false)
        return menuViewHolder(view)
    }

    // bind all in view
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentUser = tableList[position]

        holder.customerId.text = currentUser.customerId
        holder.tableNo.text = currentUser.tableNo
        holder.tableCapacity.text = currentUser.capacity

        // accept booking
        holder.acceptBooking_btn.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Tables").child(currentUser.tableNo.toString()).child("tableStatus").setValue("Active")
            FirebaseDatabase.getInstance().getReference("Tables").child(currentUser.tableNo.toString()).child("customerId").setValue(currentUser.customerId)

            FirebaseDatabase.getInstance().getReference("TablesBookings").child(currentUser.tableNo.toString()).removeValue()
            notifyDataSetChanged()
        }

        // DENY BOOKING
        holder.denyBooking_btn.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("TablesBookings").child(currentUser.tableNo.toString()).removeValue()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return tableList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tableNo = itemView.findViewById<TextView>(R.id.tvTableNo)
        val customerId = itemView.findViewById<TextView>(R.id.tvCustomerId)
        val tableCapacity = itemView.findViewById<TextView>(R.id.tvCapacityTB)
        val acceptBooking_btn = itemView.findViewById<Button>(R.id.acceptBooking_btn)
        val denyBooking_btn = itemView.findViewById<Button>(R.id.denyBooking_btn)
    }
}
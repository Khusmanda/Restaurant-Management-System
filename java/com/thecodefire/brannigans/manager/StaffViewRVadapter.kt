package com.thecodefire.brannigans.manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.userInfo

class StaffViewRVadapter(val context: Context, val lecturerList: ArrayList<userInfo>): RecyclerView.Adapter<StaffViewRVadapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_staff_layout, parent, false)
        return menuViewHolder(view)
    }

    // binder for staff
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentUser = lecturerList[position]

        holder.staffName.text = currentUser.name
        holder.staffGmail.text = currentUser.email

        holder.delBtn.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("UserRoles").child(currentUser.email.toString().substringBefore('@')).child("role").get().addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("UserInfo").child(it.value.toString()).child(currentUser.email.toString().substringBefore("@")).child("status").setValue("inActive")
            }
        }
    }

    override fun getItemCount(): Int {
        return lecturerList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val staffName = itemView.findViewById<TextView>(R.id.tvStaffName)
        val staffGmail = itemView.findViewById<TextView>(R.id.tvPrice)
        val delBtn = itemView.findViewById<Button>(R.id.del_btn)
    }
}
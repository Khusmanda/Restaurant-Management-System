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

class DeleteItemFromTableAdapter(val context: Context, val menuList: ArrayList<DataSnapshot>): RecyclerView.Adapter<DeleteItemFromTableAdapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.delete_items_custom_dialog_item, parent, false)
        return menuViewHolder(view)
    }


    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        var chef = ""
        var customer = ""
        var menuName = ""


            holder.itemName.text = currentMenu.child("menuName").value.toString()
            chef = currentMenu.child("chef").value.toString()
            customer = currentMenu.child("customer").value.toString()
            menuName = currentMenu.child("menuName").value.toString()

        // delete item
        holder.deleteItem.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(customer).get().addOnSuccessListener {
                for(snap in it.children){
                    if(snap.child("menuName").value.toString() == menuName){
                        FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(customer).child(snap.key.toString()).removeValue()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName = itemView.findViewById<TextView>(R.id.tvItemName)
        val deleteItem = itemView.findViewById<TextView>(R.id.btnDeleteItem)
    }
}
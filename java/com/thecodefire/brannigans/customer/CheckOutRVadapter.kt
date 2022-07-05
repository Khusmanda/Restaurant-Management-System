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
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.userInfo
import java.util.*
import kotlin.collections.ArrayList

// not in use
class CheckOutRVadapter(val context: Context, val menuList: ArrayList<MenuItems>): RecyclerView.Adapter<CheckOutRVadapter.menuViewHolder>() {
        // checkout item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.check_out_item_custom_layout, parent, false)
        return menuViewHolder(view)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        holder.menuName.text = currentMenu.menuName
        holder.menuPrice.text = currentMenu.menuPrice
        holder.menuQuantity.text = currentMenu.quantity

        holder.customizeBtn.setOnClickListener {
            customDialogFunction(currentMenu)
        }

        holder.removeBtn.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("cart").get().addOnSuccessListener {
                var orderId = "0"
                val user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
                for(snap in it.children){
                    if(snap.key.toString() == user){
                        orderId = snap.value.toString()
                    }
                }

                FirebaseDatabase.getInstance().getReference("orders").child(orderId).child(user).child(currentMenu.time.toString()).removeValue()
                notifyDataSetChanged()
            }
        }
    }

    private fun customDialogFunction(currentMenu: MenuItems) {
        val customDialog = Dialog(context)
        customDialog.setContentView(R.layout.customize_quantity_custom_dialog)
        customDialog.findViewById<EditText>(R.id.et_customize_quantity).setText(currentMenu.quantity)
        customDialog.findViewById<TextView>(R.id.btnSubmitOutCD).setOnClickListener {
            val quantity = customDialog.findViewById<EditText>(R.id.et_customize_quantity).text.toString()
            FirebaseDatabase.getInstance().getReference("cart").get().addOnSuccessListener {
                var orderId = "0"
                val user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
                for(snap in it.children){
                    if(snap.key.toString() == user){
                        orderId = snap.value.toString()
                    }
                }
                FirebaseDatabase.getInstance().getReference("orders").child(orderId).child(user).child(currentMenu.time.toString()).child("quantity").setValue(quantity)
                customDialog.dismiss()
            }

        }
        customDialog.show()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.tvMenuName)
        val menuPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        val menuQuantity = itemView.findViewById<TextView>(R.id.tvQuantity)

        val removeBtn = itemView.findViewById<Button>(R.id.checkout_remove_btn)
        val customizeBtn = itemView.findViewById<Button>(R.id.checkout_customize_btn)
    }
}
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

class CMenuViewRVadapter(val context: Context, val menuList: ArrayList<MenuInfo>): RecyclerView.Adapter<CMenuViewRVadapter.menuViewHolder>() {
    // bring menu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.customer_view_menu_layout, parent, false)
        return menuViewHolder(view)
    }

// display name and price
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        holder.menuName.text = currentMenu.menuName
        holder.menuPrice.text = currentMenu.menuPrice
    }

    // not in use
    private fun customDialogFunction(currentMenu: MenuInfo) {
        val customDialog = Dialog(context)
        customDialog.setContentView(R.layout.menu_quantity_custom_dialog)
        customDialog.findViewById<TextView>(R.id.btn_Customer_menu_submit).setOnClickListener {
            var quantity = customDialog.findViewById<EditText>(R.id.et_quantity).text.toString()
            if(quantity == ""){
                quantity = "1"
            }
            val user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
            var customerOrderCount = ""

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR).toString()
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val second = c.get(Calendar.SECOND)
            var time = year+"-"+month+"-"+day+"-"+hour+"-"+minute+"-"+second

            FirebaseDatabase.getInstance().getReference("orders").get().addOnSuccessListener { it1 ->
                var orderCount = ""
                if(it1.childrenCount != 0L){
                    for(snap in it1.children){
                        orderCount = snap.key.toString()
                    }
                }else{
                    orderCount = "0"
                }

                orderCount = (orderCount.toLong() + 1).toString()

                val itUser = it1.value.toString().substringBefore("=").replace("[null, {", "")

                FirebaseDatabase.getInstance().getReference("cart").get().addOnSuccessListener {
                    var bool = false
                    for(child in it.children){
                       if(child.key.toString() == itUser){
                           bool = true
                           orderCount = child.value.toString()
                           break
                       }
                    }

                    if(!bool) {
                        FirebaseDatabase.getInstance().getReference("cart").child(user).setValue(orderCount)
                    }

                    FirebaseDatabase.getInstance().getReference("orders").child(orderCount).child(user).child(time).setValue(currentMenu)
                    FirebaseDatabase.getInstance().getReference("orders").child(orderCount).child(user).child(time).child("quantity").setValue(quantity)
                    FirebaseDatabase.getInstance().getReference("orders").child(orderCount).child(user).child(time).child("status").setValue("Not Placed")
                    FirebaseDatabase.getInstance().getReference("orders").child(orderCount).child(user).child(time).child("time").setValue(time)
                }
            }
            customDialog.dismiss()
        }
        customDialog.show()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.tvMenuName)
        val menuPrice = itemView.findViewById<TextView>(R.id.tvPrice)

    }
}
package com.thecodefire.brannigans.manager

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R

class PlaceOrderAdapter(val context: Context, val menuList: ArrayList<DataSnapshot>): RecyclerView.Adapter<PlaceOrderAdapter.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.place_order_menu_view_custom_layout, parent, false)
        return menuViewHolder(view)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        holder.menuName.text = currentMenu.child("menuName").value.toString()
        holder.menuQuantity.text = currentMenu.child("quantity").value.toString()

        // quantity button
        holder.btnCusQuan.setOnClickListener {
            customDialogFunction(currentMenu)
        }

        // delete button
        holder.btnDel.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("tempOrderList").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).child(currentMenu.key.toString()).removeValue()
            notifyDataSetChanged()
        }
    }
// place item in temporary list
    private fun customDialogFunction(currentMenu: DataSnapshot) {
        val customDialog = Dialog(context)
        customDialog.setContentView(R.layout.customize_quantity_custom_dialog)
        customDialog.findViewById<EditText>(R.id.et_customize_quantity).setText(currentMenu.child("quantity").value.toString())
        customDialog.findViewById<TextView>(R.id.btnSubmitOutCD).setOnClickListener {
            var quantity = customDialog.findViewById<EditText>(R.id.et_customize_quantity).text.toString()
            if(quantity.toInt() != 0){
                FirebaseDatabase.getInstance().getReference("tempOrderList").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).child(currentMenu.key.toString()).child("quantity").setValue(quantity)
                customDialog.dismiss()
            }else{
                quantity = "1"
                FirebaseDatabase.getInstance().getReference("tempOrderList").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).child(currentMenu.key.toString()).child("quantity").setValue(quantity)
                customDialog.dismiss()
            }

        }
        customDialog.show()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.tvMenuNameW)
        val menuQuantity = itemView.findViewById<TextView>(R.id.tvQuantityW)
        val btnCusQuan = itemView.findViewById<Button>(R.id.cusQuan_btnW)
        val btnDel = itemView.findViewById<Button>(R.id.delete_btnW)
    }
}
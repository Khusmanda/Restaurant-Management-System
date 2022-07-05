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
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R

class WaiterMenuViewAdapter(val context: Context, val menuList: ArrayList<MenuInfo>): RecyclerView.Adapter<WaiterMenuViewAdapter.menuViewHolder>() {

    // view menu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.menu_view_waiter_custom_adapter, parent, false)
        return menuViewHolder(view)
    }

    // dahsboard for menu
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        holder.menuName.text = currentMenu.menuName
        holder.menuPrice.text = currentMenu.menuPrice

        holder.btnAdd.setOnClickListener {
            customDialogFunction(currentMenu)
        }
    }

    // dialog box for quantity
    private fun customDialogFunction(currentMenu: MenuInfo) {
        val customDialog = Dialog(context)
        customDialog.setContentView(R.layout.customize_quantity_custom_dialog)
        customDialog.findViewById<TextView>(R.id.btnSubmitOutCD).setOnClickListener {
            var quantity = customDialog.findViewById<TextView>(R.id.et_customize_quantity).text.toString()
            if(quantity == "" || quantity == "0" || quantity < "0"){
                quantity = "1"
            }

            val user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
            FirebaseDatabase.getInstance().getReference("tempOrderList").child  (user).get().addOnSuccessListener {
                var iCount = 0L
                if(it.childrenCount == 0L){
                    iCount++
                }else{
                    iCount = it.childrenCount + 1L
                }

                FirebaseDatabase.getInstance().getReference("tempOrderList").child(user).child(iCount.toString()).setValue(currentMenu)
                FirebaseDatabase.getInstance().getReference("tempOrderList").child(user).child(iCount.toString()).child("quantity").setValue(quantity)
            }
            customDialog.dismiss()
        }
        customDialog.show()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.tvMenuNameW)
        val menuPrice = itemView.findViewById<TextView>(R.id.tvPriceW)
        val btnAdd = itemView.findViewById<Button>(R.id.add_btnW)
    }
}
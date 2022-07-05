package com.thecodefire.brannigans.manager

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R

class MenuViewAdapterNew(val context: Context, val menuList: ArrayList<MenuInfo>): RecyclerView.Adapter<MenuViewAdapterNew.menuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_menu_layout, parent, false)
        return menuViewHolder(view)
    }
    //
    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        holder.menuName.text = currentMenu.menuName
        holder.menuPrice.text = currentMenu.menuPrice

        // delete item
        holder.delBtn.setOnClickListener{
            FirebaseDatabase.getInstance().getReference("Menu").child(currentMenu.menuType.toString()).child(currentMenu.menuId.toString()).removeValue()
        }

        // customize item
        holder.cusBtn.setOnClickListener {
            customDialogFunction(currentMenu)
        }
    }

    // customize button and saving
    private fun customDialogFunction(currentMenu: MenuInfo) {
        val customDialog = Dialog(context)
        customDialog.setContentView(R.layout.customize_menu_custom_dialog)
        customDialog.findViewById<TextView>(R.id.cd_menu_name).setText(currentMenu.menuName.toString())
        customDialog.findViewById<TextView>(R.id.cd_menu_price).setText(currentMenu.menuPrice.toString())
        customDialog.findViewById<TextView>(R.id.cd_menu_log).setText(currentMenu.menuLog.toString())
        customDialog.findViewById<TextView>(R.id.btn_cd_submit).setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Menu").child(currentMenu.menuType.toString()).child(currentMenu.menuId.toString()).child("menuName").setValue(customDialog.findViewById<TextView>(R.id.cd_menu_name).text.toString())
            FirebaseDatabase.getInstance().getReference("Menu").child(currentMenu.menuType.toString()).child(currentMenu.menuId.toString()).child("menuPrice").setValue(customDialog.findViewById<TextView>(R.id.cd_menu_price).text.toString())
            FirebaseDatabase.getInstance().getReference("Menu").child(currentMenu.menuType.toString()).child(currentMenu.menuId.toString()).child("menuLog").setValue(customDialog.findViewById<TextView>(R.id.cd_menu_log).text.toString())
            customDialog.dismiss()
        }
        customDialog.show()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    // view
    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.tvMenuName)
        val menuPrice = itemView.findViewById<TextView>(R.id.tvPrice)

        val delBtn = itemView.findViewById<Button>(R.id.del_menu_btn)
        val cusBtn = itemView.findViewById<Button>(R.id.customize_btn)
    }
}
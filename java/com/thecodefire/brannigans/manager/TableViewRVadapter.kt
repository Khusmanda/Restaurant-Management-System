package com.thecodefire.brannigans.manager

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.userInfo

class TableViewRVadapter(val context: Context, val tableList: ArrayList<TableInfo>): RecyclerView.Adapter<TableViewRVadapter.tableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): tableViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_table_layout, parent, false)
        return tableViewHolder(view)
    }

    //in active table mark as active
    override fun onBindViewHolder(holder: tableViewHolder, position: Int) {
        val currentTable = tableList[position]

        holder.tableNo.text = currentTable.tableNo
        holder.tableStatus.text = currentTable.tableStatus
        holder.cid.text = currentTable.customerId

        if(currentTable.tableStatus == "inActive"){
            holder.btnMarking.setBackgroundColor(Color.GREEN)
            holder.btnMarking.setText("Mark As Active")

            holder.btnMarking.setOnClickListener {
                customDialogFunction(currentTable.tableNo.toString())
                notifyDataSetChanged()
            }
        }

        // active table mark as inactive
        if(currentTable.tableStatus == "Active"){
            holder.btnMarking.setBackgroundColor(Color.RED)
            holder.btnMarking.setText("Mark As inActive")

            holder.btnMarking.setOnClickListener {
                FirebaseDatabase.getInstance().getReference("Tables").child(currentTable.tableNo.toString()).child("tableStatus").setValue("inActive")
                notifyDataSetChanged()
            }
        }

    }

    override fun getItemCount(): Int {
        return tableList.size
    }

    // activate table
    private fun customDialogFunction(tableNumber: String) {
        val customDialog = Dialog(context)
        customDialog.setContentView(R.layout.activate_table_custom_dialog)
        customDialog.findViewById<TextView>(R.id.btn_enter).setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Tables").child(tableNumber).child("tableStatus").setValue("Active")
            FirebaseDatabase.getInstance().getReference("Tables").child(tableNumber).child("customerId").setValue(customDialog.findViewById<TextView>(R.id.et_cid).text.toString())
            if(customDialog.findViewById<TextView>(R.id.et_tCapacity).text.isEmpty()){
                FirebaseDatabase.getInstance().getReference("Tables").child(tableNumber).child("tableCapacity").setValue("4")
            }else{
                FirebaseDatabase.getInstance().getReference("Tables").child(tableNumber).child("tableCapacity").setValue(customDialog.findViewById<TextView>(R.id.et_tCapacity).text.toString())
            }
            customDialog.dismiss()
        }
        customDialog.show()
    }

    class tableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tableNo = itemView.findViewById<TextView>(R.id.tv_tableNo)
        val tableStatus = itemView.findViewById<TextView>(R.id.tv_tableStatus)
        val cid = itemView.findViewById<TextView>(R.id.tv_cid)
        val btnMarking = itemView.findViewById<Button>(R.id.btn_marking)
    }
}
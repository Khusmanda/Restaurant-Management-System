package com.thecodefire.brannigans.waiter

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.databinding.ActivityPlaceOrderBinding
import com.thecodefire.brannigans.manager.MenuInfo
import com.thecodefire.brannigans.manager.PlaceOrderAdapter
import com.thecodefire.brannigans.manager.WaiterMenuViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class PlaceOrder : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceOrderBinding

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<DataSnapshot>
    private lateinit var adapter: PlaceOrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuList = ArrayList()
        adapter = PlaceOrderAdapter(this, menuList)
        menuRecyclerView = binding.rvStuView
        menuRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        menuRecyclerView.adapter = adapter

        FirebaseDatabase.getInstance().getReference("tempOrderList").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuList.clear()
                for(postSnapshot in snapshot.children){
                    val currentMenu = postSnapshot
                    menuList.add(currentMenu!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.btnPlaceOrder.setOnClickListener {
            customDialogFunction()
        }
    }

    private fun customDialogFunction() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.customer_name_custom_disalog)
        var chefArray = ArrayList<String>()
        var chef = ""
        val chefSpinner: Spinner = customDialog.findViewById<Spinner>(R.id.chef_SpinnerCN)

        FirebaseDatabase.getInstance().getReference("UserInfo").child("Chef").get().addOnSuccessListener {
        chefArray.clear()
            for(snap in it.children){
                chefArray.add(snap.key.toString())
            }

            val aa = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, chefArray)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            chefSpinner.setAdapter(aa)

            chefSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    chef = chefSpinner.getItemAtPosition(p2).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

        customDialog.findViewById<Button>(R.id.btnSubmitOutCN).setOnClickListener {
            val name = customDialog.findViewById<TextView>(R.id.et_customer_name).text.toString()

            val table = intent.getStringExtra("TABLENO")
            FirebaseDatabase.getInstance().getReference("Tables").child(table.toString()).child("tableStatus").setValue("Active")
            FirebaseDatabase.getInstance().getReference("Tables").child(table.toString()).child("customerId").setValue(name)

            FirebaseDatabase.getInstance().getReference("tempOrderList").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).get().addOnSuccessListener {
                var c = 0
                var drinks = 0F
                var food = 0F
                for(snap in it.children){
                    c++
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("menuName").setValue(snap.child("menuName").value.toString())
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("quantity").setValue(snap.child("quantity").value.toString())
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("menuPrice").setValue(snap.child("menuPrice").value.toString())
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("menuType").setValue(snap.child("menuType").value.toString())
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("chef").setValue(chef)
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("status").setValue("preparing...")
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("customer").setValue(name)
                    FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(chef).child(name).child(c.toString()).child("table").setValue(table)

                    if(snap.child("menuType").value.toString() == "Foods"){
                        food += snap.child("quantity").value.toString().toFloat() * snap.child("menuPrice").value.toString().toFloat()
                        Log.d("okok", snap.child("quantity").value.toString())
                        Log.d("okok", snap.child("menuPrice").value.toString())
                        Log.d("okok", food.toString())
                    }else{
                        drinks += snap.child("quantity").value.toString().toFloat() * snap.child("menuPrice").value.toString().toFloat()
                    }
                }

                val cal = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR).toString()
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)

                FirebaseDatabase.getInstance().getReference("totalSales").child(year+"-"+month+"-"+day).child(name).child("Drinks").setValue(drinks.toString().substringBefore("."))
                FirebaseDatabase.getInstance().getReference("totalSales").child(year+"-"+month+"-"+day).child(name).child("Foods").setValue(food.toString().substringBefore("."))
                FirebaseDatabase.getInstance().getReference("totalSales").child(year+"-"+month+"-"+day).child(name).child("Total").setValue((food+drinks).toString())

                FirebaseDatabase.getInstance().getReference("Bills").child(name).child("foods").setValue(food)
                FirebaseDatabase.getInstance().getReference("Bills").child(name).child("drinks").setValue(drinks)
                FirebaseDatabase.getInstance().getReference("Bills").child(name).child("total").setValue(food+drinks)
                FirebaseDatabase.getInstance().getReference("Bills").child(name).child("status").setValue("UnPaid")
                FirebaseDatabase.getInstance().getReference("Bills").child(name).child("cName").setValue(name)

                FirebaseDatabase.getInstance().getReference("tempOrderList").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).removeValue().addOnSuccessListener {
                    Toast.makeText(applicationContext, "Order Placed...", Toast.LENGTH_LONG).show()
                }
            }


            customDialog.dismiss()
        }
        customDialog.show()
    }
}
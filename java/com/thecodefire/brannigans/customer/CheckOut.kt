package com.thecodefire.brannigans.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.databinding.ActivityCheckOutBinding
import java.util.*
import kotlin.collections.ArrayList

class CheckOut : AppCompatActivity() {
        // not being used
    private lateinit var binidng: ActivityCheckOutBinding
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<MenuItems>
    private lateinit var adapter: CheckOutRVadapter
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR).toString()
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val time = year+"-"+month+"-"+day
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binidng = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binidng.root)

        var tableno2 = intent.getStringExtra("TABLENO2")
        Log.d("CHK2", tableno2.toString())

        val user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
        FirebaseDatabase.getInstance().getReference("totalSales").child(time).child(user).child("Foods").setValue("0")
        FirebaseDatabase.getInstance().getReference("totalSales").child(time).child(user).child("Drinks").setValue("0")

        menuList = ArrayList()
        adapter = CheckOutRVadapter(this, menuList)
        menuRecyclerView = binidng.rvcartView
        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        menuRecyclerView.adapter = adapter

        FirebaseDatabase.getInstance().getReference("cart").get().addOnSuccessListener {
            var orderId = "0"
            val user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
            for(snap in it.children){
                if(snap.key.toString() == user){
                    orderId = snap.value.toString()
                }
            }

            FirebaseDatabase.getInstance().getReference("orders").child(orderId).child(user).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    menuList.clear()
                    for(postSnapshot in snapshot.children){
                        if(postSnapshot.child("status").value.toString() == "Not Placed"){
                            val currentMenu = postSnapshot.getValue(MenuItems::class.java)
                            menuList.add(currentMenu!!)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        binidng.btnPlaceOrder.setOnClickListener {
//            FirebaseDatabase.getInstance().getReference("ActiveChefOrders").get().addOnSuccessListener {
//                for(snap in it.children){
//                    if(snap.value.toString().substringBefore("=").replace("{", "") == user){
//                        binidng.btnWarn.setText("Your Previous Order is on the Way")
//                    }
//                }
//            }
            FirebaseDatabase.getInstance().getReference("cart").get().addOnSuccessListener {
                var orderId = "0"
                val user = FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")
                for(snap in it.children){
                    if(snap.key.toString() == user){
                        orderId = snap.value.toString()
                    }
                }

                FirebaseDatabase.getInstance().getReference("orders").child(orderId).child(user).get().addOnSuccessListener { it2 ->
                    var c = 0
                    var temp = ""
                    for(snap in it2.children){
                        FirebaseDatabase.getInstance().getReference("orders").child(orderId).child(user).child(snap.key.toString()).child("status").setValue("Placed")
                        FirebaseDatabase.getInstance().getReference("orders").child(orderId).child(user).child(snap.key.toString()).child("tableNo").setValue(tableno2)
                        c++

                        var menuType = "Foods"
                        if(snap.child("menuType").value.toString() == "Drinks"){
                            menuType = "Drinks"
                        }

                        if(snap.child("status").value.toString() == "Placed"){
                            binidng.btnWarn.setText("Your Previous Order is on the Way")
                        }

                        temp = menuType

                        var total = snap.child("menuPrice").value.toString().toInt() * snap.child("quantity").value.toString().toInt()
                        Log.d("CHK7", menuType)
                        Log.d("CHK7", snap.child("menuPrice").value.toString())
                        Log.d("CHK7", snap.child("quantity").value.toString())
                        Log.d("CHK7", total.toString())

                        FirebaseDatabase.getInstance().getReference("total"+user).child(menuType).child(c.toString()).setValue(total.toString())
                    }

                    FirebaseDatabase.getInstance().getReference("total"+user).child("Drinks").get().addOnSuccessListener {
                        var total1 = 0
                        for(i in it.children){
                            total1 += i.value.toString().toInt()
                        }

                        FirebaseDatabase.getInstance().getReference("totalSales").child(time).child(user).child("Drinks").setValue(total1.toString()).addOnSuccessListener {
                            FirebaseDatabase.getInstance().getReference("total"+user).child("Drinks").removeValue()
                        }

                        FirebaseDatabase.getInstance().getReference("total"+user).child("Foods").get().addOnSuccessListener {
                            var total2 = 0
                            for(i in it.children){
                                total2 += i.value.toString().toInt()
                            }

                            FirebaseDatabase.getInstance().getReference("totalSales").child(time).child(user).child("Foods").setValue(total2.toString()).addOnSuccessListener {
                                FirebaseDatabase.getInstance().getReference("total"+user).child("Foods").removeValue()
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.thecodefire.brannigans.chef

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.MainActivity
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.customer.ChefOrdersAdapter
import com.thecodefire.brannigans.databinding.ActivityChefDashboardBinding
import com.thecodefire.brannigans.manager.PlaceOrderAdapter

class ChefDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityChefDashboardBinding

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuList: ArrayList<DataSnapshot>
    private lateinit var adapter: ChefOrdersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChefDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuList = ArrayList()
        adapter = ChefOrdersAdapter(this, menuList)
        menuRecyclerView = binding.rvStuView
        menuRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        menuRecyclerView.adapter = adapter
        // call the active orders which has status preparing
        FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuList.clear()
                for (postSnapshot in snapshot.children){
                    for(snap in postSnapshot.children){
                        Log.d("okok", snap.child("status").value.toString())
                        if(snap.child("status").value.toString() == "preparing..."){
                            menuList.add(snap)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

//        FirebaseDatabase.getInstance().getReference("ActiveChefOrders").child(FirebaseAuth.getInstance().currentUser?.email.toString().substringBefore("@")).get().addOnSuccessListener {
//            for(snap in it.children){
//                for(s in snap.children){
//                    if(s.child("status").value.toString() == "preparing..."){
//                        menuList.add(s)
//                    }
//                }
//            }
//            adapter.notifyDataSetChanged()
//        }

            // logout
        binding.btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
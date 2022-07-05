package com.thecodefire.brannigans

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.chef.ChefDashboard
import com.thecodefire.brannigans.customer.CustomerDashboard
import com.thecodefire.brannigans.manager.ManagerDashboard
import com.thecodefire.brannigans.waiter.WaiterDashboard

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val backgroundImg : ImageView = findViewById(R.id.iv_logo)
        val sideAnimation = AnimationUtils.loadAnimation(this,R.anim.slide)
        backgroundImg.startAnimation(sideAnimation)
        Handler().postDelayed({
            if(FirebaseAuth.getInstance().currentUser != null){
                db.getReference("UserRoles").addValueEventListener(object:
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(child in snapshot.children){
                            if(child.key.toString() == auth.currentUser?.email.toString().substringBefore("@")){
                                if(child.child("role").value == "Customer"){
                                    val intent = Intent(applicationContext, CustomerDashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                }else if(child.child("role").value == "Manager"){
                                    if(child.child("status").value == "Active"){
                                        val intent = Intent(applicationContext, ManagerDashboard::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        auth.signOut()
                                        val intent = Intent(applicationContext, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }else if(child.child("role").value == "Chef"){
                                    if(child.child("status").value == "Active"){
                                        val intent = Intent(applicationContext, ChefDashboard::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        auth.signOut()
                                        val intent = Intent(applicationContext, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }else if(child.child("role").value == "Waiter"){
                                    if(child.child("status").value == "Active"){
                                        val intent = Intent(applicationContext, WaiterDashboard::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        auth.signOut()
                                        val intent = Intent(applicationContext, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }else{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        },2000)

    }
}

package com.thecodefire.brannigans

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thecodefire.brannigans.chef.ChefDashboard
import com.thecodefire.brannigans.customer.CustomerDashboard
import com.thecodefire.brannigans.databinding.ActivityMainBinding
import com.thecodefire.brannigans.manager.ManagerDashboard
import com.thecodefire.brannigans.waiter.WaiterDashboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // login button
        binding.btnLogin.setOnClickListener {
            if(checkFieldNotEmpty()){
                signIn()
            }
        }

        // signup button
        binding.tvSignup.setOnClickListener {
            val intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // forget password buton
        binding.tvForgotPassword.setOnClickListener {
            customDialogFunction()
        }
    }

    // reset password dialog box
    private fun customDialogFunction() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.password_reset_custom_dialog)
        customDialog.findViewById<TextView>(R.id.btn_send_pr).setOnClickListener {
            auth.sendPasswordResetEmail(customDialog.findViewById<TextView>(R.id.et_gmail_pr).text.toString()).addOnSuccessListener {
                Toast.makeText(applicationContext, "Password Reset Mail Sent Successfully", Toast.LENGTH_LONG).show()
                customDialog.dismiss()
            }
        }
        customDialog.show()
    }

    // login should not be empty
    private fun checkFieldNotEmpty(): Boolean{
        if(binding.etUsername.text?.isNotEmpty() == true && binding.etPassword.text?.isNotEmpty() == true && binding.etUsername.text!!.endsWith("@gmail.com")){
            return true
        }
        Toast.makeText(applicationContext, "Username or Password must not empty", Toast.LENGTH_LONG).show()
        return false
    }

    @Suppress("DEPRECATION")
    private fun signIn(){
        binding.loading.visibility = View.VISIBLE
        Handler().postDelayed({
            binding.loading.visibility = View.INVISIBLE
        },3000)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //username and password
                auth.signInWithEmailAndPassword(binding.etUsername.text.toString(), binding.etPassword.text.toString()).addOnCompleteListener {
                }.await()
                withContext(Dispatchers.Main){
                    checkLoggedInState()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // check logging state
    private fun checkLoggedInState(){
        if(auth.currentUser == null){
            Toast.makeText(applicationContext, "Fail to Log IN", Toast.LENGTH_LONG).show()
        }else{
          db.getReference("UserRoles").addValueEventListener(object: ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                  for(child in snapshot.children){
                      if(child.key.toString() == auth.currentUser?.email.toString().substringBefore("@")){
                          // roles == customer
                          if(child.child("role").value == "Customer"){
                              val intent = Intent(applicationContext, CustomerDashboard::class.java)
                              startActivity(intent)
                              finish()
                              // role == manager
                          }else if(child.child("role").value == "Manager"){
                              if(child.child("status").value == "Active"){
                                  val intent = Intent(applicationContext, ManagerDashboard::class.java)
                                  startActivity(intent)
                                  finish()
                              }else{
                                  Toast.makeText(applicationContext, "Your Profile is not currently active", Toast.LENGTH_LONG).show()
                                  auth.signOut()
                              }
                              // role == chef
                          }else if(child.child("role").value == "Chef"){
                              if(child.child("status").value == "Active"){
                                  val intent = Intent(applicationContext, ChefDashboard::class.java)
                                  startActivity(intent)
                                  finish()
                              }else{
                                  Toast.makeText(applicationContext, "Your Profile is not currently active", Toast.LENGTH_LONG).show()
                                  auth.signOut()
                              }
                              // role == waiter
                          }else if(child.child("role").value == "Waiter"){
                              if(child.child("status").value == "Active"){
                                  val intent = Intent(applicationContext, WaiterDashboard::class.java)
                                  startActivity(intent)
                                  finish()
                              }else{
                                  Toast.makeText(applicationContext, "Your Profile is not currently active", Toast.LENGTH_LONG).show()
                                  auth.signOut()
                              }
                          }
                      }
                  }
              }

              override fun onCancelled(error: DatabaseError) {
                  TODO("Not yet implemented")
              }

          })
        }
    }
}
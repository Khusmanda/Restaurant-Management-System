package com.thecodefire.brannigans

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.thecodefire.brannigans.databinding.ActivityRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    var userRole = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar?
        actionBar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#0D2B47"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        val spinner: Spinner = binding.userRoles
        ArrayAdapter.createFromResource(
            this,
            R.array.userRoles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userRole = spinner.getItemAtPosition(p2).toString()

                if(userRole!="Select User Roles"){
                    binding.tilGmail.isEnabled = true
                    binding.tilPassword.isEnabled = true
                    binding.tilUsername.isEnabled = true
                    binding.btnLogin.isEnabled = true

                    if(userRole=="Customer"){
                        binding.btnLogin.text = "Register"
                    }else{
                        binding.btnLogin.text = "Request Registration"
                    }

                    binding.btnLogin.setOnClickListener {
                        if(checkFeildNotEmpty()){
                            if(binding.etGmail.text?.endsWith("@gmail.com") == true){
                                register(userRole)
                            }else{
                                Toast.makeText(applicationContext, "Please use proper gmail address \n or use gmail in lowercase", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }else{
                    binding.tilGmail.isEnabled = false
                    binding.tilPassword.isEnabled = false
                    binding.tilUsername.isEnabled = false
                    binding.btnLogin.isEnabled = false
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun checkFeildNotEmpty(): Boolean{
        if(binding.etUsername.text?.isNotEmpty() == true && binding.etGmail.text?.isNotEmpty() == true && binding.etPassword.text?.isNotEmpty() == true){
            return true
        }
        return false
    }

    @Suppress("DEPRECATION")
    private fun register(userRole: String){
        var status = "inActive"
        if(userRole == "Customer"){
            status = "Active"
        }

        binding.loading.visibility = View.VISIBLE
        Handler().postDelayed({
            binding.loading.visibility = View.INVISIBLE
        },3000)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.etGmail.text.toString(), binding.etPassword.text.toString()).addOnCompleteListener {
                    binding.etGmail.text?.clear()
                    binding.etUsername.text?.clear()
                    binding.etPassword.text?.clear()

                    FirebaseDatabase.getInstance().getReference("UserInfo").child(userRole).child(binding.etGmail.text.toString().substringBefore("@")).setValue(userInfo(binding.etUsername.text.toString(), binding.etGmail.text.toString(), binding.etPassword.text.toString(), status)).addOnFailureListener{
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }

                    FirebaseDatabase.getInstance().getReference("UserRoles").child(binding.etGmail.text.toString().substringBefore("@")).child("role").setValue(userRole)
                    FirebaseDatabase.getInstance().getReference("UserRoles").child(binding.etGmail.text.toString().substringBefore("@")).child("status").setValue(status)
                }.await()
                withContext(Dispatchers.Main){
                    checkLoggedInState()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RegistrationActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // check login state
    private fun checkLoggedInState(){
        if(FirebaseAuth.getInstance().currentUser == null){
            binding.tvLogInState.text = "FALIURE"
        }else{
            if(userRole != "Customer"){
                binding.tvLogInState.text = "ACCESS REQUEST SEND SUCCESSFULLY TO ADMIN"
            }else{
                binding.tvLogInState.text = "REGISTRATION SUCCESS - YOU CAN LOGIN NOW"
            }

            FirebaseAuth.getInstance().signOut()
        }
    }
}
package com.thecodefire.brannigans.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.databinding.ActivityAddStaffBinding
import com.thecodefire.brannigans.userInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddStaff : AppCompatActivity() {
    private lateinit var binding: ActivityAddStaffBinding
    var userRole = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // select from spinner staff role
        val spinner: Spinner = binding.staffRoles
        ArrayAdapter.createFromResource(
            this,
            R.array.staff,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userRole = spinner.getItemAtPosition(p2).toString()

                if(userRole!="Select Staff"){
                    binding.tilGmail.isEnabled = true
                    binding.tilPassword.isEnabled = true
                    binding.tilUsername.isEnabled = true
                    binding.btnLogin.isEnabled = true

                    // only gmail is accepted
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

    // field nnot empty
    private fun checkFeildNotEmpty(): Boolean{
        if(binding.etUsername.text?.isNotEmpty() == true && binding.etGmail.text?.isNotEmpty() == true && binding.etPassword.text?.isNotEmpty() == true){
            return true
        }
        return false
    }

    @Suppress("DEPRECATION")
    private fun register(userRole: String){

        binding.loading.visibility = View.VISIBLE
        Handler().postDelayed({
            binding.loading.visibility = View.INVISIBLE
        },3000)

        // register user
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.etGmail.text.toString(), binding.etPassword.text.toString()).addOnSuccessListener {
                    FirebaseDatabase.getInstance().getReference("UserInfo").child(userRole).child(binding.etGmail.text.toString().substringBefore("@")).setValue(
                        userInfo(binding.etUsername.text.toString(), binding.etGmail.text.toString(), binding.etPassword.text.toString(), "Active")
                    ).addOnFailureListener{
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                    FirebaseDatabase.getInstance().getReference("UserRoles").child(binding.etGmail.text.toString().substringBefore("@")).child("role").setValue(userRole)
                    FirebaseDatabase.getInstance().getReference("UserRoles").child(binding.etGmail.text.toString().substringBefore("@")).child("status").setValue("Active")

                    binding.etGmail.text?.clear()
                    binding.etUsername.text?.clear()
                    binding.etPassword.text?.clear()
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

    private fun checkLoggedInState(){
        if(FirebaseAuth.getInstance().currentUser == null){
            binding.tvLogInState.text = "FALIURE"
        }else{
            binding.tvLogInState.text = "STAFF REGISTRATION SUCCESS - STAFF CAN LOGIN NOW \n "
            Toast.makeText(applicationContext, "STAFF REGISTRATION SUCCESSFUL ", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
        }
    }
}
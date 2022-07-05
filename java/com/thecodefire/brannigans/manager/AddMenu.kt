package com.thecodefire.brannigans.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.thecodefire.brannigans.R
import com.thecodefire.brannigans.databinding.ActivityAddMenuBinding

class AddMenu : AppCompatActivity() {
    private lateinit var binding: ActivityAddMenuBinding
    var menu = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // select menu type
        val spinner: Spinner = binding.foodSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.foodMenu,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                menu = spinner.getItemAtPosition(p2).toString()

                if(menu != "Select Menu Item"){
                    binding.btnAddMenu.isEnabled = true
                    binding.etMenuName.isEnabled = true
                    binding.etMenuPrice.isEnabled = true

                    binding.btnAddMenu.setOnClickListener {
                        if(checkFeildNotEmpty()){
                            FirebaseDatabase.getInstance().getReference("Menu").child(menu).get().addOnSuccessListener {
                                var mCount = ""
                                if(it.childrenCount != 0L){
                                    for(snap in it.children){
                                        mCount = snap.key.toString()
                                    }
                                }else{
                                    mCount = "0"
                                }

                                Log.d("CHK", mCount)
                                mCount = (mCount.toInt() + 1).toString()
                                // add to firebase
                                FirebaseDatabase.getInstance().getReference("Menu").child(menu).child(mCount).setValue(MenuInfo(mCount, binding.etMenuName.text.toString(), binding.etMenuPrice.text.toString(), menu, "Select Filters")).addOnSuccessListener {
                                    binding.tvStatus.text = "Menu Added Successfully"

                                    binding.etMenuName.setText("")
                                    binding.etMenuPrice.setText("")

                                }
                            }
                        }else{
                            Toast.makeText(applicationContext, "Menu Name and Menu Price should not empty.", Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    binding.btnAddMenu.isEnabled = false
                    binding.etMenuName.isEnabled = false
                    binding.etMenuPrice.isEnabled = false
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun checkFeildNotEmpty(): Boolean{
        if(binding.etMenuName.text?.isNotEmpty() == true && binding.etMenuPrice.text?.isNotEmpty() == true){
            return true
        }
        return false
    }
}
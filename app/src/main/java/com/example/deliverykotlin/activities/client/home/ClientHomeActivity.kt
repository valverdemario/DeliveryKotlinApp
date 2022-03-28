package com.example.deliverykotlin.activities.client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.deliverykotlin.R
import com.example.deliverykotlin.activities.MainActivity
import com.example.deliverykotlin.databinding.ActivityClientHomeBinding
import com.example.deliverykotlin.databinding.ActivityMainBinding
import com.example.deliverykotlin.models.User
import com.example.deliverykotlin.utils.SharePref
import com.google.gson.Gson

class ClientHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientHomeBinding
    var sharePref : SharePref? = null
    private val TAG = "CLIENTHOMEACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)
        binding = ActivityClientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root);
        getUserSession()
         sharePref = SharePref(this)
        binding.btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout(){
        sharePref?.remove("user")
        goToLogin()
    }

    private fun getUserSession(){
         sharePref = SharePref(this)
        val gson = Gson()
        if(!sharePref?.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharePref?.getData("user"),User::class.java)
            Log.d(TAG,"${user}")
        }
    }

    private fun goToLogin(){
        val i = Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        finish()
    }
}
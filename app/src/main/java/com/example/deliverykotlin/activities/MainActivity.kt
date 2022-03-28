package com.example.deliverykotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.deliverykotlin.R
import com.example.deliverykotlin.activities.client.home.ClientHomeActivity
import com.example.deliverykotlin.databinding.ActivityMainBinding
import com.example.deliverykotlin.models.ResponseHttp
import com.example.deliverykotlin.models.User
import com.example.deliverykotlin.providers.UsersProvider
import com.example.deliverykotlin.utils.SharePref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var userProvider = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root);
        getUserSession()
        binding.goToRegister.setOnClickListener {
            goToRegister()
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (isValidForm(email, password)) {
            userProvider.login(email, password)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Toast.makeText(this@MainActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    if(response.body()?.success == true){
                        Log.d("LOGIN","Reponse: ${response.body()}")
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }else{
                        Log.d("LOGIN","Reponse ERROR: ${response.body()}")
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
            Toast.makeText(this, "Valido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Invalido", Toast.LENGTH_SHORT).show()
        }


    }

    private fun isValidForm(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            return false
        }
        if (!email.isEmailValid()) {
            return false
        }
        return true;
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)

    }

    private fun saveUserInSession(data:String){
        val sharePref = SharePref(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharePref.save("user",user)
    }
    private fun goToClientHome(){
        val i = Intent(this,ClientHomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        finish()
    }
    private fun getUserSession(){
        val sharePref = SharePref(this)
        val gson = Gson()
        if(!sharePref.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharePref.getData("user"),User::class.java)
            goToClientHome()
        }
    }
}
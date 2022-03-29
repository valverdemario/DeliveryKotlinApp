package com.example.deliverykotlin.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deliverykotlin.activities.client.home.ClientHomeActivity
import com.example.deliverykotlin.databinding.ActivityRegisterBinding
import com.example.deliverykotlin.models.ResponseHttp
import com.example.deliverykotlin.models.User
import com.example.deliverykotlin.providers.UsersProvider
import com.example.deliverykotlin.utils.SharePref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.InetAddress

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    var userProvider = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root);
        binding.goToLogin.setOnClickListener {
            goToLogin()
        }
        binding.btnRegister.setOnClickListener {
            register()
        }
    }


    private fun register() {
        val name = binding.etName.text.toString()
        val lastName = binding.etLastname.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val password = binding.etPassword.text.toString()
        val repeatPassword = binding.etRepeatPassword.text.toString()



        if (isValidForm(email, password, name, lastName, phone, repeatPassword)) {

            val user = User(
                name = name,
                lastName = lastName,
                email = email,
                phone = phone,
                password = password
            )

            userProvider.register(user)?.enqueue(object : Callback<ResponseHttp> {

                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {

                    if(response.isSuccessful){
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_SHORT)
                        .show()
                    Log.d("REGISTRO", "${response}")
                    Log.d("REGISTRO", "${response.body()?.success}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("ERROR", "Ocurrio un error ${t.message}")
                    Log.d("ERROR", "Ocurrio un error ${t.localizedMessage}")
                    Log.d("ERROR", "Ocurrio un error ${t.cause}")
                    Toast.makeText(this@RegisterActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        } else {
            Toast.makeText(this, "Formulario invalido", Toast.LENGTH_SHORT).show()
        }

        Log.d("REGISTRO", name)
        Log.d("REGISTRO", lastName)
        Log.d("REGISTRO", email)
        Log.d("REGISTRO", phone)
        Log.d("REGISTRO", password)
        Log.d("REGISTRO", repeatPassword)
    }
    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        finish()
    }

    private fun isValidForm(
        email: String,
        password: String,
        name: String,
        lastName: String,
        phone: String,
        repeatPassword: String
    ): Boolean {
        if (email.isBlank() || password.isBlank() || name.isBlank() || lastName.isBlank() || phone.isBlank() || repeatPassword.isBlank()) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!email.isEmailValid()) {
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (password != repeatPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }
        return true;
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }


    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        finish()
    }

    private fun saveUserInSession(data:String){
        val sharePref = SharePref(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharePref.save("user",user)
    }
}
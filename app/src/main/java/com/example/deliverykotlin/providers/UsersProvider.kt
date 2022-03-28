package com.example.deliverykotlin.providers

import android.util.Log
import com.example.deliverykotlin.api.ApiRoutes
import com.example.deliverykotlin.models.ResponseHttp
import com.example.deliverykotlin.models.User
import com.example.deliverykotlin.routes.UsersRoutes
import retrofit2.Call

class UsersProvider {
    private var usersRoutes: UsersRoutes? = null;

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {

        Log.d("USUARIO", user.lastName)
        return usersRoutes?.register(user)

    }


    fun login(email: String, password: String): Call<ResponseHttp>? {


        return usersRoutes?.login(email, password)

    }


}
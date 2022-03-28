package com.example.deliverykotlin.api

import com.example.deliverykotlin.routes.UsersRoutes


class ApiRoutes {

    val retrofit = RetrofitClient()
    fun getUsersRoutes():UsersRoutes{
        return retrofit.getClient().create(UsersRoutes::class.java)
    }

}
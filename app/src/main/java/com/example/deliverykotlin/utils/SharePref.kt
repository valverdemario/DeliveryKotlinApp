package com.example.deliverykotlin.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import java.lang.Exception

class SharePref(activity: Activity) {
    private var pref:SharedPreferences ? = null

    init {
        pref = activity.getSharedPreferences("com.example.deliverykotlin", Context.MODE_PRIVATE)
    }

    fun save(key:String,objecto:Any){
        try {
            val gson = Gson()
            val json = gson.toJson(objecto)
            with(pref?.edit()){
                this?.putString(key,json)
                this?.commit()
            }
        }catch (e:Exception){
            Log.d("ERRO", "${e.message}")
        }
    }

    fun getData(key: String): String? {
        return pref?.getString(key, "")
    }

    fun remove(key:String){
        pref?.edit()?.remove(key)?.apply()
    }
}
package com.marcinmejner.czatownik.Services

import android.content.Context
import org.json.JSONObject

object AuthService {

    fun registerUser(context: Context, email: String, passowrd: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", passowrd)

        val requestBody = jsonBody.toString()




    }
}
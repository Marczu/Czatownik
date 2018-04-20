package com.marcinmejner.czatownik.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.marcinmejner.czatownik.R
import com.marcinmejner.czatownik.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginCreateNewUserBtnClicked(view: View){
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)

    }

    fun loginLoginBtnClicked(view: View){

        val password = loginPasswordEdt.text.toString()
        val email = loginEmailEdt.text.toString()

        AuthService.loginUser(this, email, password) { loginSuccess ->
            if(loginSuccess){
                Log.d(TAG, "Token: ${AuthService.authToken}  Email: ${AuthService.userEmail} ")

            }
        }

    }
}

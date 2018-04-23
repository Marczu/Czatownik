package com.marcinmejner.czatownik.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.marcinmejner.czatownik.R
import com.marcinmejner.czatownik.R.id.*
import com.marcinmejner.czatownik.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginProgressBar.visibility = View.INVISIBLE
    }

    fun loginCreateNewUserBtnClicked(view: View){
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun loginLoginBtnClicked(view: View){

        enableSpinner(true)

        val password = loginPasswordEdt.text.toString()
        val email = loginEmailEdt.text.toString()

        hideKeyboard()

        if(email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.loginUser(this, email, password) { loginSuccess ->
                if(loginSuccess){
                    AuthService.findUserByEmail(this){findSucces ->
                        if(findSucces){
                            enableSpinner(false)
                            finish()
                        }else{
                            errorToast()
                        }
                    }
                    Log.d(TAG, "Token: ${App.prefs.authToken}  Email: ${App.prefs.userEmail} ")

                }else{
                    errorToast()
                }
            }
        }else{
            Toast.makeText(this, "Please fill both email and password", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }



    }

    fun errorToast(){
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    /*Włączamy widoczność ProgressBar, i wyłączamy przysicki, na czas tworzenia/rejestrowania/logowania usera*/
    fun enableSpinner(enable: Boolean) {
        if(enable){
            loginProgressBar.visibility = View.VISIBLE
        }else{
            loginProgressBar.visibility = View.INVISIBLE
        }
        loginLoginBtn.isEnabled = !enable
        loginRegisterNewAccountBtn.isEnabled = !enable
    }

    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}

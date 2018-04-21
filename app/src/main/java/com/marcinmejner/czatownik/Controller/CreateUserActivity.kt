package com.marcinmejner.czatownik.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.marcinmejner.czatownik.R
import com.marcinmejner.czatownik.R.id.createProgressBar
import com.marcinmejner.czatownik.Services.AuthService
import com.marcinmejner.czatownik.Services.UserDataService
import com.marcinmejner.czatownik.Utils.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    private val TAG = "CreateUserActivity"

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createProgressBar.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if(color == 0) {
            userAvatar = "light$avatar"
        }else{
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatar_Iv.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View){

        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatar_Iv.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"

    }

    fun createUserClicked(view: View){

        enableSpinner(true)
        val userName = createUserNameEdt.text.toString()
        val password = createPasswordEdt.text.toString()
        val email = createEmailEdt.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){

            AuthService.registerUser(this, email, password) {registerSuccess ->
                if(registerSuccess){
                    Log.d(TAG, "Utworzenie nowego usera powiodłosię: " + registerSuccess.toString())
                    AuthService.loginUser(this, email, password) {loginSuccess ->
                        if(loginSuccess){
                            Log.d(TAG, "Token: ${AuthService.authToken}  Email: ${AuthService.userEmail} ")
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) {createSucces ->
                                if(createSucces){

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    Log.d(TAG, "Dane usera: Name: " +
                                            "${UserDataService.name}," +
                                            "\n Email: ${UserDataService.email}," +
                                            "\n AvatarName: ${UserDataService.avatarName}, " +
                                            "\n AvatarColor ${UserDataService.avatarColor} ")
                                    enableSpinner(false)
                                    finish()
                                }else{
                                    errorToast()
                                }
                            }

                        }else{
                            errorToast()
                        }
                    }
                } else{
                    errorToast()
                }
            }

        }else{
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show()
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
            createProgressBar.visibility = View.VISIBLE
        }else{
            createProgressBar.visibility = View.INVISIBLE
        }
        createCreateUserBtn.isEnabled = !enable
        createAvatar_Iv.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }
}

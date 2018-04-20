package com.marcinmejner.czatownik.Controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.marcinmejner.czatownik.R
import com.marcinmejner.czatownik.Services.AuthService
import com.marcinmejner.czatownik.Services.UserDataService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    private val TAG = "CreateUserActivity"

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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

        val userName = createUserNameEdt.text.toString()

        val password = createPasswordEdt.text.toString()
        val email = createEmailEdt.text.toString()


        AuthService.registerUser(this, email, password) {registerSuccess ->
            if(registerSuccess){
                Log.d(TAG, "Utworzenie nowego usera powiodłosię: " + registerSuccess.toString())
                AuthService.loginUser(this, email, password) {loginSuccess ->
                    if(loginSuccess){
                        Log.d(TAG, "Token: ${AuthService.authToken}  Email: ${AuthService.userEmail} ")
                        AuthService.createUser(this, userName, email, userAvatar, avatarColor) {createSucces ->
                            if(createSucces){
                                Log.d(TAG, "Dane usera: Name: " +
                                        "${UserDataService.name}," +
                                        "\n Email: ${UserDataService.email}," +
                                        "\n AvatarName: ${UserDataService.avatarName}, " +
                                        "\n AvatarColor ${UserDataService.avatarColor} ")
                                finish()
                            }
                        }

                    }
                }
            }
        }

    }
}

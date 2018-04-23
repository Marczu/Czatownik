package com.marcinmejner.czatownik.Services

import android.graphics.Color
import android.util.Log
import com.marcinmejner.czatownik.Controller.App
import java.util.*

object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
    }

    fun returnAvatarColor(components: String) : Int {
        Log.d("liczba", components)
        val strippedColor = components
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")


        var liczby = mutableListOf(strippedColor.split(" "))


        var r = (liczby[0][0].toDouble() * 255).toInt()
        var g = (liczby[0][1].toDouble() * 255).toInt()
        var b = (liczby[0][2].toDouble() * 255).toInt()


        Log.d("kolory", "$r + $g + $b ")

        return Color.rgb(r,g,b)
    }


}
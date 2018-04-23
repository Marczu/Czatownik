package com.marcinmejner.czatownik.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager

import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.marcinmejner.czatownik.R
import com.marcinmejner.czatownik.R.id.*
import com.marcinmejner.czatownik.Services.AuthService
import com.marcinmejner.czatownik.Services.UserDataService
import com.marcinmejner.czatownik.Utils.BROADCAST_USER_DATA_CHANGE
import com.marcinmejner.czatownik.Utils.SOCKET_URL
import io.socket.client.IO

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


    }


    override fun onResume() {
        socket.connect()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReciver, IntentFilter(BROADCAST_USER_DATA_CHANGE))
        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReciver)
        super.onPause()
    }

    override fun onDestroy() {
        socket.disconnect()
        super.onDestroy()
    }

    private val userDataChangeReciver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (AuthService.isLoggedIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginBtnNavHeader.text = "Logout"
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked(view: View) {
        if (AuthService.isLoggedIn) {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)

            builder.setView(dialogView)
                    .setPositiveButton("Yes") { dialogInterface, i ->
                        //Logout
                        UserDataService.logout()
                        userNameNavHeader.text = ""
                        userEmailNavHeader.text = ""
                        userImageNavHeader.setImageResource(R.drawable.profiledefault)
                        userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
                        loginBtnNavHeader.text = "Login"

                        val intent = Intent(MainActivity@ this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    .setNegativeButton("No") { dialogInterface, i ->
                    }
                    .show()
        } else {
            //Login
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelClicked(view: View) {
        if (AuthService.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)
                    .setPositiveButton("Add") { dialogInterface, i ->

                        val nameTextEdt = dialogView.findViewById<EditText>(R.id.dialogAddChannelName)
                        val descriptionEdt = dialogView.findViewById<EditText>(R.id.dialogChannelDescription)
                        val channelName = nameTextEdt.text.toString()
                        val channelDesc = descriptionEdt.text.toString()

                        //tworzymy kanal z nazwą i opisem
                        socket.emit("newChannel", channelName, channelDesc)
                    }
                    .setNegativeButton("Cancel") { dialogInterface, i ->
                    }
                    .show()


        }


    }

    fun sendMessageBtnClicked(view: View) {
        hideKeyboard()
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

}

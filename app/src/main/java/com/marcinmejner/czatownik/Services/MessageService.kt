package com.marcinmejner.czatownik.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.marcinmejner.czatownik.Controller.App
import com.marcinmejner.czatownik.Model.Channel
import com.marcinmejner.czatownik.Model.Message
import com.marcinmejner.czatownik.Utils.URL_GET_CHANNELS
import com.marcinmejner.czatownik.Utils.URL_GET_MESSAGES
import org.json.JSONException

object MessageService {
    private val TAG = "MessageService"

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit) {


        val channelRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->

            channels.clear()

            try {
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val channelDesc = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(name, channelDesc, channelId)
                    this.channels.add(newChannel)
                }
                complete(true)

            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener { error ->
            Log.d(TAG, "getChannels: Could not retrive channels")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }
        App.prefs.requestQueue.add(channelRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        clearMessages()
        val url = "$URL_GET_MESSAGES$channelId"

        val messagesRequest = object : JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->

            try {
                for (x in 0 until response.length()) {

                    val message = response.getJSONObject(x)

                    val messageBody = message.getString("messageBody")
                    val channelId = message.getString("channelId")
                    val id = message.getString("_id")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val timeStamp = message.getString("timeStamp")

                    val newMessage = Message(messageBody, userName, channelId, userAvatar, userAvatarColor, id, timeStamp)
                    this.messages.add(newMessage)
                }
                complete(true)

            } catch (e: JSONException) {
                Log.d(TAG, "getMessages: ${e.localizedMessage}")
            }

        }, Response.ErrorListener {
            Log.d(TAG, "getChannels: Could not retrive channels")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }

        }
        App.prefs.requestQueue.add(messagesRequest)
    }

    fun clearMessages() {
        messages.clear()
    }

    fun clearChannels() {
        channels.clear()
    }


}
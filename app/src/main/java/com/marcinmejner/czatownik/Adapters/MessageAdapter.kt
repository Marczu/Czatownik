package com.marcinmejner.czatownik.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.marcinmejner.czatownik.Model.Message
import com.marcinmejner.czatownik.R
import com.marcinmejner.czatownik.Services.UserDataService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(val context: Context, val messages: ArrayList<Message>): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private val TAG = "MessageAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMessage(context, messages[position])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val userImage = itemView?.findViewById<ImageView>(R.id.messageUserIv)
        val timeStamp = itemView?.findViewById<TextView>(R.id.timeStampTv)
        val userName = itemView?.findViewById<TextView>(R.id.messagaUserNameTv)
        val messageBody = itemView?.findViewById<TextView>(R.id.messsageBodyTv)

        fun bindMessage(context: Context, message: Message){
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            userName?.text = message.userName
            timeStamp?.text = returnDateString(message.timeStamp)
            messageBody?.text = message.message
        }

        fun returnDateString(isoString: String) : String{

            val isoFormater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormater.timeZone = TimeZone.getTimeZone("UTC")

            var convertedDate = Date()
            try{
                convertedDate = isoFormater.parse(isoString)
            }catch (e: ParseException){
                Log.d(TAG, "returnDateString: ${e.localizedMessage}")

            }

            val outDateString = SimpleDateFormat("E h:mm a", Locale.getDefault())
            return outDateString.format(convertedDate)
        }

    }
}
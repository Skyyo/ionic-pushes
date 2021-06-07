package com.skyyo.groupingandreplytonotifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.RemoteInput


class DirectReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && "REPLY_ACTION" == intent.action) {
            //what user entered in notification
            val userInput = replyMessage(intent)

            //to get ids etc from the notification
            val messageId = intent.getIntExtra("KEY_MESSAGE_ID", 0)

            //here you want to either store the data to your local storage, or execute network request.
            // also keep in mind that ideally, you want to append the user response to that
            // initial message, and continue doing it. This will require some additional logic
            // to store the currently active notification ids, their texts etc.
            Toast.makeText(context, "$messageId : $userInput", Toast.LENGTH_LONG).show()
        }
    }

    private fun replyMessage(intent: Intent): CharSequence? {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        return remoteInput?.getCharSequence("KEY_TEXT_REPLY")
    }
}
package com.example.quickmessage.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quickmessage.R
import com.quickblox.chat.QBRestChatService
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.chat.model.QBChatMessage
import com.quickblox.chat.request.QBMessageGetBuilder
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException

class ChatScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        var dialogId: String? = ""
        val bundle: Bundle? = intent.extras

        bundle?.let {
            dialogId = bundle.getString("dialog_id")
        }

        Toast.makeText(this, dialogId, Toast.LENGTH_LONG).show()

        getChatDialogByID(dialogId)
    }

    private fun getChatDialogByID(dialogId: String?) {
        QBRestChatService.getChatDialogById(dialogId).performAsync(object :
            QBEntityCallback<QBChatDialog> {
            override fun onSuccess(qbChatDialog: QBChatDialog?, bundle: Bundle?) {
                getChatHistory(qbChatDialog)
            }

            override fun onError(e: QBResponseException?) {
                Toast.makeText(this@ChatScreenActivity, e.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getChatHistory(qbChatDialog: QBChatDialog?) {
        val messageGetBuilder = QBMessageGetBuilder()
        messageGetBuilder.limit = 100

        // If you want to retrieve messages using filtering:
        //messageGetBuilder.gte("date_sent", "508087800")
        //messageGetBuilder.lte("date_sent", "1170720000")
        //messageGetBuilder.markAsRead(false)

        QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(object : QBEntityCallback<ArrayList<QBChatMessage>> {
            override fun onSuccess(qbChatMessages: ArrayList<QBChatMessage>?, bundle: Bundle?) {

            }

            override fun onError(e: QBResponseException?) {
                Toast.makeText(this@ChatScreenActivity, e.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
}
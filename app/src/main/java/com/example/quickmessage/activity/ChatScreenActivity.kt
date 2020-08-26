package com.example.quickmessage.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickmessage.R
import com.example.quickmessage.adapter.ExistingChatDialogsAdapter
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBRestChatService
import com.quickblox.chat.exception.QBChatException
import com.quickblox.chat.listeners.QBChatDialogMessageListener
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.chat.model.QBChatMessage
import com.quickblox.chat.request.QBMessageGetBuilder
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import kotlinx.android.synthetic.main.activity_chat_screen.*
import kotlinx.android.synthetic.main.activity_existing_chat_users.*
import org.jivesoftware.smackx.muc.DiscussionHistory

class ChatScreenActivity : AppCompatActivity() {

    lateinit var chatDialog: QBChatDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        //chat_screen_activity_loader.visibility = View.VISIBLE

        var dialogId: String? = ""
        val bundle: Bundle? = intent.extras

        bundle?.let {
            dialogId = bundle.getString("dialog_id")
        }

        Toast.makeText(this, dialogId, Toast.LENGTH_LONG).show()

        getChatDialogByID(dialogId)

        send_message_button.setOnClickListener {
            sendMessage()
        }
    }

    private fun getChatDialogByID(dialogId: String?) {

        QBRestChatService.getDialogMessagesCount(dialogId)
            .performAsync(object : QBEntityCallback<Int> {
                override fun onSuccess(p0: Int?, p1: Bundle?) {
                    4
                    TODO("Not yet implemented")
                }

                override fun onError(p0: QBResponseException?) {
                    TODO("Not yet implemented")
                }
            }
            )
        QBRestChatService.getChatDialogById(dialogId).performAsync(object :
            QBEntityCallback<QBChatDialog> {
            override fun onSuccess(qbChatDialog: QBChatDialog, bundle: Bundle?) {
                chatDialog = qbChatDialog
                createMessageListener()
                getChatHistory(chatDialog)
            }

            override fun onError(e: QBResponseException?) {
                Toast.makeText(this@ChatScreenActivity, e.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun joinDialog(dialog: QBChatDialog) {
        val history = DiscussionHistory()
        history.maxStanzas = 0
        Toast.makeText(this@ChatScreenActivity, dialog.isJoined.toString(), Toast.LENGTH_LONG)
            .show()
        dialog.join(history)
        dialog.join(history, object : QBEntityCallback<Void> {
            override fun onSuccess(o: Void?, bundle: Bundle?) {
                Toast.makeText(this@ChatScreenActivity, "dialog joined", Toast.LENGTH_LONG).show()

                chat_screen_activity_loader.visibility = View.GONE
                getChatHistory(dialog)
            }

            override fun onError(e: QBResponseException?) {
                Toast.makeText(this@ChatScreenActivity, "dialog not joined", Toast.LENGTH_LONG)
                    .show()
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

        QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder)
            .performAsync(object : QBEntityCallback<ArrayList<QBChatMessage>> {
                override fun onSuccess(qbChatMessages: ArrayList<QBChatMessage>?, bundle: Bundle?) {

                    //TODO()
                    //fill the recycler view
                    5
                    //createMessageListener()
                }

                override fun onError(e: QBResponseException?) {
                    Toast.makeText(this@ChatScreenActivity, e.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setupRecyclerView(result: ArrayList<QBChatDialog>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        chat_dialogs_recycler_view.layoutManager = layoutManager

        val adapter = ExistingChatDialogsAdapter(this, result)

        chat_dialogs_recycler_view.adapter = adapter
        existing_chat_users_activity_loader.visibility = View.GONE
    }

    private fun sendMessage() {
        val chatMessage = QBChatMessage()
        chatMessage.body = message_edit_text.text.toString()

        chatDialog.sendMessage(chatMessage, object : QBEntityCallback<Void> {
            override fun onSuccess(aVoid: Void?, bundle: Bundle?) {
                Toast.makeText(this@ChatScreenActivity, "message sent", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: QBResponseException?) {

            }
        })
    }

    private fun createMessageListener() {
        val chatService = QBChatService.getInstance()
        val incomingMessagesManager = chatService.incomingMessagesManager

        incomingMessagesManager.addDialogMessageListener(object : QBChatDialogMessageListener {
            override fun processMessage(
                dialogID: String?,
                qbChatMessage: QBChatMessage?,
                senderID: Int?
            ) {
                if (dialogID == chatDialog.dialogId.toString()) {
                    //TODO()
                    // insert into the recycler view
                }
                Toast.makeText(
                    this@ChatScreenActivity,
                    "Message received: " + qbChatMessage?.body.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun processError(
                dialogID: String?,
                e: QBChatException?,
                qbChatMessage: QBChatMessage?,
                senderID: Int?
            ) {
                Toast.makeText(this@ChatScreenActivity, e.toString(), Toast.LENGTH_LONG).show()
            }
        })
        //joinDialog(chatDialog)
    }


}

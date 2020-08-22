package com.example.quickmessage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.quickblox.auth.session.QBSettings
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBRestChatService
import com.quickblox.chat.exception.QBChatException
import com.quickblox.chat.listeners.QBChatDialogMessageListener
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.chat.model.QBChatMessage
import com.quickblox.chat.model.QBDialogType
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.activity_main.*
import org.jivesoftware.smackx.muc.DiscussionHistory

class MainActivity : AppCompatActivity() {

    private val APPLICATION_ID = "85670"
    private val AUTH_KEY = "2jmhzjJdNO7EpWZ"
    private val AUTH_SECRET = "pB394z2ADvPRZAj"
    private val ACCOUNT_KEY = "uyCB3CQYrSPUsrq_NR9o"

    private lateinit var chatDialog: QBChatDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QBSettings.getInstance().init(applicationContext, APPLICATION_ID, AUTH_KEY, AUTH_SECRET)
        QBSettings.getInstance().accountKey = ACCOUNT_KEY

        signIn()

        send_button.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val chatMessage = QBChatMessage()
        chatMessage.body = message_text.text.toString()

        chatDialog.sendMessage(chatMessage, object : QBEntityCallback<Void> {
            override fun onSuccess(aVoid: Void?, bundle: Bundle?) {
                Toast.makeText(this@MainActivity, "message sent", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: QBResponseException?) {

            }
        })
    }

    private fun signIn() {
        val userDetails = QBUser()
        userDetails.login = "MyLogin"
        userDetails.password = "mypassword"

        // code to sign up the user

//        QBUsers.signUp(userDetails).performAsync(object : QBEntityCallback<QBUser> {
//            override fun onSuccess(p0: QBUser?, p1: Bundle?) {
//                QBUsers.signIn(userDetails).performAsync(object : QBEntityCallback<QBUser> {
//                    override fun onSuccess(user: QBUser?, args: Bundle?) {
//                        userDetails.id = user?.id
//                        login(user)
//                    }
//
//                    override fun onError(error: QBResponseException?) {
//                        Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
//                    }
//                })
//            }
//
//            override fun onError(p0: QBResponseException?) {
//                Toast.makeText(this@MainActivity, p0.toString(), Toast.LENGTH_LONG).show()
//            }
//
//        })

        QBUsers.signIn(userDetails).performAsync(object : QBEntityCallback<QBUser> {
            override fun onSuccess(user: QBUser?, args: Bundle?) {
                userDetails.id = user?.id
                login(userDetails)
            }

            override fun onError(error: QBResponseException?) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
            }
        })


    }

    private fun login(qbUser: QBUser?) {
        QBChatService.getInstance().login(qbUser, object : QBEntityCallback<Void> {
            override fun onSuccess(o: Void?, bundle: Bundle?) {
                Toast.makeText(this@MainActivity, "login success", Toast.LENGTH_LONG).show()
                createChatDialogForChat()
            }

            override fun onError(e: QBResponseException?) {
                Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createChatDialogForChat() {
        val occupantIdsList = ArrayList<Int>()
        occupantIdsList.add(121513191) //id of mylogin2
        //occupantIdsList.add(121512899) //id of mylogin

        val dialog = QBChatDialog()
        dialog.type = QBDialogType.PRIVATE
        dialog.setOccupantsIds(occupantIdsList)

        // or just use DialogUtils
        //QBChatDialog dialog = DialogUtils.buildPrivateDialog(recipientId);

        QBRestChatService.createChatDialog(dialog).performAsync(object : QBEntityCallback<QBChatDialog> {
            override fun onSuccess(result: QBChatDialog, params: Bundle?) {
                Toast.makeText(this@MainActivity, "chat dialog created", Toast.LENGTH_LONG).show()
                chatDialog = result
                createMessageListener()
                joinDialog(result)
            }

            override fun onError(responseException: QBResponseException?) {
                Toast.makeText(this@MainActivity, responseException.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun joinDialog(dialog: QBChatDialog) {
        val history = DiscussionHistory()
        history.maxStanzas = 0
        dialog.join(history, object : QBEntityCallback<Void> {
            override fun onSuccess(o: Void?, bundle: Bundle?) {
                Toast.makeText(this@MainActivity, "dialog joined", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: QBResponseException?) {
                Toast.makeText(this@MainActivity, "dialog not joined", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createMessageListener() {
        val chatService = QBChatService.getInstance()
        val incomingMessagesManager = chatService.incomingMessagesManager

        incomingMessagesManager.addDialogMessageListener(object : QBChatDialogMessageListener {
            override fun processMessage(dialogID: String?, qbChatMessage: QBChatMessage?, senderID: Int?) {
                Toast.makeText(this@MainActivity, "Message received: " + qbChatMessage?.body.toString(), Toast.LENGTH_LONG).show()
                //Toast.makeText(this@MainActivity, qbChatMessage.toString(), Toast.LENGTH_LONG).show()
            }

            override fun processError(dialogID: String?, e: QBChatException?, qbChatMessage: QBChatMessage?, senderID: Int?) {
                Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

}
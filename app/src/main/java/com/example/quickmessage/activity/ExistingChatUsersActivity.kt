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
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.request.QBRequestGetBuilder
import kotlinx.android.synthetic.main.activity_existing_chat_users.*
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection

class ExistingChatUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_chat_users)

        existing_chat_users_activity_loader.visibility = View.VISIBLE

        val connectionListener = object : ConnectionListener {
            override fun connected(xmppConnection: XMPPConnection) {
                Toast.makeText(this@ExistingChatUsersActivity, "Connected", Toast.LENGTH_LONG)
                    .show()
            }

            override fun authenticated(xmppConnection: XMPPConnection, b: Boolean) {

            }

            override fun connectionClosed() {

            }

            override fun connectionClosedOnError(e: Exception) {

            }

            override fun reconnectionSuccessful() {

            }

            override fun reconnectingIn(i: Int) {

            }

            override fun reconnectionFailed(e: Exception) {

            }
        }

        QBChatService.getInstance().addConnectionListener(connectionListener)

        getChatDialogsList()
    }

    private fun getChatDialogsList() {
        val requestBuilder = QBRequestGetBuilder()
        requestBuilder.limit = 50
//      requestBuilder.setSkip(100);
//      requestBuilder.sortAsc("last_message_date_sent");

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(object :
            QBEntityCallback<ArrayList<QBChatDialog>> {
            override fun onSuccess(result: ArrayList<QBChatDialog>, params: Bundle?) {
                setupRecyclerView(result)
            }

            override fun onError(responseException: QBResponseException?) {
                existing_chat_users_activity_loader.visibility = View.GONE
                Toast.makeText(this@ExistingChatUsersActivity, responseException.toString(), Toast.LENGTH_LONG).show()
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
}
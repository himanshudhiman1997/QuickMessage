package com.example.quickmessage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBRestChatService
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.request.QBRequestGetBuilder
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection

class ExistingChatUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_chat_users)

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
            override fun onSuccess(result: ArrayList<QBChatDialog>?, params: Bundle?) {
                val d = 7
            }

            override fun onError(responseException: QBResponseException?) {

            }
        })
    }
}
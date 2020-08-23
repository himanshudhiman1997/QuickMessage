package com.example.quickmessage.listener

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.quickblox.chat.QBChatService
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException

class BackgroundListener : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onBackground() {
        QBChatService.getInstance().logout(object : QBEntityCallback<Void> {
            override fun onSuccess(v: Void?, b: Bundle?) {
                QBChatService.getInstance().destroy()
            }

            override fun onError(e: QBResponseException?) {

            }
        })
    }
}
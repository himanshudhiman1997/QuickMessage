package com.example.quickmessage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmessage.R
import com.example.quickmessage.activity.ChatScreenActivity
import com.quickblox.chat.model.QBChatDialog
import kotlinx.android.synthetic.main.chat_dialog_list_item.view.*

class ExistingChatDialogsAdapter(
    val context: Context,
    private val chatDialogsArray: ArrayList<QBChatDialog>
) : RecyclerView.Adapter<ExistingChatDialogsAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userDetails: QBChatDialog? = null
        var currentPosition: Int = 0

        init {
            itemView.chat_dialog_item_card_view.setOnClickListener {
                val intent = Intent(context, ChatScreenActivity::class.java)
                intent.putExtra("dialog_id", userDetails!!.dialogId)
                context.startActivity(intent)
            }
        }

        fun setData(chatDialogItem: QBChatDialog, position: Int)
        {
            chatDialogItem.let {
                itemView.chat_dialog_item_text_view.text = chatDialogItem.name.toString()
            }
            this.userDetails = chatDialogItem
            this.currentPosition = position

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExistingChatDialogsAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.chat_dialog_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExistingChatDialogsAdapter.MyViewHolder, position: Int) {
        val chatDialogItem = chatDialogsArray[position]
        holder.setData(chatDialogItem, position)
    }

    override fun getItemCount(): Int {
        return chatDialogsArray.size
    }


}
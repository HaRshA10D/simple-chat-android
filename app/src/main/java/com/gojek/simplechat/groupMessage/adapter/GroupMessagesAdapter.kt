package com.gojek.simplechat.groupMessage.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gojek.simplechat.R
import com.gojek.simplechat.groupMessage.model.GroupMessage

class GroupMessagesAdapter(private val groupMessages: List<GroupMessage>)
    : RecyclerView.Adapter<GroupMessagesAdapter.GroupMessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMessagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_message_card, parent, false)
        return GroupMessagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return groupMessages.size
    }

    override fun onBindViewHolder(holder: GroupMessagesViewHolder, position: Int) {
        val message = groupMessages[position]
        holder.username.text = message.userName
        holder.messageText.text = message.messageText
        holder.messageTimestamp.text = message.epochToDateTime()
    }

    class GroupMessagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.group_message_username)
        val messageText: TextView = view.findViewById(R.id.group_message_text_message)
        val messageTimestamp: TextView = view.findViewById(R.id.group_message_timestamp)
    }
}

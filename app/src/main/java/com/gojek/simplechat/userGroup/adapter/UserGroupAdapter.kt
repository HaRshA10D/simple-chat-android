package com.gojek.simplechat.userGroup.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gojek.simplechat.R
import com.gojek.simplechat.userGroup.model.UserGroup

class UserGroupAdapter(
        private val userGroupList: List<UserGroup>,
        private val onGroupClickListener: (String, String) -> Unit)
    : RecyclerView.Adapter<UserGroupAdapter.UserGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserGroupViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_group_card, parent, false)
        return UserGroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userGroupList.size
    }

    override fun onBindViewHolder(groupListViewHolder: UserGroupViewHolder, position: Int) {
        groupListViewHolder.bindItems(userGroupList[position])
        groupListViewHolder.itemView.setOnClickListener {
            val groupId = userGroupList[position].id
            val groupName = userGroupList[position].name
            onGroupClickListener(groupId, groupName)
        }
    }

    class UserGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var groupTitle: TextView

        fun bindItems(userGroup: UserGroup) {
            groupTitle = itemView.findViewById(R.id.tvGroup)
            groupTitle.text = userGroup.name
        }
    }
}

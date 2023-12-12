package com.example.firebasep

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasep.DataModels.UserModel

class UserAdapter(private val userList: List<UserModel>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserId: TextView = view.findViewById(R.id.tvUserId)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvUserEmail: TextView = view.findViewById(R.id.tvUserEmail)
        val tvUserPassword: TextView = view.findViewById(R.id.tvUserPassword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.tvUserId.text = currentUser.uId
        holder.tvUserName.text = currentUser.uName
        holder.tvUserEmail.text = currentUser.uEmail
        holder.tvUserPassword.text = currentUser.uPass
        Log.e("reach to adapter ", "Addapter ")
        Log.e("UserAdapter", "UserID: ${currentUser.uId}, UserName: ${currentUser.uName}")
    }

    override fun getItemCount() = userList.size
}

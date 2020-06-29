package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.StorageReference
import com.utalli.R
import com.utalli.helpers.DateUtils
import com.utalli.helpers.ImageUtils
import com.utalli.models.chat.ChatMsg
import java.util.ArrayList

class ChatAdapterrr (var context : Context, var msgs : ArrayList<ChatMsg>, var userName : String): RecyclerView.Adapter<ChatAdapterrr.ChatViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }



    override fun getItemCount(): Int {
       return msgs.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        var msg = msgs.get(position)

        if (msg.isRead){
            holder.imgFiendReadTick.setImageResource(R.mipmap.ic_tick_green)
        }else{
            holder.imgFiendReadTick.setImageResource(R.mipmap.ic_tick_white)
        }



        if (msg.fromID.equals(userName)){
            holder.lytMsgFriend.setVisibility(View.GONE)
            holder.imgFrnd.setVisibility(View.GONE)
            holder.imgUser.setVisibility(View.GONE)
            holder.txtUserMsg.setText(msg.content)
            holder.txtUserMsgTime.setText(DateUtils.getTime(msg.timestamp))
            if (msg.type.equals("text")){
                holder.lytMsgUser.setVisibility(View.VISIBLE)
                holder.imgUser.setVisibility(View.GONE)
            }else{
                holder.lytMsgUser.setVisibility(View.GONE)
                holder.imgUser.setVisibility(View.VISIBLE)
                ImageUtils.displayImageFromUrl(holder.itemView.context, holder.imgUser, msg.content)
            }


        } else{
            holder.lytMsgUser.setVisibility(View.GONE)
            holder.imgFrnd.setVisibility(View.GONE)
            holder.imgUser.setVisibility(View.GONE)
            holder.txtFriendMsg.setText(msg.content)
            holder.txtFriendMsgTime.setText(DateUtils.getTime(msg.timestamp))
            if (msg.type.equals("text", ignoreCase = true)) {
                holder.lytMsgFriend.setVisibility(View.VISIBLE)
                holder.imgFrnd.setVisibility(View.GONE)
            } else {
                holder.lytMsgFriend.setVisibility(View.GONE)
                holder.imgFrnd.setVisibility(View.VISIBLE)
                ImageUtils.displayImageFromUrl(holder.itemView.context, holder.imgFrnd, msg.content)
            }
        }



    }


    inner class ChatViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView){
        var lytMsgUser: LinearLayout
        var txtUserMsg : TextView
        var txtUserMsgTime : TextView
        var txtFriendMsg : TextView
        var txtFriendMsgTime : TextView
        var lytMsgFriend : LinearLayout
        var imgFrnd : ImageView
        var imgUser : ImageView
        var imgFiendReadTick : ImageView

        init {
            lytMsgUser = itemView.findViewById(R.id.lytMsgUser)
            txtUserMsg = itemView.findViewById(R.id.txtUserMsg)
            txtUserMsgTime = itemView.findViewById(R.id.txtUserMsgTime)
            txtFriendMsg = itemView.findViewById(R.id.txtFriendMsg)
            txtFriendMsgTime = itemView.findViewById(R.id.txtFriendMsgTime)
            lytMsgFriend = itemView.findViewById(R.id.lytMsgFriend)
            imgFrnd = itemView.findViewById(R.id.imgFrnd)
            imgUser = itemView.findViewById(R.id.imgUser)
            imgFiendReadTick = itemView.findViewById(R.id.imgFiendReadTick)
        }

    }



}
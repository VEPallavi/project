package com.utalli.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.utalli.R
import com.utalli.helpers.ImageUtils
import com.utalli.helpers.Utils.Companion.formatToYesterdayOrToday
import com.utalli.models.chat.ChatUser
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class ChatMessageAdapter(var mcontext: Context, var usersList: ArrayList<ChatUser>, var observer: UserObserver) :
    RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder>() {

    interface UserObserver {
        fun onUser(user: ChatUser)
        fun onDelete(user: ChatUser)
        fun onArchive(user: ChatUser)
    }


    fun updateUsersList(users: ArrayList<ChatUser>) {

        usersList=ArrayList<ChatUser>()
        usersList.addAll(users)
        notifyDataSetChanged()


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageAdapter.MessageViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_chat_message, parent, false)
        return MessageViewHolder(view)
    }


    override fun getItemCount(): Int {
        return usersList.size
    }


    override fun onBindViewHolder(holder: ChatMessageAdapter.MessageViewHolder, position: Int) {
//            holder.tvUser_name.setText(msgg.get(position))
//            holder.tvMsgDay.setText(formatToYesterdayOrToday(dateTime.get(position)))
//            if (position > 0) {
//                if (dateTime.get(position).equals(dateTime.get(position - 1))) {
//                    holder.tvMsgDay.setVisibility(View.GONE);
//                } else {
//                    holder.tvMsgDay.setVisibility(View.VISIBLE);
//                }
//            } else {
//                holder.tvMsgDay.setVisibility(View.VISIBLE);
//            }

        var user = usersList.get(position)
        holder.txtName.setText(user.name)
        holder.txtStatus.setText(user.lastMsg)
        holder.txtMsgTime.setText(user.lastUpdateTime)
        holder.txtMsgCount.setText(user.notificationCount)

        if (user.notificationCount.equals("0")) {
            holder.txtMsgCount.visibility = View.GONE
        } else {
            holder.txtMsgCount.visibility = View.VISIBLE
        }


        if (user.msgReaded) {
            holder.imgReadTick.setImageResource(R.drawable.ic_tick_green)
        } else {
            holder.imgReadTick.setImageResource(R.drawable.ic_chat_read)
        }

        ImageUtils.displayImageFromUrl(holder.itemView.context, holder.imgProfile, user.profileUrl)


        holder.lytParent.setOnClickListener {
            observer.onUser(user)
            /*  var intent = Intent(mcontext, ChatActivity::class.java)
               intent.putExtra("userName",msgg.get(position))
               mcontext.startActivity(intent)*/
        }


    }


    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lytParent: ConstraintLayout
        var txtName: TextView
        var txtStatus: TextView
        var txtMsgTime: TextView
        var txtMsgCount: TextView
        var imgProfile: CircleImageView
        var imgReadTick: ImageView


        init {
            txtName = itemView.findViewById(R.id.tv_user_name)
            txtStatus = itemView.findViewById<TextView>(R.id.tv_message_descp)
            txtMsgTime = itemView.findViewById<TextView>(R.id.iv_message_time)
            txtMsgCount = itemView.findViewById<TextView>(R.id.txtMsgCount)
            imgProfile = itemView.findViewById<CircleImageView>(R.id.profile_image)
            lytParent = itemView.findViewById<ConstraintLayout>(R.id.cl_main)
            imgReadTick = itemView.findViewById<ImageView>(R.id.imgReadTick)

        }

    }

}

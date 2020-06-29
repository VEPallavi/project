package com.utalli.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.client.Firebase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.utalli.R
import com.utalli.activity.ChatActivity
import com.utalli.adapter.ChatMessageAdapter
/*import com.utalli.activity.ChatActivity*/
/*import com.utalli.adapter.ChatMessageAdapter*/
import com.utalli.helpers.AppPreference
import kotlinx.android.synthetic.main.fragment_chat_message.*
import com.utalli.models.UserModel
import com.utalli.models.chat.ChatUser


class ChatMessageFragment : Fragment(), ChatMessageAdapter.UserObserver,  ChatUser.ChangeObserver,View.OnClickListener{



    var adapter: ChatMessageAdapter? = null
    var name = ArrayList<String>()
    var user: UserModel? = null
    var mContext : Activity ?= null
    //  var userId : String = ""

    private val users = ArrayList<ChatUser>()
    private val queryUsers = ArrayList<ChatUser>()
    private var userId: String = ""
    private var refOnlineStaus: Firebase? = null
    private var imgNotification: ImageView? = null
    private var deviceWidth: Int = 0
    private var cl_onaddbutton: ConstraintLayout? = null
    var preference : AppPreference ?= null
    var recyclerView_chatMessage : RecyclerView?= null
    var pd : ProgressDialog?= null


    override fun onAttach(context: Activity) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat_message, container, false)

        preference = AppPreference.getInstance(activity!!)
        userId = preference!!.getuserIdFirebase()


        recyclerView_chatMessage =  view.findViewById<RecyclerView>(R.id.recyclerView_chatMessage)
        recyclerView_chatMessage!!.layoutManager = LinearLayoutManager(mContext)

        Firebase.setAndroidContext(activity!!)
        FirebaseApp.initializeApp(activity!!)

        val baseUrl = "https://utalii-fda70.firebaseio.com/" //FirebaseDatabase.getInstance().reference.toString()

        refOnlineStaus = Firebase(baseUrl+"/users/"+userId+"/credentials")

        pd = ProgressDialog(activity);
        pd!!.setMessage("loading");
        pd!!.show()

        init()
        getUsers()
        registerReceiver()

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        et_location.text = AppPreference.getInstance(activity!!).getUserLastLocation()
    }


     override fun onDestroy() {
        refOnlineStaus!!.child("isOnline").setValue(false)
        super.onDestroy()
    }


    fun getUsers() {
        val rootRef = FirebaseDatabase.getInstance().reference
        val usersdRef = rootRef.child("users").child(userId)
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var mapData = dataSnapshot.getValue() as Map<*, *>?
                var mapConversion = (mapData!! as Map<out String, Any?>).get("conversations") as Map<*, *>?


                    //show
                    if(mapConversion == null){

                        if(!(mContext!!.isFinishing())) {
                            Snackbar.make(recyclerView_chatMessage!!, resources.getString(R.string.no_chat_till_now), Snackbar.LENGTH_SHORT).show()
                        }
                        cl_no_conversation_found.visibility = View.VISIBLE
                        return
                    }




                cl_no_conversation_found?.visibility = View.GONE


               //var users=ArrayList<ChatUser>();

                for (id in mapConversion!!.keys) {
                    try {
                        val idStr = id.toString()
                        val jsonConversion = mapConversion[idStr] as Map<*, *>?

                        val location = jsonConversion?.get("location").toString()
                        val unreadCount = jsonConversion?.get("unreadCount").toString()
                        val friendName = jsonConversion?.get("friendName").toString()

//                        if (idStr.startsWith("-")) {
//                            // Add Group Chat
//                            val groupImage = jsonConversion["groupImage"]!!.toString()
//                            val groupName = jsonConversion["groupName"]!!.toString()
//                            val user = ChatUser(idStr, groupName, "", groupImage, "", unreadCount + "")
//                            user.conversationLocation = location
//                            val firebase = Firebase("https://utalii-fda70.firebaseio.com/users/" + userId+"/conversations/" +idStr+ "/unreadCount")
//                            user.unReadCountListener(firebase, this@ChatMessageFragment)
//                            users.add(user)
//                        }
                      //  else {
                            //  Add user to user chat
                            //                            getUserCredentials(idStr,location,unreadCount);
                            val user = ChatUser()
                            user.id = idStr
                            user.name = friendName
                            user.notificationCount = unreadCount
                            user.conversationLocation = location
                            val firebaseCount = Firebase("https://utalii-fda70.firebaseio.com/users/" + userId+"/conversations/" +idStr+ "/unreadCount")
                            user.unReadCountListener(firebaseCount, this@ChatMessageFragment)
                            val firebase = Firebase("https://utalii-fda70.firebaseio.com/users/" + user.id + "/credentials")
                            user.setRefCredentials(firebase)
                            users.add(user)
                            //                            getLastMsg(user);
                     //   }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

               // adapter!!.updateUsersList(users)
              //  adapter!!.notifyDataSetChanged()

                adapter = ChatMessageAdapter(mContext!!,users, this@ChatMessageFragment)
                recyclerView_chatMessage!!.setLayoutManager(LinearLayoutManager(mContext!!))
                recyclerView_chatMessage!!.setAdapter(adapter)
                pd!!.dismiss()

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        usersdRef.addListenerForSingleValueEvent(eventListener)
    }



    private fun refreshMsgNdTime() {
        Thread(Runnable {
            for (user in users) {
                getLastMsg(user)
            }
        }).start()
    }

    private fun getLastMsg(user: ChatUser) {
        FirebaseApp.initializeApp(activity!!)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val query = databaseReference.child("conversations").child(user.conversationLocation).limitToLast(1)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: com.google.firebase.database.DataSnapshot) {
                val map = dataSnapshot.value as Map<*, *>?
                if (map != null)
                    for (key in map.keys) {
                        try {
                            val mapMsg = map.get(key) as Map<*, *>?
                            var message = mapMsg!!.get("content").toString()
                            val createdOn = mapMsg.get("timestamp").toString()
                            val isMsgReaded = mapMsg.get("isRead").toString()
                            val type = mapMsg.get("type").toString()
                            if (type.equals("photo", ignoreCase = true))
                                message = "Image"
                            user.lastMsg = message
                            user.isMsgReaded = java.lang.Boolean.valueOf(isMsgReaded)
                            user.lastUpdateTime = createdOn
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                else
                    user.lastMsg = ""
                adapter!!.notifyDataSetChanged()
                pd!!.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun init() {

        adapter = ChatMessageAdapter(activity!!,users, this@ChatMessageFragment)
        recyclerView_chatMessage!!.setLayoutManager(LinearLayoutManager(activity!!))
        recyclerView_chatMessage!!.setAdapter(adapter)

    }



    private fun deleteChat(user: ChatUser) {
        val baseUrl = FirebaseDatabase.getInstance().reference.toString()
        val userRef = Firebase(baseUrl+"/chats/"+userId)
        userRef.child(user.getId()).removeValue()
        users.remove(user)
        adapter!!.notifyDataSetChanged()
    }



    override fun onUser(user: ChatUser) {
        val intent = Intent(activity!!, ChatActivity::class.java)
        intent.putExtra("friendId", user!!.getId())
        intent.putExtra("friendName", user!!.name)
        intent.putExtra("friendLocation", user!!.getConversationLocation())
        intent.putExtra("friendProfilePic", user!!.getProfileUrl())
        intent.putExtra("friendLastSeen", user!!.getLastSeen())
        startActivity(intent)
    }

    override fun onDelete(user: ChatUser) {
        deleteChat(user!!)
    }

    override fun onArchive(user: ChatUser) {
        //makeArchiveUser(user!!)
    }

    override fun onClick(v: View?) {

    }


    private fun makeArchiveUser(user: ChatUser) {

        val baseUrl = FirebaseDatabase.getInstance().reference.toString()
        val userRef = Firebase(baseUrl + "/users/" + userId + "/conversations/" + user.getId())
        userRef.child("isArchive").setValue("true")
        users.remove(user)
        adapter!!.notifyDataSetChanged()
    }

    private fun toggleArchiveLyt() {

    }

    override fun onUserChange() {
        adapter!!.notifyDataSetChanged()
    }




    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction("LOCATION_UPDATED")
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(mReciever, filter)
    }


    var mReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (et_location != null)
                et_location!!.text = AppPreference.getInstance(activity!!).getUserLastLocation()
        }
    }


    override fun onResume() {
        super.onResume()

        user = AppPreference.getInstance(activity!!).getUserData() as UserModel
        if (user != null) {

            Glide.with(this)
                .load(user!!.profile_img)
                .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
                .into(profile_Pic)
        }


        if (users.size > 0){
            refreshMsgNdTime()
        }
        refOnlineStaus!!.child("isOnline").setValue(true)

    }


    override fun onStop() {
        super.onStop()
        refOnlineStaus!!.child("isOnline").setValue(false)
    }


 /*   override fun onDetach() {
        super.onDetach()
        mContext = null;
    }*/
}
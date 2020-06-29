package com.utalli.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.client.ChildEventListener
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.utalli.R
import com.utalli.adapter.ChatAdapterrr
import com.utalli.helpers.AppPreference
import com.utalli.helpers.DateUtils
import com.utalli.helpers.ImageUtils
import com.utalli.helpers.Utils
import com.utalli.models.chat.ChatMsg
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_guide_pools_list.*
import kotlinx.android.synthetic.main.item_chat.*
import kotlinx.android.synthetic.main.layout_chat_action_bar.*
import java.io.File
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap

class ChatActivity : AppCompatActivity(), View.OnClickListener{


    private val REQUEST_CODE_CAMERA = 201
    private var reference: Firebase? = null
    private var refMyUnread:Firebase? = null
    private var refFrndUnread:Firebase? = null
    private var refTyping:Firebase? = null
    private var refFrndCre : Firebase ?= null
    private var storageReference: StorageReference? = null
    private var userId: String =""
    private var recyclerView: RecyclerView? = null
    private var adapter: ChatAdapterrr? = null
    private val msgs = ArrayList<ChatMsg>()
    private var isAddChild = false
    private var isSend = false
    private var frndUnReadCount = 0
    private var friendId: String = ""
    private var friendName:String= ""
    private var friendLocation:String? = null
    private var friendProfilePic:String? = null
    private var friendLastSeen:String? = null
    private var saveLocation: String? = null
    private var deviceWidth: Int = 0
    private var photoFile: File? = null
    private var photoURI: Uri? = null
    var preference: AppPreference? = null
    var imgSend : ImageView ?= null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        preference = AppPreference.getInstance(this)

        Firebase.setAndroidContext(this)
        init()
        initFirebase()

       // if (!friendId!!.startsWith("-")){
            initFrndUnreadListener()
       // }

      //  getLastMsg()


    }


    private fun init() {
        setSupportActionBar(findViewById<View>(R.id.action_bar) as Toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

       // userId = "userId_" +preference!!.getId().toString() //AppPreferencesHelper(this).getUserIdFirebase()
        userId = preference!!.getuserIdFirebase()


            friendId = intent.extras!!.getString("friendId")
            friendName = intent.extras!!.getString("friendName")
            friendLocation = intent.extras!!.getString("friendLocation")
            friendProfilePic = intent.extras!!.getString("friendProfilePic")
            friendLastSeen = intent.extras!!.getString("friendLastSeen")


        adapter = ChatAdapterrr(this@ChatActivity,msgs, userId)
      //  var manager = LinearLayoutManager(this)

        var imgFrnd = findViewById<CircleImageView>(R.id.imgUserProfile)
        var txtTitle = findViewById<TextView>(R.id.txtTitle)
        var txtSubtitle = findViewById<TextView>(R.id.txtSubtitle)
        imgSend = findViewById<ImageView>(R.id.imgSend)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        imgSend!!.setOnClickListener(this)

        txtTitle.text = friendName
        ImageUtils.displayImageFromUrl(this, imgFrnd, friendProfilePic)


        recyclerView!!.layoutManager = LinearLayoutManager(this)
      //  recyclerView!!.setLayoutManager(manager)
      //  recyclerView!!.setItemAnimator(DefaultItemAnimator())
        recyclerView!!.setAdapter(adapter)


        edtMsg!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isSend) {
                    refTyping!!.child("isTyping").setValue(true)
                    isSend = true
                    Handler().postDelayed(
                        {
                            isSend = false
                        }, (5 * 1000).toLong())
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    //https://utalii-fda70.firebaseio.com

    private fun getLastMsg() {
        val baseUrl = "https://utalii-fda70.firebaseio.com/"//FirebaseDatabase.getInstance().reference.toString()
        refMyUnread = Firebase(baseUrl + "/users/" + userId+"/conversations/"+friendId) //Firebase("$baseUrl/users/$userId/conversations/$friendId")
        val databaseReference = FirebaseDatabase.getInstance().reference
        val query = databaseReference.child("conversations").child(friendLocation!!).orderByChild("timestamp").limitToLast(30)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: com.google.firebase.database.DataSnapshot) {
                try {
                    isAddChild = true
                    val mapConversion = dataSnapshot.value as HashMap<String, Any>?
                    for (key in mapConversion!!.keys) {
                        val map = mapConversion.get(key) as Map<String, Any>?
                        val msg = ChatMsg()
                        msg.content = map!!.get("content").toString()
                        msg.fromID = map!!.get("fromID").toString()
                        msg.toID = map.get("toID").toString()
                        msg.setIsRead(map.get("isRead").toString())
                        msg.type = map.get("type").toString()
                        msg.timestamp = map.get("timestamp").toString()
                        msgs.add(msg)
                    }
                    adapter!!.notifyDataSetChanged()
                    refMyUnread!!.child("unreadCount").setValue("0")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


  private fun initFirebase() {
        val baseUrl = "https://utalii-fda70.firebaseio.com/"//FirebaseDatabase.getInstance().reference.toString()
        storageReference = FirebaseStorage.getInstance().getReference()
        refTyping = Firebase("$baseUrl/users/$userId/credentials")
        reference = Firebase("$baseUrl/conversations/$friendLocation")
        refMyUnread = Firebase(baseUrl + "/users/" + userId+"/conversations/"+friendId)

        reference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
              //  if (isAddChild)
                    if (s != null) {
                        try {
                            val map = dataSnapshot.getValue(Map::class.java)
                            val msg = ChatMsg()
                            msg.content = map.get("content").toString()
                            msg.fromID = map.get("fromID").toString()
                            msg.toID = map.get("toID").toString()
                            msg.setIsRead(map.get("isRead").toString())
                            msg.type = map.get("type").toString()
                            msg.timestamp = map.get("timestamp").toString()
                            if (userId.equals(msg.toID, ignoreCase = true)) {
                                reference!!.child(s).child("isRead").setValue("true")
                                msg.setIsRead("true")
                            }
                            msgs.add(msg)
                            recyclerView!!.smoothScrollToPosition(adapter!!.getItemCount())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        adapter!!.notifyDataSetChanged()
                        recyclerView!!.smoothScrollToPosition(adapter!!.getItemCount())
                        refMyUnread!!.child("unreadCount").setValue("0")
                    }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(firebaseError: FirebaseError) {

            }
        })



      refFrndCre = Firebase(baseUrl + "/users/" + friendId + "/credentials")

      refFrndCre!!.addChildEventListener(object : ChildEventListener{

          override fun onCancelled(firebaseError: FirebaseError?) {

          }

          override fun onChildMoved(dataSnapshot: DataSnapshot?, s: String?) {

          }

          override fun onChildChanged(dataSnapshot: DataSnapshot?, s: String?) {
              when (dataSnapshot!!.getKey()) {
                  "name" -> {
                      friendName = dataSnapshot.getValue().toString()
                      txtTitle.setText(friendName)
                  }
                  "lastSeen" -> {
                      friendLastSeen = dataSnapshot.getValue().toString()
                      txtSubtitle.setText(DateUtils.getTimeDate(friendLastSeen))
                  }
                  "isTyping" -> {
                      val isTyping = java.lang.Boolean.parseBoolean(dataSnapshot.getValue().toString())
                      if (isTyping)
                          txtSubtitle.setText(friendName + " typing...")
                  }
                  "profilePicLink" -> {
                      friendProfilePic = dataSnapshot.getValue().toString()
                      ImageUtils.displayImageFromUrl(getBaseContext(), imgFrnd, friendProfilePic)
                  }
                  "isOnline" -> {
                      val isOnline = java.lang.Boolean.parseBoolean(dataSnapshot.getValue().toString())
                      if (isOnline)
                          txtSubtitle.setText("online")
                  }
              }
          }

          override fun onChildAdded(dataSnapshot: DataSnapshot?, s: String?) {

          }

          override fun onChildRemoved(dataSnapshot: DataSnapshot?) {

          }
      })

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgSend ->{
                sendMsg()
            }
        }
    }




    private fun sendMsg() {
        refTyping!!.child("isTyping").setValue(false)
        val messageText = edtMsg.text.toString()
        if (messageText != "") {
            val map = HashMap<String, String>()
            map["timestamp"] = System.currentTimeMillis().toString()
            map["isRead"] = false.toString()
            map["content"] = messageText
            map["toID"] = friendId
            map["fromID"] = userId
            map["type"] = "text"

            edtMsg.setText("")
            reference!!.push().setValue(map)

          //  if (!friendId!!.startsWith("-")) {
                frndUnReadCount++
                refFrndUnread!!.child("unreadCount").setValue(frndUnReadCount)
           // }
        }
    }


    private fun initFrndUnreadListener() {
        refFrndUnread = Firebase("https://utalii-fda70.firebaseio.com/users/$friendId/conversations/$userId")
        refFrndUnread!!.addValueEventListener(object : com.firebase.client.ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
             //   if(dataSnapshot.getValue() != null){
                    frndUnReadCount = Integer.parseInt(dataSnapshot.getValue(HashMap::class.java).get("unreadCount").toString())
             //   }
            }
            override fun onCancelled(firebaseError: FirebaseError) {

            }
        })
    }


}

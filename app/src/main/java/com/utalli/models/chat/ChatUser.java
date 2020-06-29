package com.utalli.models.chat;

//import com.amindset.ve.beyond.Utils.DateUtils;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.*;
import com.utalli.helpers.DateUtils;

import java.io.Serializable;
import java.util.Map;

public class ChatUser implements Serializable {

    private String id,name, email,lastMsg,profileUrl,lastUpdateTime,NotificationCount,conversationLocation,lastSeen;
    private boolean isOnline,isMsgReaded,isSelected;
    private Firebase refCredentials;
    private ChangeObserver listener;

    public interface ChangeObserver{
        void onUserChange();
    }

    public ChatUser(String name, String lastMsg, String profileUrl, String lastUpdateTime, String notificationCount) {
        this.name = name;
        this.lastMsg = lastMsg;
        this.profileUrl = profileUrl;
        this.lastUpdateTime = lastUpdateTime;
        NotificationCount = notificationCount;
    }

    public ChatUser(String id, String name, String lastMsg, String profileUrl, String lastUpdateTime, String notificationCount) {
        this.id = id;
        this.name = name;
        this.lastMsg = lastMsg;
        this.profileUrl = profileUrl;
        this.lastUpdateTime = lastUpdateTime;
        NotificationCount = notificationCount;
    }

    public ChatUser() {

    }

    public void unReadCountListener(Firebase refUnreadCount, ChangeObserver observer){
        listener=observer;

        getMsgDetails();

        refUnreadCount.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    NotificationCount = dataSnapshot.getValue().toString();
                    if(listener!=null)
                        listener.onUserChange();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void setRefCredentials(Firebase refCredentials) {
        this.refCredentials = refCredentials;
        this.refCredentials.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()){
                    case "name":
                        name=dataSnapshot.getValue().toString();
                        break;
                    case "lastSeen":
                        lastSeen=dataSnapshot.getValue().toString();
                        break;
                    case "isTyping":
                        boolean isTyping= Boolean.parseBoolean(dataSnapshot.getValue().toString());
                        break;
                    case "profilePicLink":
                        profileUrl=dataSnapshot.getValue().toString();
                        break;
                    case "isOnline":
                        isOnline= Boolean.parseBoolean(dataSnapshot.getValue().toString());
                        break;
                }
                if(listener!=null)
                    listener.onUserChange();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()){
                    case "name":
                        name=dataSnapshot.getValue().toString();
                        break;
                    case "lastSeen":
                        lastSeen=dataSnapshot.getValue().toString();
                        break;
                    case "isTyping":
                        boolean isTyping= Boolean.parseBoolean(dataSnapshot.getValue().toString());
                        break;
                    case "profilePicLink":
                        profileUrl=dataSnapshot.getValue().toString();
                        break;
                    case "isOnline":
                        isOnline= Boolean.parseBoolean(dataSnapshot.getValue().toString());
                        break;
                }

                if(listener!=null)
                    listener.onUserChange();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        getMsgDetails();
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isMsgReaded() {
        return isMsgReaded;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = DateUtils.getTimeDate(lastUpdateTime);
    }

    public String getNotificationCount() {
        return NotificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        NotificationCount = notificationCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(String online) {
        isOnline = Boolean.valueOf(online);
    }

    public String getConversationLocation() {
        return conversationLocation;
    }

    public void setConversationLocation(String conversationLocation) {
        this.conversationLocation = conversationLocation;
    }

    public boolean getMsgReaded() {
        return isMsgReaded;
    }

    public void setMsgReaded(boolean msgReaded) {
        isMsgReaded = msgReaded;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void getMsgDetails(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        Query query=databaseReference.child("conversations").child(getConversationLocation()).limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Map map= (Map) dataSnapshot.getValue();
                if(map!=null)
                    for (Object key: map.keySet()) {
                        try {
                            Map mapMsg = (Map) map.get(key);
                            String message = mapMsg.get("content").toString();
                            String createdOn = mapMsg.get("timestamp").toString();
                            String isMsgReaded = mapMsg.get("isRead").toString();
                            String type = mapMsg.get("type").toString();
                            if(type.equalsIgnoreCase("photo"))
                                message="Image";
                            setLastMsg(message);
                            setMsgReaded(Boolean.valueOf(isMsgReaded));
                            setLastUpdateTime(createdOn);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                else
                    setLastMsg("");

                if(listener!=null)
                    listener.onUserChange();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

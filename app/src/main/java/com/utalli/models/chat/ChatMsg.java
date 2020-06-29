package com.utalli.models.chat;

public class ChatMsg {
    private String toID,fromID,timestamp,isRead,content,type;

    public ChatMsg() {
    }
    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsRead() {
        return Boolean.parseBoolean(isRead);
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if(content==null)
            content="";
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
//        if(type.equalsIgnoreCase("photo") && !getContent().startsWith("http")){
//            try {
//                StorageReference storageRef= FirebaseStorage.getInstance().getReference();
//                storageRef.child(getContent()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        setContent(uri.toString());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        exception.printStackTrace();
//                        // Handle any errors
//                    }
//                });
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }
}

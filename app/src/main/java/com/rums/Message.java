package com.rums;

public class Message implements Identity {
    private String id;
    private String userID;
    private String nickName;
    private String avatarImageURL;
//  Kanske (så småningom) nöja oss med userID och messageText. nickName och avatar kan ju hämtas via userID
    private String messageText;
    private String timeStamp;

    public Message() {
    }

    public Message(String userID, String nickName, String avatarImageURL, String messageText) {
        this.userID = userID;
        this.nickName = nickName;
        this.avatarImageURL = avatarImageURL;
        this.messageText = messageText;
    }

    public Message(String userID, String nickName, String avatarImageURL, String messageText, String ID, String timeStamp) {
        this.userID = userID;
        this.nickName = nickName;
        this.avatarImageURL = avatarImageURL;
        this.messageText = messageText;
        this.id = ID;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userID='" + userID + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarImageURL='" + avatarImageURL + '\'' +
                ", messageText='" + messageText + '\'' +
                '}';
    }


    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarImageURL() {
        return avatarImageURL;
    }

    public void setAvatarImageURL(String avatarImageURL) {
        this.avatarImageURL = avatarImageURL;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


}

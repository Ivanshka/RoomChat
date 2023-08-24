package by.ivanshka.roomchat.client.chat;

import by.ivanshka.roomchat.client.exception.InvalidSessionParameterException;
import by.ivanshka.roomchat.common.util.StringUtils;
import lombok.Getter;

@Getter
public class ChatSession {
    private String username;
    private String roomId;

    public void setUsername(String username) {
        if (!StringUtils.notNullOrEmpty(username)) {
            throw new InvalidSessionParameterException("Username can't be null or empty");
        }
        this.username = username;
    }

    public void setRoomId(String roomId) {
        if (!StringUtils.notNullOrEmpty(roomId)) {
            throw new InvalidSessionParameterException("Room id can't be null or empty");
        }
        this.roomId = roomId;
    }

    public boolean isSetUp() {
        return isUsernameSet() && isJoinedRoom();
    }

    public boolean isJoinedRoom() {
        return StringUtils.notNullOrEmpty(roomId);
    }

    public boolean isUsernameSet() {
        return StringUtils.notNullOrEmpty(username);
    }

    public void resetRoomId() {
        roomId = null;
    }
}

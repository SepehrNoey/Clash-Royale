package shared.model;

import shared.MessageType;

import java.io.Serializable;

/**
 * a class to sending and receiving messages as an object
 *
 */
public class Message implements Serializable {
    // event , loginReq , ....
    private MessageType type;
    // sender name
    private String sender;
    // message content
    private String content;

    public Message(MessageType type , String sender , String content){
        this.type = type;
        this.sender = sender;
        this.content = content;
    }

    /**
     * getter
     * @return MessageType
     */
    public MessageType getType() {
        return type;
    }

    /**
     * getter
     * @return content of message
     */
    public String getContent() {
        return content;
    }

    /**
     * getter
     * @return sender name
     */
    public String getSender() {
        return sender;
    }
}

package com.owner.domain;

import java.io.OutputStream;

/**
 * @author yhw(email:861574834@qq.com)
 * @date 2015-12-07 21:05
 * @package com.owner.domain
 * @description ChatMessage  TODO(消息实体类)
 * @params TODO(进入界面传参描述)
 */
public class ChatMessage {

    public static enum MESSAGE_TYPE{
        INPUT, OUTPUT
    }

    private int id;

    private int fromId;

    private int toId;

    private String content;

    private int status;//状态

    private int type;//消息类型 收到的消息还是发送的消息

    private long time;//消息时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}

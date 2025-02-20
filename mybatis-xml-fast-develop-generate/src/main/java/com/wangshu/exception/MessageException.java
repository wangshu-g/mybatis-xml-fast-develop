package com.wangshu.exception;

import com.wangshu.enu.MessageType;
import lombok.EqualsAndHashCode;

/**
 * @author GSF
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class MessageException extends RuntimeException {

    private String message;
    private MessageType messageType;
    private Exception e;

    public MessageException(String message, MessageType messageType, Exception e) {
        this.message = message;
        this.messageType = messageType;
        this.e = e;
    }

    public MessageException(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

}

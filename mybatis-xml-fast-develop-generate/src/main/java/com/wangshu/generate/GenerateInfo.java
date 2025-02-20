package com.wangshu.generate;

import com.wangshu.enu.MessageType;
import com.wangshu.exception.MessageException;

import java.util.Formatter;
import java.util.Objects;
import java.util.function.Consumer;

public interface GenerateInfo extends Generate {

    Consumer<MessageException> getMessage();

    default void printNote(String message) {
        this.printMessage(MessageType.NOTE, null, message);
    }

    default void printNote(String message, Object... args) {
        this.printMessage(MessageType.NOTE, null, message, args);
    }

    default void printWarn(String message) {
        this.printMessage(MessageType.WARN, null, message);
    }

    default void printWarn(String message, Object... args) {
        this.printMessage(MessageType.WARN, null, message, args);
    }

    default void printError(String message) {
        this.printMessage(MessageType.ERROR, null, message);
    }

    default void printError(String message, Object... args) {
        this.printMessage(MessageType.ERROR, null, null, message, args);
    }

    default void printNote(String message, Exception e, Object... args) {
        this.printMessage(MessageType.NOTE, e, message, args);
    }

    default void printWarn(String message, Exception e, Object... args) {
        this.printMessage(MessageType.WARN, e, message, args);
    }

    default void printError(String message, Exception e, Object... args) {
        this.printMessage(MessageType.ERROR, e, message, args);
    }

    default void printMessage(MessageType messageType, Exception e, String message, Object... args) {
        if (Objects.nonNull(this.getMessage())) {
            this.getMessage().accept(new MessageException(new Formatter().format(message, args).toString(), messageType, e));
        }
    }

}

package com.wangshu.generate;

// MIT License
//
// Copyright (c) 2025 2560334673@qq.com wangshu-g https://github.com/wangshu-g/mybatis-xml-fast-develop
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

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

/*
 * Copyright (c) 2019.  Pedro Alípio, All Rights Reserved.
 *
 * This material is provided "as is", with absolutely no warranty expressed
 * or implied. Any use is at your own risk.
 *
 * Permission to use or copy this software for any purpose is hereby granted
 * without fee. Permission to modify the code and to distribute modified
 * code is also granted without any restrictions.
 */
package com.pmalipio.commitclient.exceptions;

/**
 * Exception for output lines which failed to be parsed.
 */
public class InvalidLineException extends RuntimeException {

    public InvalidLineException(final String msg) {
        super(msg);
    }
}

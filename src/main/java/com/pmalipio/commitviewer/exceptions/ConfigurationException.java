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
package com.pmalipio.commitviewer.exceptions;

/**
 * Exception for configuration errors.
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException(final String msg) {
        super(msg);
    }
}

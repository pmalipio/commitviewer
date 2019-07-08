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
package com.pmalipio.commandline.api;

import io.atlassian.fugue.Either;

import java.util.List;

/**
 * Command line executor API.
 * @param <T> The type of the returned output since there may be a line processor converting each line.
 */
public interface CommandLineExecutor<T> {

    /**
     * Runs a command.
     *
     * @param commandLineParams The execution parameters.
     * @return either a Exception is an error occurs or a List with the command output.
     */
    Either<Exception, List<T>> runCommand(CommandLineParams commandLineParams);
}

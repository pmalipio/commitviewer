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
package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import io.atlassian.fugue.Either;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

public class CommandLineExecutorImpl<T> implements CommandLineExecutor {

    public CommandLineExecutorImpl() {}

    @Override
    public Either<Exception, List<T>> runCommand(final CommandLineParams commandLineParams) {

        final ProcessBuilder builder = new ProcessBuilder()
                .command(commandLineParams.getCommand())
                .directory(new File(commandLineParams.getWorkingDirectory()));

        try {
            final Process process = builder.start();

            final StreamProcessor<T> streamProcessor = StreamProcessor.from(process.getInputStream(), commandLineParams);

            final Future<List<T>> futureResult = CompletableFuture.supplyAsync(streamProcessor);

            List<T> result = futureResult.get(commandLineParams.getTimeout(), TimeUnit.SECONDS);

            return Either.right(result);
        } catch (Exception e) {
            return Either.left(e);
        }

    }
}

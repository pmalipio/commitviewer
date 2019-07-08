package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import com.pmalipio.commandline.exceptions.ExitCodeException;
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

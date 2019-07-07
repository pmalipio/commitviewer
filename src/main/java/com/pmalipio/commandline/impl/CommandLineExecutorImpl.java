package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import com.pmalipio.commandline.exceptions.ExitCodeException;
import io.atlassian.fugue.Either;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

public class CommandLineExecutorImpl<T> implements CommandLineExecutor {

    private static CommandLineExecutor instance;

    private CommandLineExecutorImpl() {
    }

    public static CommandLineExecutor getInstance() {
        if(instance == null) {
            instance = new CommandLineExecutorImpl();
        }
        return instance;
    }

    @Override
    public Either<List<T>, Exception> runCommand(CommandLineParams commandLineParams) {

        final ProcessBuilder builder = new ProcessBuilder()
                .command(commandLineParams.getCommand())
                .directory(new File(commandLineParams.getWorkingDirectory()));

        try {
            final Process process = builder.start();

            final StreamProcessor<T> streamProcessor = StreamProcessor.from(process.getInputStream(), commandLineParams);

            final Future<List<T>> futureResult = CompletableFuture.supplyAsync(streamProcessor);

            final Integer exitCode = process.waitFor();

            if (exitCode != 0) {
                return Either.right(new ExitCodeException("Process did not return 0"));
            }

            List<T> result = futureResult.get(commandLineParams.getTimeout(), TimeUnit.SECONDS);

            return Either.left(result);
        } catch (Exception e) {
            return Either.right(e);
        }

    }
}

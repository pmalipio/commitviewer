package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import io.atlassian.fugue.Either;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

public class CommandLineExecutorImpl<T> implements CommandLineExecutor {

    CommandLineExecutorImpl() {
    }

    @Override
    public Either<List<T>, Exception> runCommand(CommandLineParams commandLineParams) {

        final ProcessBuilder builder = new ProcessBuilder()
                .command(commandLineParams.getCommand())
                .directory(new File(commandLineParams.getWorkingDirectory()));

        final Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            return Either.right(e);
        }

        final StreamProcessor<T> streamProcessor = StreamProcessor.from(process.getInputStream(), commandLineParams);

        Future<List<T>> futureResult = CompletableFuture.supplyAsync(streamProcessor);


        final Integer exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            return Either.right(e);
        }

        try {
            List<T> result = futureResult.get(10, TimeUnit.SECONDS);
            return Either.left(result);
        } catch (Exception e) {
            return Either.right(e);
        }

    }
}

package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import io.atlassian.fugue.Either;

import java.io.*;
import java.util.concurrent.Executors;

public class CommandLineExecutorImpl implements CommandLineExecutor {

    CommandLineExecutorImpl() {
    }

    @Override
    public Either<Integer, Exception> runCommand(CommandLineParams commandLineParams) {

        final ProcessBuilder builder = new ProcessBuilder()
                .command(commandLineParams.getCommand())
                .directory(new File(commandLineParams.getWorkingDirectory()));

        final Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            return Either.right(e);
        }

        final StreamProcessor streamProcessor = StreamProcessor.from(process.getInputStream(), commandLineParams);

        Executors.newSingleThreadExecutor().submit(streamProcessor);

        final Integer exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            return Either.right(e);
        }
        return Either.left(exitCode);
    }
}

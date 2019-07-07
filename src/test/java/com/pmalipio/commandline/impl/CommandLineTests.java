package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import io.atlassian.fugue.Either;
import org.junit.Test;
import org.springframework.boot.system.SystemProperties;

import java.util.List;
import java.util.function.Function;

public class CommandLineTests {

    @Test
    public void basicTest() throws InterruptedException {
        CommandLineExecutor cmd = new CommandLineExecutorImpl();

        final Function<String, String> processor = x -> x.toUpperCase();

        final CommandLineParams<String> params = CommandLineParams.builder()
                .withCommand("ls","/")
                .withLineProcessor(processor)
                .withWorkingDirectory(System.getProperty("java.io.tmpdir"))
                .build();

        final Either<List<String>, Exception> integerExceptionEither = cmd.runCommand(params);
    }

    @Test
    public void skipTest() throws InterruptedException {
        CommandLineExecutor cmd = new CommandLineExecutorImpl();

        final Function<String, String> processor = x -> x.toUpperCase();

        CommandLineParams params = CommandLineParams.builder()
                .withCommand("ls","/")
                .withSkip(3)
                .withLineProcessor(processor)
                .withWorkingDirectory(System.getProperty("java.io.tmpdir"))
                .build();

        Either<List<String>, Exception> integerExceptionEither = cmd.runCommand(params);
    }

    @Test
    public void skipAndLimitTest() throws InterruptedException {
        CommandLineExecutor cmd = new CommandLineExecutorImpl();

        final Function<String, String> processor = x -> x.toUpperCase();

        CommandLineParams params = CommandLineParams.builder()
                .withCommand("ls","/")
                .withSkip(3)
                .withLimit(5)
                .withLineProcessor(processor)
                .withWorkingDirectory(System.getProperty("java.io.tmpdir"))
                .build();

        Either<List<String>, Exception> integerExceptionEither = cmd.runCommand(params);
    }
}

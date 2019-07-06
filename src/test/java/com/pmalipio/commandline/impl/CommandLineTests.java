package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import io.atlassian.fugue.Either;
import org.junit.Test;
import org.springframework.boot.system.SystemProperties;

public class CommandLineTests {

    @Test
    public void basicTest() throws InterruptedException {
        CommandLineExecutor cmd = new CommandLineExecutorImpl();
        CommandLineParams params = CommandLineParams.builder()
                .withCommand("ls","/")
                .withLineProcessor(System.out::println)
                .withWorkingDirectory(System.getProperty("java.io.tmpdir"))
                .build();

        Either<Integer, Exception> integerExceptionEither = cmd.runCommand(params);

        Thread.sleep(2000);
    }

    @Test
    public void skipTest() throws InterruptedException {
        CommandLineExecutor cmd = new CommandLineExecutorImpl();
        CommandLineParams params = CommandLineParams.builder()
                .withCommand("ls","/")
                .withSkip(3)
                .withLineProcessor(System.out::println)
                .withWorkingDirectory(System.getProperty("java.io.tmpdir"))
                .build();

        Either<Integer, Exception> integerExceptionEither = cmd.runCommand(params);

        Thread.sleep(2000);
    }
}

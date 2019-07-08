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
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandLineTests {
    private String testFilePath;
    private CommandLineExecutor cmd = new CommandLineExecutorImpl();

    @Before
    public void setup() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("commandtest.txt").getFile());
        testFilePath = file.getAbsolutePath();
    }

    @Test
    public void basicTest() throws InterruptedException {

        final Function<String, String> processor = x -> x.toUpperCase();

        final CommandLineParams<String> params = CommandLineParams.builder()
                .withCommand("cat", testFilePath)
                .withLineProcessor(processor)
                .withWorkingDirectory("./")
                .build();

        final Either<Exception, List<String>> result = cmd.runCommand(params);

        assertThat(result.right()).contains(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
    }

    @Test
    public void skipTest() throws InterruptedException {
        final Function<String, String> processor = x -> x.toUpperCase();

        final CommandLineParams<String> params = CommandLineParams.builder()
                .withCommand("cat", testFilePath)
                .withLineProcessor(processor)
                .withWorkingDirectory("./")
                .withSkip(3)
                .build();

        final Either<Exception, List<String>> result = cmd.runCommand(params);

        assertThat(result.right()).contains(Arrays.asList("D", "E", "F", "G", "H", "I", "J"));
    }

    @Test
    public void skipAndLimitTest() throws InterruptedException {
        final Function<String, String> processor = x -> x.toUpperCase();

        final CommandLineParams<String> params = CommandLineParams.builder()
                .withCommand("cat", testFilePath)
                .withLineProcessor(processor)
                .withWorkingDirectory("./")
                .withSkip(3)
                .withLimit(5)
                .build();

        final Either<Exception, List<String>> result = cmd.runCommand(params);

        assertThat(result.right()).contains(Arrays.asList("D", "E", "F", "G", "H"));
    }

    @Test
    public void workingDirectoryTest() {
        final CommandLineParams<String> params1 = CommandLineParams.builder()
                .withCommand("bash", "-c","echo hello > test.txt")
                .build();
        final Either<Exception, List<String>> result1 = cmd.runCommand(params1);
        final CommandLineParams<String> params2 = CommandLineParams.builder()
                .withCommand("cat","test.txt")
                .build();
        final Either<Exception, List<String>> result2 = cmd.runCommand(params2);
        assertThat(result2.right()).contains(Arrays.asList("hello"));
    }

    @Test
    public void invalidTest() {
        final CommandLineParams<String> params = CommandLineParams.builder()
                .build();
        final Either<Exception, List<String>> result = cmd.runCommand(params);
        assertThat(result.isRight()).isTrue();
    }
}

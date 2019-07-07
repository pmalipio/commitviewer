package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import io.atlassian.fugue.Either;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.system.SystemProperties;

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

        final Either<List<String>, Exception> result = cmd.runCommand(params);

        assertThat(result.left()).contains(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
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

        final Either<List<String>, Exception> result = cmd.runCommand(params);

        assertThat(result.left()).contains(Arrays.asList("D", "E", "F", "G", "H", "I", "J"));
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

        final Either<List<String>, Exception> result = cmd.runCommand(params);

        assertThat(result.left()).contains(Arrays.asList("D", "E", "F", "G", "H"));
    }
}

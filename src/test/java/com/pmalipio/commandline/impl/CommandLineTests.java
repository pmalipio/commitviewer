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
    private CommandLineExecutor cmd = CommandLineExecutorImpl.getInstance();

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

    @Test
    public void workingDirectoryTest() {
        final CommandLineParams<String> params1 = CommandLineParams.builder()
                .withCommand("bash", "-c","echo hello > test.txt")
                .build();
        final Either<List<String>, Exception> result1 = cmd.runCommand(params1);
        final CommandLineParams<String> params2 = CommandLineParams.builder()
                .withCommand("cat","test.txt")
                .build();
        final Either<List<String>, Exception> result2 = cmd.runCommand(params2);
        assertThat(result2.left()).contains(Arrays.asList("hello"));
    }

    @Test
    public void invalidTest() {
        final CommandLineParams<String> params = CommandLineParams.builder()
                .build();
        final Either<List<String>, Exception> result = cmd.runCommand(params);
        assertThat(result.isRight());
    }
}

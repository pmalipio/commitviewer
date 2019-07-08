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
package com.pmalipio.commandline.api;

import org.springframework.lang.Nullable;
import java.util.function.Function;

/**
 * The command line parameters.
 *
 * @param <T> The type used by line processor output.
 */
public class CommandLineParams<T> {
    /**
     * The working directory where commands are executed.
     */
    private String workingDirectory;

    /**
     * The line processor which may transform each output line in something else.
     */
    private Function<String, T> lineProcessor;

    /**
     * The skipped results.
     */
    private Integer skip;

    /**
     * The results limit.
     */
    private Integer limit;

    /**
     * The command execution timeout in seconds.
     */
    private Integer timeout;

    /**
     * The commmand and its arguments.
     */
    private String[] command;

    /**
     * Gets a builder to build an instance.
     *
     * @return a builder.
     */
    public static CommandLineParamsBuilder builder() {
        return new CommandLineParamsBuilder();
    }

    private CommandLineParams(final String workingDirectory, final Function<String, T> lineProcessor,
                              final @Nullable Integer skip, final @Nullable Integer limit,
                              final @Nullable Integer timeout, final String... command) {
        this.workingDirectory = workingDirectory;
        this.lineProcessor = lineProcessor;
        this.skip = skip;
        this.limit = limit;
        this.timeout = timeout;
        this.command = command;
    }

    /**
     * Gets the working directory.
     *
     * @return the working directory.
     */
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Gets the line processor function.
     *
     * @return the line processor function.
     */
    public Function<String, T>   getLineProcessor() {
        return lineProcessor;
    }

    /**
     * Gets the skip value.
     *
     * @return the skip value.
     */
    public Integer getSkip() {
        return skip;
    }

    /**
     * Gets the limit value.
     *
     * @return the limit value.
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Gets the command array.
     *
     * @return the command array.
     */
    public String[] getCommand() {
        return command;
    }

    /**
     * Gets the timeout value.
     *
     * @return timeout value.
     */
    public Integer getTimeout() {
        return timeout;
    }

    public static final class CommandLineParamsBuilder<T> {
        private String workingDirectory = System.getProperty("java.io.tmpdir");
        private Function<String, T>  lineProcessor = x -> (T) x;
        private Integer skip = null;
        private Integer limit = null;
        private Integer timeout = 10; // default in seconds
        private String[] command = new String[] {""};

        private CommandLineParamsBuilder() {
        }

        public CommandLineParamsBuilder withWorkingDirectory(String workingDirectory) {
            this.workingDirectory = workingDirectory;
            return this;
        }

        public CommandLineParamsBuilder withLineProcessor(Function<String, T>  lineProcessor) {
            this.lineProcessor = lineProcessor;
            return this;
        }

        public CommandLineParamsBuilder withSkip(Integer skip) {
            this.skip = skip;
            return this;
        }

        public CommandLineParamsBuilder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public CommandLineParamsBuilder withTimeout(Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        public CommandLineParamsBuilder withCommand(String... command) {
            this.command = command;
            return this;
        }

        public CommandLineParams build() {
            CommandLineParams commandLineParams = new CommandLineParams(workingDirectory, lineProcessor, skip, limit,
                    timeout, command);
            return commandLineParams;
        }
    }
}

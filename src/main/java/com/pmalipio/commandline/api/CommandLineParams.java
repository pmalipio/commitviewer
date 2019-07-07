package com.pmalipio.commandline.api;

import org.springframework.lang.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommandLineParams<T> {
    private String workingDirectory;
    private Function<String, T> lineProcessor;
    private Integer skip;
    private Integer limit;
    private Integer timeout; // in seconds
    private String[] command;

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

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Function<String, T>   getLineProcessor() {
        return lineProcessor;
    }

    public Integer getSkip() {
        return skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public String[] getCommand() {
        return command;
    }

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

package com.pmalipio.commandline.api;

import org.springframework.lang.Nullable;

import java.util.function.Consumer;

public class CommandLineParams {
    private String workingDirectory;
    private Consumer<String> lineProcessor;
    private Integer skip;
    private Integer limit;
    private String[] command;

    public static CommandLineParamsBuilder builder() {
        return new CommandLineParamsBuilder();
    }

    private CommandLineParams(final String workingDirectory, final Consumer<String> lineProcessor,
                              final @Nullable Integer skip, final @Nullable Integer limit,
                              final String... command) {
        this.workingDirectory = workingDirectory;
        this.lineProcessor = lineProcessor;
        this.skip = skip;
        this.limit = limit;
        this.command = command;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Consumer<String> getLineProcessor() {
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

    public static final class CommandLineParamsBuilder {
        private String workingDirectory;
        private Consumer<String> lineProcessor;
        private Integer skip;
        private Integer limit;
        private String[] command;

        private CommandLineParamsBuilder() {
        }

        public CommandLineParamsBuilder withWorkingDirectory(String workingDirectory) {
            this.workingDirectory = workingDirectory;
            return this;
        }

        public CommandLineParamsBuilder withLineProcessor(Consumer<String> lineProcessor) {
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

        public CommandLineParamsBuilder withCommand(String... command) {
            this.command = command;
            return this;
        }

        public CommandLineParams build() {
            CommandLineParams commandLineParams = new CommandLineParams(workingDirectory, lineProcessor, skip, limit, command);
            return commandLineParams;
        }
    }
}
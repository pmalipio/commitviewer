package com.pmalipio.gitclient.api;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.impl.CommandLineExecutorImpl;

public class GitClientConfiguration {
    private final CommandLineExecutor commandLineExecutor;
    private final Integer commandExecutionTimeout;
    private final String baseDirectory;

    private GitClientConfiguration(final CommandLineExecutor commandLineExecutor, final Integer commandExecutionTimeout,
                                   final String baseDirectory) {
        this.commandLineExecutor = commandLineExecutor;
        this.commandExecutionTimeout = commandExecutionTimeout;
        this.baseDirectory = baseDirectory;
    }

    public CommandLineExecutor getCommandLineExecutor() {
        return commandLineExecutor;
    }

    public Integer getCommandExecutionTimeout() {
        return commandExecutionTimeout;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public static GitClientConfigurationBuilder builder() {
        return new GitClientConfigurationBuilder();
    }

    public static final class GitClientConfigurationBuilder {
        private CommandLineExecutor commandLineExecutor = new CommandLineExecutorImpl(); // default command line executor
        private Integer commandExecutionTimeout = 10; // default timeout in seconds
        private String baseDirectory = System.getProperty("java.io.tmpdir"); // default is tmp

        private GitClientConfigurationBuilder() {
        }

        public GitClientConfigurationBuilder withCommandLineExecutor(final CommandLineExecutor commandLineExecutor) {
            this.commandLineExecutor = commandLineExecutor;
            return this;
        }

        public GitClientConfigurationBuilder withCommandExecutionTimeout(final Integer commandExecutionTimeout) {
            this.commandExecutionTimeout = commandExecutionTimeout;
            return this;
        }

        public GitClientConfigurationBuilder withBaseDirectory(final String baseDirectory) {
            this.baseDirectory = baseDirectory;
            return this;
        }

        public GitClientConfiguration build() {
            return new GitClientConfiguration(commandLineExecutor, commandExecutionTimeout, baseDirectory);
        }
    }
}

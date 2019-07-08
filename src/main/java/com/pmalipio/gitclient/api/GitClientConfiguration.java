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
package com.pmalipio.gitclient.api;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.impl.CommandLineExecutorImpl;

/**
 * Git client configuration.
 */
public class GitClientConfiguration {
    /**
     * The command line executor.
     */
    private final CommandLineExecutor commandLineExecutor;

    /**
     * The command timeout.
     */
    private final Integer commandExecutionTimeout;

    /**
     * The base directory for the git repositories.
     */
    private final String baseDirectory;

    private GitClientConfiguration(final CommandLineExecutor commandLineExecutor, final Integer commandExecutionTimeout,
                                   final String baseDirectory) {
        this.commandLineExecutor = commandLineExecutor;
        this.commandExecutionTimeout = commandExecutionTimeout;
        this.baseDirectory = baseDirectory;
    }

    /**
     * Gets the command line executor.
     *
     * @return the command line executor.
     */
    public CommandLineExecutor getCommandLineExecutor() {
        return commandLineExecutor;
    }

    /**
     * Gets the command execution timeout.
     *
     * @return the command execution timeout.
     */
    public Integer getCommandExecutionTimeout() {
        return commandExecutionTimeout;
    }

    /**
     * Gets the base directory.
     *
     * @return the base direcoty.
     */
    public String getBaseDirectory() {
        return baseDirectory;
    }

    /**
     * Gets a builder to create git client configuration instances.
     *
     * @return a builder for git client configuration.
     */
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

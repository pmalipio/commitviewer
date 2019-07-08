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
package com.pmalipio.commitclient.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmalipio.commandline.impl.CommandLineExecutorImpl;
import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import com.pmalipio.commitclient.exceptions.InvalidLineException;
import com.pmalipio.gitclient.api.GitClient;
import com.pmalipio.gitclient.api.GitClientConfiguration;
import com.pmalipio.gitclient.exceptions.DirectoryParseError;
import com.pmalipio.gitclient.impl.GitClientImpl;
import io.atlassian.fugue.Either;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command line based commit client implementation.
 */
public class CommandLineCommitClient implements CommitClient {
    private static final String BASE_URL = "https://github.com/";
    private static final String DIRECTORY_ERROR_MESSAGE = "Could not extract the directory from the github url";

    private final GitClient<CommitInfo> gitClient;
    private final CommitClientConfiguration configuration;

    private CommandLineCommitClient(final CommitClientConfiguration configuration) {
        this.configuration = configuration;
        GitClientConfiguration gitClientConfiguration = GitClientConfiguration.builder()
                .withCommandExecutionTimeout(configuration.getTimeout())
                .withCommandLineExecutor(new CommandLineExecutorImpl<CommitInfo>())
                .build();
        this.gitClient = GitClientImpl.from(gitClientConfiguration);
    }

    /**
     * Creates a CommandLineCommitClient from the configuration.
     *
     * @param configuration the client configuration.
     * @return a command line based commit client.
     */
    public static CommandLineCommitClient from(final CommitClientConfiguration configuration) {
        return new CommandLineCommitClient(configuration);
    }

    private Either<Exception, List<CommitInfo>> setUpCommitList(final String repositoryUrl, final String branch) {
        final Either<Exception, List<String>> cloneResult = gitClient.cloneRepository(repositoryUrl);
        if (cloneResult.isLeft()) {
            return cloneResult.map(x -> Collections.emptyList());
        }
        final Optional<String> dirOpt = GitClientImpl.getDirectoryFromURl(repositoryUrl);
        if (!dirOpt.isPresent()) {
            return Either.left(new DirectoryParseError(DIRECTORY_ERROR_MESSAGE));
        }

        final Either<Exception, List<String>> checkoutResult = gitClient.checkout(dirOpt.get(), branch);
        if (checkoutResult.isLeft()) {
            return checkoutResult.map(x -> Collections.emptyList());
        }
        return Either.right(Collections.emptyList());
    }

    private CommitInfo processLogLine(final String line) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(line, CommitInfo.class);
        } catch (IOException e) {
            throw new InvalidLineException("Failed to parse line: " + line);
        }
    }

    @Override
    public Either<Exception, List<CommitInfo>> listCommits(final String user, final String repName, final String branch) {
        final String repositoryUrl = BASE_URL + user + "/" + repName + ".git";
        final Either<Exception, List<CommitInfo>> setupResult = setUpCommitList(repositoryUrl, branch);
        if (setupResult.isLeft()) {
            return setupResult;
        }
        final Optional<String> dirOpt = GitClientImpl.getDirectoryFromURl(repositoryUrl);
        if (!dirOpt.isPresent()) {
            return Either.left(new DirectoryParseError(DIRECTORY_ERROR_MESSAGE));
        }
        return gitClient.processLog(dirOpt.get(), this::processLogLine);
    }

    @Override
    public Either<Exception, List<CommitInfo>> listCommits(final String user, final String repName, final String branch, final int page) {
        final String repositoryUrl = BASE_URL + user + "/" + repName + ".git";
        final Either<Exception, List<CommitInfo>> setupResult = setUpCommitList(repositoryUrl, branch);
        if (setupResult.isLeft()) {
            return setupResult;
        }
        final Optional<String> dirOpt = GitClientImpl.getDirectoryFromURl(repositoryUrl);
        if (!dirOpt.isPresent()) {
            return Either.left(new DirectoryParseError(DIRECTORY_ERROR_MESSAGE));
        }
        return gitClient.processLog(dirOpt.get(), this::processLogLine, (page - 1) * configuration.getPageSize(), configuration.getPageSize());
    }

}

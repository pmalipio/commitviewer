package com.pmalipio.commitclient.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmalipio.commandline.impl.CommandLineExecutorImpl;
import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import com.pmalipio.gitclient.api.GitClient;
import com.pmalipio.gitclient.api.GitClientConfiguration;
import com.pmalipio.gitclient.impl.GitClientImpl;
import io.atlassian.fugue.Either;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CommandLineCommitClient implements CommitClient {

    private static CommandLineCommitClient instance;

    private final GitClient<CommitInfo> gitClient;
    private final CommitClientConfiguration configuration;

    private CommandLineCommitClient(final CommitClientConfiguration configuration) {
        this.configuration = configuration;
        GitClientConfiguration gitClientConfiguration = GitClientConfiguration.builder()
                .withCommandExecutionTimeout(configuration.getTimeout())
                .withCommandLineExecutor(CommandLineExecutorImpl.getInstance())
                .build();
        this.gitClient = GitClientImpl.getInstance(gitClientConfiguration);
    }

    public static CommandLineCommitClient getInstance(final CommitClientConfiguration configuration) {
        if(instance == null) {
            instance = new CommandLineCommitClient(configuration);
        }
        return instance;
    }

    private Either<List<CommitInfo>, Exception>setUpCommitList(final String repositoryUrl, final String branch) {
        final String dir = GitClientImpl.getDirectoryFromURl(repositoryUrl).get();
        final Either cloneResult = gitClient.cloneRepository(repositoryUrl);
        if (cloneResult.isRight()) return cloneResult;
        final Either checkoutResult = gitClient.checkout(dir, branch);
        if (checkoutResult.isRight()) return checkoutResult;
        return Either.left(Collections.emptyList());
    }

    private CommitInfo processLogLine(final String line) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            CommitInfo commitInfo = mapper.readValue(line, CommitInfo.class);
            return commitInfo;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse line: " + line);
        }
    }

    @Override
    public Either<List<CommitInfo>, Exception> listCommits(final String repositoryUrl, final String branch) {
        final String dir = GitClientImpl.getDirectoryFromURl(repositoryUrl).get();
        final Either<List<CommitInfo>, Exception> setupResult = setUpCommitList(repositoryUrl, branch);
        if (setupResult.isRight()) {
            return setupResult;
        }
        return gitClient.processLog(dir, this::processLogLine);
    }

    @Override
    public Either<List<CommitInfo>, Exception> listCommits(final String repositoryUrl, final String branch, final int page) {
        final String dir = GitClientImpl.getDirectoryFromURl(repositoryUrl).get();
        final Either<List<CommitInfo>, Exception> setupResult = setUpCommitList(repositoryUrl, branch);
        if (setupResult.isRight()) {
            return setupResult;
        }
        return gitClient.processLog(dir, this::processLogLine, page * configuration.getPageSize(), configuration.getPageSize());
    }

}

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CommandLineCommitClient implements CommitClient {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineCommitClient.class);

    private static final String BASE_URL = "https://github.com/";

    private final GitClient<CommitInfo> gitClient;
    private final CommitClientConfiguration configuration;

    private CommandLineCommitClient(final CommitClientConfiguration configuration) {
        this.configuration = configuration;
        GitClientConfiguration gitClientConfiguration = GitClientConfiguration.builder()
                .withCommandExecutionTimeout(configuration.getTimeout())
                .withCommandLineExecutor(new CommandLineExecutorImpl())
                .build();
        this.gitClient = GitClientImpl.from(gitClientConfiguration);
    }

    public static CommandLineCommitClient from(final CommitClientConfiguration configuration) {
        return new CommandLineCommitClient(configuration);
    }

    private Either<Exception, List<CommitInfo>>setUpCommitList(final String repositoryUrl, final String branch) {
        final String dir = GitClientImpl.getDirectoryFromURl(repositoryUrl).get();
        final Either<Exception, List<String>> cloneResult = gitClient.cloneRepository(repositoryUrl);
        if (cloneResult.isLeft()) {
            return cloneResult.map(x -> Collections.emptyList());
        }
        final Either<Exception, List<String>> checkoutResult = gitClient.checkout(dir, branch);
        if (checkoutResult.isLeft()) {
            return checkoutResult.map(x -> Collections.emptyList());
        }
        return Either.right(Collections.emptyList());
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
    public Either<Exception, List<CommitInfo>> listCommits(final String user, final String repName, final String branch) {
        final String repositoryUrl = BASE_URL + user + "/" + repName + ".git";
        final String dir = GitClientImpl.getDirectoryFromURl(repositoryUrl).get();
        final Either<Exception, List<CommitInfo>> setupResult = setUpCommitList(repositoryUrl, branch);
        if (setupResult.isLeft()) {
            return setupResult;
        }
        return gitClient.processLog(dir, this::processLogLine);
    }

    @Override
    public Either<Exception, List<CommitInfo>> listCommits(final String user, final String repName, final String branch, final int page) {
        final String repositoryUrl = BASE_URL + user + "/" + repName + ".git";
        final String dir = GitClientImpl.getDirectoryFromURl(repositoryUrl).get();
        final Either<Exception, List<CommitInfo>> setupResult = setUpCommitList(repositoryUrl, branch);
        if (setupResult.isLeft()) {
            return setupResult;
        }
        return gitClient.processLog(dir, this::processLogLine, (page - 1) * configuration.getPageSize(), configuration.getPageSize());
    }

}

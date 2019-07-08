package com.pmalipio.gitclient.impl;

import com.pmalipio.commandline.api.CommandLineParams;
import com.pmalipio.gitclient.api.GitClient;
import com.pmalipio.gitclient.api.GitClientConfiguration;
import io.atlassian.fugue.Either;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitClientImpl<T> implements GitClient {

    private final GitClientConfiguration configuration;

    private GitClientImpl(final GitClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public static synchronized GitClientImpl from(final GitClientConfiguration configuration) {
        return new GitClientImpl(configuration);
    }

    @Override
    public Either<Exception, List<String>> cloneRepository(final String url) {
        final String dir = GitClientImpl.getDirectoryFromURl(url).get();

        try {
            FileUtils.deleteDirectory(new File(configuration.getBaseDirectory() + "/" + dir));
        } catch (IOException e) {
            return Either.left(e);
        }

        final CommandLineParams gitCloneCmd = CommandLineParams.builder()
                .withWorkingDirectory(configuration.getBaseDirectory())
                .withTimeout(configuration.getCommandExecutionTimeout())
                .withCommand("git", "clone", url)
                .build();

        return configuration.getCommandLineExecutor().runCommand(gitCloneCmd);
    }

    @Override
    public Either<Exception, List<String>> checkout(final String repositoryDir, final String branch) {
        final CommandLineParams gitCheckoutCmd = CommandLineParams.builder()
                .withWorkingDirectory(configuration.getBaseDirectory() + "/" + repositoryDir)
                .withTimeout(configuration.getCommandExecutionTimeout())
                .withCommand("git", "checkout", branch)
                .build();

        return configuration.getCommandLineExecutor().runCommand(gitCheckoutCmd);
    }

    private CommandLineParams.CommandLineParamsBuilder getParamsBuilderForLogProcessor(final String repositoryDir,
                                                                                       final Function logProcessor) {
        return CommandLineParams.builder()
                .withWorkingDirectory(configuration.getBaseDirectory() + "/" + repositoryDir)
                .withTimeout(configuration.getCommandExecutionTimeout())
                .withCommand("git","log", "--pretty=format: {\"commit\": \"%H\",  \"author\": \"%aN <%aE>\",  \"date\": \"%ad\",  \"message\": \"%f\"}");
    }

    @Override
    public Either<List, Exception> processLog(final String repositoryDir, final Function logProcessor) {
        CommandLineParams logCmd = getParamsBuilderForLogProcessor(repositoryDir, logProcessor).build();
        return configuration.getCommandLineExecutor().runCommand(logCmd);
    }

    @Override
    public Either<Exception, List> processLog(String repositoryDir, Function logProcessor, int skip, int limit) {
        CommandLineParams logCmd = getParamsBuilderForLogProcessor(repositoryDir, logProcessor)
                .withSkip(skip)
                .withLimit(limit)
                .withLineProcessor(logProcessor)
                .build();

        return configuration.getCommandLineExecutor().runCommand(logCmd);
    }

    public static final Optional<String> getDirectoryFromURl(final String url) {
        final Pattern pattern = Pattern.compile(".*\\/(.*)\\.git$");
        final Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        } else {
            return Optional.empty();
        }
    }
}

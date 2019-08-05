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
package com.pmalipio.gitclient.impl;

import com.pmalipio.commandline.api.CommandLineParams;
import com.pmalipio.gitclient.api.GitClient;
import com.pmalipio.gitclient.api.GitClientConfiguration;
import com.pmalipio.gitclient.exceptions.DirectoryParseError;
import io.atlassian.fugue.Either;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Git client implementation
 */
public class GitClientImpl implements GitClient {

    private final GitClientConfiguration configuration;

    private GitClientImpl(final GitClientConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Builds a git client instance.
     *
     * @param configuration the git client configuration.
     * @return a {@link GitClientImpl} instance.
     */
    public static synchronized GitClientImpl from(final GitClientConfiguration configuration) {
        return new GitClientImpl(configuration);
    }

    @Override
    public Either<Exception, List<String>> cloneRepository(final String url) {
        final Optional<String> dirOpt = GitClientImpl.getDirectoryFromURl(url);
        if (!dirOpt.isPresent()) {
            return Either.left(new DirectoryParseError("Could not extract the directory from the url"));
        }

        try {
            FileUtils.deleteDirectory(new File(configuration.getBaseDirectory() + File.pathSeparator + dirOpt.get()));
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
        // this git log command creates a one line output for each commit:
        // It applies the followin template: commit:(.*),author:(.*),date:(.*),message:(.*)__EOL__
        // It cleans up ^M, then replaces all CR, then it replaces line breaks by the __NL__ tag.
        // The only linebreack here is by replacing the __EOL__ tag by a \n. Finally it removes __NL__at the
        // beginning of the line whish is garbage.
        return CommandLineParams.builder()
                .withWorkingDirectory(configuration.getBaseDirectory() + "/" + repositoryDir)
                .withTimeout(configuration.getCommandExecutionTimeout())
                .withLineProcessor(logProcessor)
                .withCommand("/bin/sh", "-c", "git log --pretty=format:'commit:%H,author:%aN <%aE>,date:%ad,message:%B__EOL__'" +
                "|sed 's/\\r//'|sed ':a;N;$!ba;s/\\n/__NL__/g'|sed 's/__EOL__/\\n/g'|sed 's/^__NL__//g'");
    }

    @Override
    public Either<Exception, List> processLog(final String repositoryDir, final Function logProcessor) {
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

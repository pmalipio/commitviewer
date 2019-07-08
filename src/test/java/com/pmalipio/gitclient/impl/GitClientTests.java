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

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import com.pmalipio.commandline.impl.CommandLineExecutorImpl;
import com.pmalipio.gitclient.api.GitClient;
import com.pmalipio.gitclient.api.GitClientConfiguration;
import io.atlassian.fugue.Either;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GitClientTests {

    private GitClient gitClient;
    private CommandLineExecutor commandLineExecutor;

    @Before
    public void setup() {
        this.commandLineExecutor = new CommandLineExecutorImpl();
        this.gitClient = GitClientImpl.from(GitClientConfiguration.builder().build());
    }

    @Test
    public void directoryFromUrlTest() {
        final Optional<String> dir = GitClientImpl.getDirectoryFromURl("https://github.com/pmalipio/rabbitflow.git");
        assertThat(dir.isPresent()).isTrue();
        assertThat(dir.get()).isEqualTo("rabbitflow");
    }

    @Test
    public void cloneRepositoryTest() {
        final String url = "https://github.com/pmalipio/rabbitflow.git";
        final Either<Exception, List<String>> cloneResult = gitClient.cloneRepository(url);
        assertThat(cloneResult.isRight()).isTrue();

        final CommandLineParams ls = CommandLineParams.builder()
                .withWorkingDirectory(System.getProperty("java.io.tmpdir") + "/" + GitClientImpl.getDirectoryFromURl(url).get())
                .withCommand("ls")
                .build();

        final Either<Exception, List<String>>  lsResult = commandLineExecutor.runCommand(ls);
        assertThat(lsResult.right().get()).size().isGreaterThan(0);
    }

    @Test
    public void checkoutTest() {
        final String url = "https://github.com/pmalipio/rabbitflow.git";
        gitClient.cloneRepository(url);

        final Either<Exception, List<String>> checkout = gitClient.checkout(GitClientImpl.getDirectoryFromURl(url).get(),"master");
        assertThat(checkout.isLeft()).isTrue();
    }

    @Test
    public void logProcessorTest() {
        final String url = "https://github.com/pmalipio/rabbitflow.git";
        final String dir = GitClientImpl.getDirectoryFromURl(url).get();
        gitClient.cloneRepository(url);
        gitClient.checkout(dir,"master");
        Either<Exception, List<String>> logResult = gitClient.processLog(dir, x -> x);
        assertThat(logResult.isRight()).isTrue();
        assertThat(logResult.right().get()).size().isGreaterThan(0);

        Either<Exception, List<String>> logResult2 = gitClient.processLog(dir, x -> x, 1, 2);
        assertThat(logResult2.isRight()).isTrue();
        assertThat(logResult2.right().get()).size().isEqualTo(2);
    }
}

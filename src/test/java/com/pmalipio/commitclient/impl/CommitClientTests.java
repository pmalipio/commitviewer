package com.pmalipio.commitclient.impl;

import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class CommitClientTests {

    @Test
    public void basicTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(10)
                .build();

        CommandLineCommitClient cmdCommitClient = CommandLineCommitClient.getInstance(configuration);

        Either<List<CommitInfo>, Exception> commits = cmdCommitClient.listCommits("https://github.com/pmalipio/commitviewer.git", "master");
        assertThat(commits.isLeft());
        assertThat(commits.left().get()).size().isGreaterThan(0);
    }

    @Test
    public void timeoutTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(2)
                .build();

        CommandLineCommitClient cmdCommitClient = CommandLineCommitClient.getInstance(configuration);
        Either<List<CommitInfo>, Exception> commits = cmdCommitClient.listCommits("https://github.com/typelevel/cats.git", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void pageTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(10)
                .build();

        CommandLineCommitClient cmdCommitClient = CommandLineCommitClient.getInstance(configuration);
        Either<List<CommitInfo>, Exception> commits = cmdCommitClient.listCommits("https://github.com/typelevel/cats.git", "master", 3);
        assertThat(commits.isLeft());
        assertThat(commits.left().get()).size().isEqualTo(10);
    }
}

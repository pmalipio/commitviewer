package com.pmalipio.commitclient.impl;

import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class CommitCmdLineClientTests {

    @Test
    public void basicTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(10)
                .build();

        CommandLineCommitClient cmdCommitClient = CommandLineCommitClient.from(configuration);

        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("pmalipio", "commitviewer", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }

    @Test
    public void timeoutTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(2)
                .build();

        CommandLineCommitClient cmdCommitClient = CommandLineCommitClient.from(configuration);
        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("scala", "scala", "master");
        assertThat(commits.isLeft());
        assertThat(commits.left().get()).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void pageTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(50)
                .build();

        CommandLineCommitClient cmdCommitClient = CommandLineCommitClient.from(configuration);
        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("typelevel", "cats", "master", 3);
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isEqualTo(10);
    }
}

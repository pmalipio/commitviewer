package com.pmalipio.commitclient.impl;

import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommitGithubClientTests {
    @Test
    public void basicTest() {
        GithubCommitClient cmdCommitClient = new GithubCommitClient();

        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("pmalipio", "commitviewer", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }

    @Test
    public void pageTest() {
        GithubCommitClient cmdCommitClient = new GithubCommitClient();

        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("pmalipio", "commitviewer", "master", 1);
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }

    @Test
    public void heavyTest() {
        GithubCommitClient cmdCommitClient = new GithubCommitClient();

        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("scala", "scala", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }
}

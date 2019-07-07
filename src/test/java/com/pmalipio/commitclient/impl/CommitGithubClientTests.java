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
        GithubCommitClient cmdCommitClient = GithubCommitClient.getInstance();

        Either<List<CommitInfo>, Exception> commits = cmdCommitClient.listCommits("pmalipio", "commitviewer", "master");
        assertThat(commits.isLeft());
        assertThat(commits.left().get()).size().isGreaterThan(0);
    }

    public void pageTest() {
        GithubCommitClient cmdCommitClient = GithubCommitClient.getInstance();

        Either<List<CommitInfo>, Exception> commits = cmdCommitClient.listCommits("pmalipio", "commitviewer", "master", 1);
        assertThat(commits.isLeft());
        assertThat(commits.left().get()).size().isGreaterThan(0);
    }
}

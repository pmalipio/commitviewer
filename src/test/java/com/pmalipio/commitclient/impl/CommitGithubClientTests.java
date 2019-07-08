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

import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommitGithubClientTests {
    @Test
    public void basicTest() {
        GithubCommitClient cmdCommitClient = new GithubCommitClient();

        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("pmalipio", "rabbitflow", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }

    @Test
    public void pageTest() {
        GithubCommitClient cmdCommitClient = new GithubCommitClient();

        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("pmalipio", "rabbitflow", "master", 1);
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }

    @Test
    public void heavyTest() {
        GithubCommitClient cmdCommitClient = new GithubCommitClient();

        Either<Exception, List<CommitInfo>> commits = cmdCommitClient.listCommits("typelevel", "cats", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }
}

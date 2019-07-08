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

import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FallbackClientTests {

    @Test
    public void basicTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(200)
                .build();

        final FallbackClient fallbackClient = FallbackClient.from(new GithubCommitClient(), CommandLineCommitClient.from(configuration));
        Either<Exception, List<CommitInfo>> commits = fallbackClient.listCommits("pmalipio", "rabbitflow", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }

    @Test
    public void firstFailsTest() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(10)
                .withTimeout(200)
                .build();

        final FallbackClient fallbackClient = FallbackClient.from(new FailClient(), CommandLineCommitClient.from(configuration));
        Either<Exception, List<CommitInfo>> commits = fallbackClient.listCommits("typelevel", "cats", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }

    class FailClient implements CommitClient {
        private FailClient() {
        }

        @Override
        public Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch) {
            return Either.left(new RuntimeException());
        }

        @Override
        public Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch, int page) {
            return Either.left(new RuntimeException());
        }
    }
}

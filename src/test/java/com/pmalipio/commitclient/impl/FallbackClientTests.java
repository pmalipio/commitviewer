package com.pmalipio.commitclient.impl;

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
                .withTimeout(50)
                .build();

        final FallbackClient fallbackClient = FallbackClient.from(new GithubCommitClient(), CommandLineCommitClient.from(configuration));
        Either<Exception, List<CommitInfo>> commits = fallbackClient.listCommits("scala", "scala", "master");
        assertThat(commits.isRight());
        assertThat(commits.right().get()).size().isGreaterThan(0);
    }
}

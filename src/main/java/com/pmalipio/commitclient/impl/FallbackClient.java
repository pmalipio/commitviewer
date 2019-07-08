package com.pmalipio.commitclient.impl;

import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;

import java.util.List;

public class FallbackClient implements CommitClient {

    private final CommitClient primaryClient;
    private final CommitClient secondaryClient;

    public static synchronized FallbackClient from(final CommitClient primaryClient, final CommitClient secondaryClient) {
        return new FallbackClient(primaryClient, secondaryClient);
    }

    private FallbackClient(final CommitClient primaryClient, final CommitClient secomdaryClient) {
        this.primaryClient = primaryClient;
        this.secondaryClient = secomdaryClient;
    }

    @Override
    public Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch) {
        return primaryClient.listCommits(user, repname, branch)
                .orElse(secondaryClient.listCommits(user, repname, branch));
    }

    @Override
    public Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch, int page) {
        return primaryClient.listCommits(user, repname, branch, page)
                .orElse(secondaryClient.listCommits(user, repname, branch, page));
    }
}

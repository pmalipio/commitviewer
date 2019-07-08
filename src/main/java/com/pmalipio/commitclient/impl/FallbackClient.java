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
import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;

import java.util.List;

/**
 * The implementation of a fallback commit client.
 */
public class FallbackClient implements CommitClient {

    /**
     * The primary client.
     */
    private final CommitClient primaryClient;

    /**
     * The secondary client.
     */
    private final CommitClient secondaryClient;

    /**
     * Creates a fallback client instance.
     *
     * @param primaryClient   The primary client.
     * @param secondaryClient The secondary client.
     * @return a {@link FallbackClient} instance.
     */
    public static FallbackClient from(final CommitClient primaryClient, final CommitClient secondaryClient) {
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

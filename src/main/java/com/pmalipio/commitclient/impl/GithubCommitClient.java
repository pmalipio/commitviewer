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
import com.pmalipio.commitclient.data.GithubInfo;
import io.atlassian.fugue.Either;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A commit client based on the Github API.
 */
public class GithubCommitClient implements CommitClient {
    private static final String BASE_URL = "https://api.github.com/repos/";

    private final RestTemplate restTemplate;

    /**
     * build a {@link GithubCommitClient} instance.
     */
    public GithubCommitClient() {
        this.restTemplate= new RestTemplateBuilder().build();
    }

    /**
     * Converts the github API commit info result into {@link  CommitInfo}.
     *
     * @param githubInfo The github commit data.
     * @return a {@link CommitInfo} instance with the commit information.
     */
    private CommitInfo toCommitInfo(final GithubInfo githubInfo) {
        return CommitInfo.buiilder()
                .withCommit(githubInfo.getSha())
                .withAuthor(githubInfo.getCommit().getCommitter().getName() + " <" + githubInfo.getCommit().getCommitter().getEmail() + ">")
                .withDate(githubInfo.getCommit().getCommitter().getDate())
                .withMessage(githubInfo.getCommit().getMessage())
                .build();
    }

    @Override
    public Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch) {
        try {
            final GithubInfo[] gitHubResults = restTemplate.getForObject(BASE_URL + user + "/" + repname + "/commits?sha=" + branch, GithubInfo[].class);
            final List<CommitInfo> commitInfoList = Stream.of(gitHubResults)
                    .map(this::toCommitInfo)
                    .collect(Collectors.toList());
            return Either.right(commitInfoList);
        } catch (Exception e) {
            return Either.left(e);
        }
    }

    @Override
    public Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch, int page) {
        try {
            final  GithubInfo[] gitHubResults = restTemplate.getForObject(BASE_URL + user + "/" + repname + "/commits?sha=" + branch + "&page=" + page, GithubInfo[].class);
            final List<CommitInfo> commitInfoList = Stream.of(gitHubResults)
                    .map(this::toCommitInfo)
                    .collect(Collectors.toList());
            return Either.right(commitInfoList);
        } catch (Exception e) {
            return Either.left(e);
        }
    }
}

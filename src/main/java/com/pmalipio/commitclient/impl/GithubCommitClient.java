package com.pmalipio.commitclient.impl;

import com.pmalipio.commandline.impl.StreamProcessor;
import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.data.CommitInfo;
import com.pmalipio.commitclient.data.GithubInfo;
import io.atlassian.fugue.Either;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GithubCommitClient implements CommitClient {
    private static final String BASE_URL = "https://api.github.com/repos/";
    private static GithubCommitClient instance;

    public static GithubCommitClient getInstance() {
        if(instance == null) {
            instance = new GithubCommitClient();
        }
        return instance;
    }

    private GithubCommitClient() {
    }

    private CommitInfo toCommitInfo(final GithubInfo githubInfo) {
        return CommitInfo.buiilder()
                .withCommit(githubInfo.getSha())
                .withAuthor(githubInfo.getCommit().getCommitter().getName() + " <" + githubInfo.getCommit().getCommitter().getEmail() + ">")
                .withDate(githubInfo.getCommit().getCommitter().getDate())
                .withMessage(githubInfo.getCommit().getMessage())
                .build();
    }

    @Override
    public Either<List<CommitInfo>, Exception> listCommits(String user, String repname, String branch) {
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final GithubInfo[] gitHubResults = restTemplate.getForObject(BASE_URL + user + "/" + repname + "/commits?sha=" + branch, GithubInfo[].class);
            final List<CommitInfo> commitInfoList = Stream.of(gitHubResults)
                    .map(this::toCommitInfo)
                    .collect(Collectors.toList());
            return Either.left(commitInfoList);
        } catch (Exception e) {
            return Either.right(e);
        }
    }

    @Override
    public Either<List<CommitInfo>, Exception> listCommits(String user, String repname, String branch, int page) {
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final  GithubInfo[] gitHubResults = restTemplate.getForObject(BASE_URL + user + "/" + repname + "/commits?sha=" + branch + "&page=" + page, GithubInfo[].class);
            final List<CommitInfo> commitInfoList = Stream.of(gitHubResults)
                    .map(this::toCommitInfo)
                    .collect(Collectors.toList());
            return Either.left(commitInfoList);
        } catch (Exception e) {
            return Either.right(e);
        }
    }
}

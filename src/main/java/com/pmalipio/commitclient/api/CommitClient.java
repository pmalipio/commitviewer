package com.pmalipio.commitclient.api;

import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;

import java.util.List;

public interface CommitClient {


    Either<List<CommitInfo>, Exception> listCommits(String repositoryUrl, String branch);

    Either<List<CommitInfo>, Exception> listCommits(String repositoryUrl, String branch, int page);
}

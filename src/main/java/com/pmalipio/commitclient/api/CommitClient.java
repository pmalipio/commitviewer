package com.pmalipio.commitclient.api;

import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;

import java.util.List;

public interface CommitClient {


    Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch);

    Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch, int page);
}

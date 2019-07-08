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
package com.pmalipio.commitclient.api;

import com.pmalipio.commitclient.data.CommitInfo;
import io.atlassian.fugue.Either;

import java.util.List;

/**
 * Commit client API.
 */
public interface CommitClient {

    /**
     * List the github repository commits for a given branch.
     *
     * @param user    the github user.
     * @param repname the repository name.
     * @param branch  the branch name.
     * @return either an exception is an error occurs or a list with the commit information.
     */
    Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch);

    /**
     * List the github repository commits for a given branch and a page.
     *
     * @param user    the github user.
     * @param repname the repository name.
     * @param branch  the branch name.
     * @param page    the page.
     * @return either an exception is an error occurs or a list with the commit information.
     */
    Either<Exception, List<CommitInfo>> listCommits(String user, String repname, String branch, int page);
}

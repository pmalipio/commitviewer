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
package com.pmalipio.gitclient.api;

import io.atlassian.fugue.Either;

import java.util.List;
import java.util.function.Function;

/**
 * The git client API.
 *
 * @param <T> type of the processed command output.
 */
public interface GitClient<T> {

    /**
     * Clones a repository from a github url.
     *
     * @param url the url.
     * @return either an exception if an error occurs or a list with the command output.
     */
    Either<Exception, List<String>> cloneRepository(String url);

    /**
     * Checks out a branch.
     *
     * @param repositoryDir Repository directory. Note that this is a relative path from the repositories root.
     * @param branch        The branch name.
     * @return either an exception if an error occurs or a list with the command output.
     */
    Either<Exception, List<String>> checkout(String repositoryDir, String branch);

    /**
     * Process git log.
     *
     * @param repositoryDir Repository directory. Note that this is a relative path from the repositories root.
     * @param logProcessor  The log processor function.
     * @return either an exception if an error occurs or a list with the command output.
     */
    Either<Exception, List<T>> processLog(String repositoryDir, Function<String, T> logProcessor);

    /**
     * Process git log.
     *
     * @param repositoryDir Repository directory. Note that this is a relative path from the repositories root.
     * @param logProcessor  The log processor function.
     * @param skip          The skip value.
     * @param limit         The limit value.
     * @return either an exception if an error occurs or a list with the command output.
     */
    Either<Exception, List<T>> processLog(String repositoryDir, Function<String, T> logProcessor, int skip, int limit);
}

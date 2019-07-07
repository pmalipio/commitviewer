package com.pmalipio.gitclient.api;

import io.atlassian.fugue.Either;
import io.atlassian.fugue.Unit;

import java.util.List;
import java.util.function.Function;

public interface GitClient<T> {
    Either<List<String>, Exception> cloneRepository(String url);

    /**
     *
     *
     * @param repositoryDir Repository directory. Note that this is a relative path from the repositories root.
     * @param branch
     * @return
     */
    Either<List<String>, Exception> checkout(String repositoryDir, String branch);

    Either<List<T>, Exception> processLog(String repositoryDir, Function<String, T> logProcessor);

    Either<List<T>, Exception> processLog(String repositoryDir, Function<String, T> logProcessor, int skip, int limit);
}

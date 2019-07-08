package com.pmalipio.gitclient.api;

import io.atlassian.fugue.Either;

import java.util.List;
import java.util.function.Function;

public interface GitClient<T> {
    Either<Exception, List<String>> cloneRepository(String url);

    /**
     *
     *
     * @param repositoryDir Repository directory. Note that this is a relative path from the repositories root.
     * @param branch
     * @return
     */
    Either<Exception, List<String>> checkout(String repositoryDir, String branch);

    Either<Exception, List<T>> processLog(String repositoryDir, Function<String, T> logProcessor);

    Either<Exception, List<T>> processLog(String repositoryDir, Function<String, T> logProcessor, int skip, int limit);
}

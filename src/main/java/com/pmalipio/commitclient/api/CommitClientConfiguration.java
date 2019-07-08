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

/**
 * The commit client configuration.
 */
public class CommitClientConfiguration {

    /**
     * The page size.
     */
    private final Integer pageSize;

    /**
     * Timeout used for command line commands.
     */
    private final Integer timeout;

    private CommitClientConfiguration(final Integer pageSize, final Integer timeout) {
        this.pageSize = pageSize;
        this.timeout = timeout;
    }

    /**
     * Gets the page size value.
     *
     * @return the page size value.
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Gets the timeout value.
     *
     * @return the timeout value.
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Gets the a builder to create the configuration instance.
     *
     * @return a builder for {@link CommitClientConfiguration} instances.
     */
    public static CommitClientConfigurationBuilder builder() {
        return new CommitClientConfigurationBuilder();
    }

    public static final class CommitClientConfigurationBuilder {
        private Integer pageSize = 10; // default page size is 10.
        private Integer timeout = 10; // default timeout is 10s.

        private CommitClientConfigurationBuilder() {
        }

        public CommitClientConfigurationBuilder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public CommitClientConfigurationBuilder withTimeout(Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        public CommitClientConfiguration build() {
            return new CommitClientConfiguration(pageSize, timeout);
        }
    }
}

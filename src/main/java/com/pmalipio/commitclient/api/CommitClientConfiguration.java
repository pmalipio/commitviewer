package com.pmalipio.commitclient.api;

public class CommitClientConfiguration {
    private final Integer pageSize;
    private final Integer timeout;

    private CommitClientConfiguration(final Integer pageSize, final Integer timeout) {
        this.pageSize = pageSize;
        this.timeout = timeout;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTimeout() {
        return timeout;
    }

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

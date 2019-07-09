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
package com.pmalipio.commitclient.data;

/**
 * Commit Information.
 */
public class CommitInfo {
    private String commit;
    private String author;
    private String date;
    private String message;

    public String getCommit() {
        return commit;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public static CommitInfoBuilder buiilder() {
        return new CommitInfoBuilder();
    }

    public static final class CommitInfoBuilder {
        private String commit;
        private String author;
        private String date;
        private String message;

        private CommitInfoBuilder() {
        }

        public CommitInfoBuilder withCommit(String commit) {
            this.commit = commit;
            return this;
        }

        public CommitInfoBuilder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public CommitInfoBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public CommitInfoBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public CommitInfo build() {
            CommitInfo commitInfo = new CommitInfo();
            commitInfo.author = this.author;
            commitInfo.commit = this.commit;
            commitInfo.date = this.date;
            commitInfo.message = this.message;
            return commitInfo;
        }
    }
}

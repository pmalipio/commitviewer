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

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "author",
    "committer",
    "message",
    "tree",
    "url",
    "comment_count",
    "verification"
})
public class Commit {

    @JsonProperty("author")
    private CommitAuthor author;
    @JsonProperty("committer")
    private CommitCommitter committer;
    @JsonProperty("message")
    private String message;
    @JsonProperty("tree")
    private Tree tree;
    @JsonProperty("url")
    private String url;
    @JsonProperty("comment_count")
    private Integer commentCount;
    @JsonProperty("verification")
    private Verification verification;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("author")
    public CommitAuthor getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(CommitAuthor author) {
        this.author = author;
    }

    @JsonProperty("committer")
    public CommitCommitter getCommitter() {
        return committer;
    }

    @JsonProperty("committer")
    public void setCommitter(CommitCommitter committer) {
        this.committer = committer;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("tree")
    public Tree getTree() {
        return tree;
    }

    @JsonProperty("tree")
    public void setTree(Tree tree) {
        this.tree = tree;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("comment_count")
    public Integer getCommentCount() {
        return commentCount;
    }

    @JsonProperty("comment_count")
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    @JsonProperty("verification")
    public Verification getVerification() {
        return verification;
    }

    @JsonProperty("verification")
    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

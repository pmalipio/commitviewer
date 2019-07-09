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
package com.pmalipio.commitviewer.controller;


import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import com.pmalipio.commitclient.impl.CommandLineCommitClient;
import com.pmalipio.commitclient.impl.FallbackClient;
import com.pmalipio.commitclient.impl.GithubCommitClient;
import com.pmalipio.commitviewer.configuration.CommitViewerConfiguration;
import io.atlassian.fugue.Either;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * The commit viewer rest controller.
 */
@RestController
@RequestMapping("/api")
public class CommitViewerController {

    private static final Logger logger = LoggerFactory.getLogger(CommitViewerController.class);

    private CommitClient commitClient;

    @Autowired
    public CommitViewerController(CommitViewerConfiguration configuration) {
        CommitClient primaryClient = configuration.getPrimaryClient();
        CommitClient secondaryClient = configuration.getSecondaryClient();
        this.commitClient = FallbackClient.from(primaryClient, secondaryClient);
    }

    /**
     * Gets the commits list. The arguments are kept similar to the github api for consistency.
     * Note that is a branch is not provided it will assume the master branch.
     *
     * @param user    the github user.
     * @param repname the github repository name.
     * @param sha     the branch name.
     * @param page    the page number.
     * @return a List with the commit info.
     */
    @Cacheable
    @GetMapping("/repos/{user}/{repname}/commits")
    public List<CommitInfo> getCommits(@PathVariable final String user,
                                       @PathVariable final String repname,
                                       @RequestParam(value = "sha", required = false) final String sha,
                                       @RequestParam(value = "page", required = false) final Integer page) {

        final String branch = sha == null ? "master" : sha;

        logger.info("Commits requested for Repository: {} and Branch: {}", repname ,branch);

        final Either<Exception, List<CommitInfo>> result = page != null ? commitClient.listCommits(user, repname, branch, page)
                : commitClient.listCommits(user, repname, branch);

        return result.getOrThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Could not list commits due to an internal error.", result.left().get()));
    }
}

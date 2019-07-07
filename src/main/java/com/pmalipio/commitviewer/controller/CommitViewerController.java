package com.pmalipio.commitviewer.controller;


import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.data.CommitInfo;
import com.pmalipio.commitclient.impl.CommandLineCommitClient;
import io.atlassian.fugue.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommitViewerController {

    private static final Logger logger = LoggerFactory.getLogger(CommitViewerController.class);

    private CommitClient commitClient;

    public CommitViewerController() {
        final CommitClientConfiguration configuration = CommitClientConfiguration.builder()
                .withPageSize(30)
                .withTimeout(10)
                .build();
        this.commitClient = CommandLineCommitClient.getInstance(configuration);
    }

    @GetMapping("/repos/{user}/{repname}/commits")
    public List<CommitInfo> getCommits(@PathVariable final String user,
                                       @PathVariable final String repname,
                                       @RequestParam(value = "sha", required = false) final String sha,
                                       @RequestParam(value = "page", required = false) final Integer page) {

        final String branch = sha == null ? "master" : sha;

        logger.info("Commits requested for Repository: {} and Branch: {}", repname ,branch);

        final Either<List<CommitInfo>, Exception> result = page != null ? commitClient.listCommits(user, repname, branch, page)
                : commitClient.listCommits(user, repname, branch);

        if (result.isRight()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not list commits due to an internal error.", result.right().get());
        } else {
            return result.left().get();
        }
    }
}

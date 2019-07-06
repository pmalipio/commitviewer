package com.pmalipio.commitviewer.controller;


import com.pmalipio.commitviewer.data.CommitInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommitViewerController {

    private static final Logger logger = LoggerFactory.getLogger(CommitViewerController.class);

    @GetMapping("/project/{proj}/commits")
    public List<CommitInfo> getCommits(@PathVariable(value = "proj") final String projectName,
                                       @RequestParam(required = false) final String branch) {

        logger.info("Commits requested for Project: {} and Branch: {}", projectName ,branch);

        return Collections.emptyList();

    }
}

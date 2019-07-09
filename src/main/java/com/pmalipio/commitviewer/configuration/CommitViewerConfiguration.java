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
package com.pmalipio.commitviewer.configuration;

import com.pmalipio.commitclient.api.CommitClient;
import com.pmalipio.commitclient.api.CommitClientConfiguration;
import com.pmalipio.commitclient.impl.CommandLineCommitClient;
import com.pmalipio.commitclient.impl.GithubCommitClient;
import com.pmalipio.commitviewer.exceptions.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The Commit Viewer Configuration.
 */
@Configuration
@PropertySource("classpath:commitviewer.properties")
public class CommitViewerConfiguration {

    private static final String GITHUB = "github";
    private static final String COMMAND = "command";

    @Value("${fallback.primary:github}")
    private String primaryClient;

    @Value("${fallback.scondary:command}")
    private String secondaryClient;

    @Value("${command.timeout:60}")
    private Integer commandTimeout;

    @Value("${command.pagesize:30}")
    private Integer pageSize;

    private CommitClient getClient(final String value) {
        switch (value) {
            case GITHUB  : return new GithubCommitClient();
            case COMMAND : return CommandLineCommitClient.from(
                    CommitClientConfiguration.builder().withPageSize(pageSize).withTimeout(commandTimeout).build());
            default : throw new ConfigurationException("Unsupported client");
        }
    }

    /**
     * Gets the primary client for fallback.
     * @return the primary client.
     */
    public CommitClient getPrimaryClient() {
        return getClient(primaryClient);
    }

    /**
     * Gets the scondary client for fallback.
     * @return the secondary client.
     */
    public CommitClient getSecondaryClient() {
        return getClient(secondaryClient);
    }

    /**
     * Gets the command execution timeout.
     *
     * @return the command execution timeout.
     */
    public Integer getCommandTimeout() {
        return commandTimeout;
    }

    /**
     * Get the number of results per page for the commmand line client.
     *
     * @return the number of results per page.
     */
    public Integer getPageSize() {
        return pageSize;
    }
}

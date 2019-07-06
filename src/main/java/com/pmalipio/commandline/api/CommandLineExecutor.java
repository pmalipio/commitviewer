package com.pmalipio.commandline.api;

import io.atlassian.fugue.Either;
import org.springframework.stereotype.Component;

public interface CommandLineExecutor {
    Either<Integer, Exception> runCommand(CommandLineParams commandLineParams);
}

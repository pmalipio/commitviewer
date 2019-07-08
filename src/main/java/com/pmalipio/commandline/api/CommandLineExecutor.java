package com.pmalipio.commandline.api;

import io.atlassian.fugue.Either;

import java.util.List;

public interface CommandLineExecutor<T> {
    Either<Exception, List<T>> runCommand(CommandLineParams commandLineParams);
}

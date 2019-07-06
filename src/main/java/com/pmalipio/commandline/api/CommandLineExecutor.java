package com.pmalipio.commandline.api;

import io.atlassian.fugue.Either;

import java.util.List;

public interface CommandLineExecutor<T> {
    Either<List<T>, Exception> runCommand(CommandLineParams commandLineParams);
}

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
package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Processes the command output.
 *
 * @param <T> return type for log processing.
 */
public class StreamProcessor<T> implements Supplier<List<T>> {
    private final InputStream inputStream;
    private final CommandLineParams commandLineParams;

    /**
     * Builds a {@link StreamProcessor} instance.
     *
     * @param inputStream       The input stream.
     * @param commandLineParams The command line execution parameters.
     * @return a {@link StreamProcessor} instance.
     */
    public static final StreamProcessor from(final InputStream inputStream, final CommandLineParams commandLineParams) {
        return new StreamProcessor(inputStream, commandLineParams);
    }

    private StreamProcessor(final InputStream inputStream, final CommandLineParams commandLineParams) {
        this.inputStream = inputStream;
        this.commandLineParams = commandLineParams;
    }


    @Override
    public List<T> get() {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        final Integer skip = commandLineParams.getSkip();

        final Integer limit = commandLineParams.getLimit();

        final Stream<String> lines = bufferedReader.lines();

        final Stream<String> skipStream = Optional.ofNullable(skip)
                .map(lines::skip).orElse(lines);

        final Stream<String> limitStream = Optional.ofNullable(limit)
                .map(skipStream::limit).orElse(skipStream);

        final Function<String, T> lineProcessor = commandLineParams.getLineProcessor();

        return limitStream
                .map(lineProcessor)
                .collect(Collectors.toList());
    }
}
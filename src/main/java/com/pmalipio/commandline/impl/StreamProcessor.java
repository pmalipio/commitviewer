package com.pmalipio.commandline.impl;

import com.pmalipio.commandline.api.CommandLineParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamProcessor implements Runnable {
    private final InputStream inputStream;
    private final CommandLineParams commandLineParams;

    public static final StreamProcessor from(final InputStream inputStream, final CommandLineParams commandLineParams) {
        return new StreamProcessor(inputStream, commandLineParams);
    }

    private StreamProcessor(final InputStream inputStream, final CommandLineParams commandLineParams) {
        this.inputStream = inputStream;
        this.commandLineParams = commandLineParams;
    }

    @Override
    public void run() {
        final Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream)).lines();
        final Stream<String> skipStream = Optional.ofNullable(commandLineParams.getSkip())
                .map(l -> lines.skip(l)).orElse(lines);
        final Stream<String> limitStream = Optional.ofNullable(commandLineParams.getLimit())
                .map(l -> skipStream.limit(l)).orElse(skipStream);
        limitStream
                .forEach(commandLineParams.getLineProcessor());
    }
}
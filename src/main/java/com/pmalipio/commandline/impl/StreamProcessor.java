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

public class StreamProcessor<T> implements Supplier<List<T>> {
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
    public List<T> get() {
        final Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream)).lines();
        final Stream<String> skipStream = Optional.ofNullable(commandLineParams.getSkip())
                .map(l -> lines.skip(l)).orElse(lines);
        final Stream<String> limitStream = Optional.ofNullable(commandLineParams.getLimit())
                .map(l -> skipStream.limit(l)).orElse(skipStream);

        final Function<String, T> lineProcessor = commandLineParams.getLineProcessor();

        return limitStream
                .map(lineProcessor)
                .collect(Collectors.toList());
    }
}
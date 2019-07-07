package com.pmalipio.gitclient.impl;

import com.pmalipio.commandline.api.CommandLineExecutor;
import com.pmalipio.commandline.api.CommandLineParams;
import com.pmalipio.commandline.impl.CommandLineExecutorImpl;
import com.pmalipio.gitclient.api.GitClient;
import com.pmalipio.gitclient.api.GitClientConfiguration;
import com.pmalipio.gitclient.impl.GitClientImpl;
import io.atlassian.fugue.Either;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GitClientTests {

    private GitClient gitClient;
    private CommandLineExecutor commandLineExecutor;

    @Before
    public void setup() {
        this.commandLineExecutor = CommandLineExecutorImpl.getInstance();
        this.gitClient = GitClientImpl.getInstance(GitClientConfiguration.builder().build());
    }

    @Test
    public void directoryFromUrlTest() {
        final Optional<String> dir = GitClientImpl.getDirectoryFromURl("https://github.com/pmalipio/commitviewer.git");
        assertThat(dir.isPresent());
        assertThat(dir.get()).isEqualTo("commitviewer");
    }

    @Test
    public void cloneRepositoryTest() {
        final String url = "https://github.com/pmalipio/commitviewer.git";
        final Either<List<String>, Exception> cloneResult = gitClient.cloneRepository(url);
        assertThat(cloneResult.isLeft());

        final CommandLineParams ls = CommandLineParams.builder()
                .withWorkingDirectory(System.getProperty("java.io.tmpdir") + "/" + GitClientImpl.getDirectoryFromURl(url).get())
                .withCommand("ls")
                .build();

        final Either<List<String>, Exception>  lsResult = commandLineExecutor.runCommand(ls);
        assertThat(lsResult.left().get()).size().isGreaterThan(0);
    }

    @Test
    public void checkoutTest() {
        final String url = "https://github.com/pmalipio/commitviewer.git";
        gitClient.cloneRepository(url);

        final Either<List<String>, Exception> checkout = gitClient.checkout(GitClientImpl.getDirectoryFromURl(url).get(),"master");
        assertThat(checkout.isLeft());
    }

    @Test
    public void logProcessorTest() {
        final String url = "https://github.com/pmalipio/commitviewer.git";
        final String dir = GitClientImpl.getDirectoryFromURl(url).get();
        gitClient.cloneRepository(url);
        gitClient.checkout(dir,"master");
        Either<List<String>, Exception> logResult = gitClient.processLog(dir, x -> x);
        assertThat(logResult.isLeft());
        assertThat(logResult.left().get()).size().isGreaterThan(0);

        Either<List<String>, Exception> logResult2 = gitClient.processLog(dir, x -> x, 1, 2);
        assertThat(logResult2.isLeft());
        assertThat(logResult2.left().get()).size().isEqualTo(2);
    }
}

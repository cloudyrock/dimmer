package com.github.cloudyrock.dimmer.config.util;

import com.github.cloudyrock.dimmer.DimmerLogger;
import com.github.cloudyrock.dimmer.Preconditions;
import org.eclipse.jgit.api.RebaseResult;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class GitPuller {
    private static final DimmerLogger LOG = new DimmerLogger(GitPuller.class);

    private final File gitFolder;
    private final String gitRepository;
    private final long initialDelayMilliSeconds;
    private final long periodMilliseconds;
    private final Consumer<Throwable> errorHandler;
    private final String username;
    private final String password;

    public static Builder builder() {
        return new Builder();
    }

    private GitPuller(String username,
                      String password,
                      File gitFolder,
                      String gitRepository,
                      long initialDelayMilliSeconds,
                      long periodMilliseconds,
                      Consumer<Throwable> errorHandler) {
        this.username = username;
        this.password = password;
        this.gitFolder = gitFolder;
        this.gitRepository = gitRepository;
        this.initialDelayMilliSeconds = initialDelayMilliSeconds;
        this.periodMilliseconds = periodMilliseconds;
        this.errorHandler = errorHandler;
    }

    public void run() {
        subscribe(null, null, errorHandler);
    }

    public ScheduledFuture<?> subscribeOnChange(Consumer<RebaseResult> onchangeConsumer) {
        LOG.trace("git puller subscribed onChange");
        Preconditions.checkNullOrEmpty(onchangeConsumer, "onChange consumer");
        return subscribe(null, onchangeConsumer, errorHandler);
    }

    public ScheduledFuture<?> subscribeOnAny(Consumer<RebaseResult> onAnyStatusConsumer) {
        LOG.trace("git puller subscribed onAny");
        Preconditions.checkNullOrEmpty(onAnyStatusConsumer, "onAnyStatus consumer");
        return subscribe(onAnyStatusConsumer, null, errorHandler);
    }


    public ScheduledFuture<?> subscribe(Consumer<RebaseResult> onAnyStatusConsumer,
                                        Consumer<RebaseResult> onchangeConsumer,
                                        Consumer<Throwable> onErrorConsumer) {
        return Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                new GitPullWorker(username, password, gitFolder, gitRepository, onAnyStatusConsumer, onchangeConsumer, onErrorConsumer),
                initialDelayMilliSeconds,
                periodMilliseconds,
                TimeUnit.MILLISECONDS);
    }



    public static class Builder {
        private File gitFolder;
        private String gitRepository;
        private long initialDelayMilliSeconds = 1000L;// default 1 second
        private long periodMilliseconds = 10 * 60 * 1000L;// default 10 minutes
        private Consumer<Throwable> errorHandler = null;
        private String username;
        private String password;

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder gitFolder(File gitFolder) {
            this.gitFolder = gitFolder;
            return this;
        }

        public Builder gitRepository(String gitRepository) {
            this.gitRepository = gitRepository;
            return this;
        }

        public Builder initialDelayMilliSeconds(long initialDelayMilliSeconds) {
            this.initialDelayMilliSeconds = initialDelayMilliSeconds;
            return this;
        }

        public Builder periodMilliseconds(long periodMilliseconds) {
            this.periodMilliseconds = periodMilliseconds;
            return this;
        }


        public Builder errorHandler(Consumer<Throwable> errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        public GitPuller build() {
            LOG.trace("building git puller with builder:\n {}", this);
            Preconditions.checkNullOrEmpty(gitFolder, "git folder");
            Preconditions.checkNullOrEmpty(gitRepository, "git repository");
            return new GitPuller(
                    username,
                    password,
                    gitFolder,
                    gitRepository,
                    initialDelayMilliSeconds,
                    periodMilliseconds,
                    errorHandler);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "gitFolder='" + gitFolder + '\'' +
                    ", gitRepository='" + gitRepository + '\'' +
                    ", initialDelayMilliSeconds=" + initialDelayMilliSeconds +
                    ", periodMilliseconds=" + periodMilliseconds +
                    ", errorHandler=" + errorHandler +
                    ", username='" + username + '\'' +
                    ", password='" + (password == null || "".equals(password) ? "empty" : "********" ) + '\'' +
                    '}';
        }
    }
}

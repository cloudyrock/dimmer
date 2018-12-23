package com.github.cloudyrock.dimmer.config.util;

import com.github.cloudyrock.dimmer.DimmerLogger;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RebaseResult;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.eclipse.jgit.api.RebaseResult.Status.FAST_FORWARD;
import static org.eclipse.jgit.api.RebaseResult.Status.OK;

public final class GitPuller {
    private static final DimmerLogger LOG = new DimmerLogger(GitPuller.class);

    private final File gitFolder;
    private final String gitRepository;
    private final long initialDelayMilliSeconds;
    private final long periodMilliseconds;
    private final Consumer<Throwable> errorHandler;
    private static final Set<RebaseResult.Status> statusOnChange = new HashSet<>(Arrays.asList(OK, FAST_FORWARD));

    public static Builder builder() {
        return new Builder();
    }

    private GitPuller(File gitFolder,
                      String gitRepository,
                      long initialDelayMilliSeconds,
                      long periodMilliseconds,
                      Consumer<Throwable> errorHandler) {
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
        Util.checkNullEmpty(onchangeConsumer, "onChange consumer");
        return subscribe(null, onchangeConsumer, errorHandler);
    }

    public ScheduledFuture<?> subscribeOnAny(Consumer<RebaseResult> onAnyStatusConsumer) {
        Util.checkNullEmpty(onAnyStatusConsumer, "onAnyStatus consumer");
        return subscribe(onAnyStatusConsumer, null, errorHandler);
    }


    public ScheduledFuture<?> subscribe(Consumer<RebaseResult> onAnyStatusConsumer,
                                        Consumer<RebaseResult> onchangeConsumer,
                                        Consumer<Throwable> onErrorConsumer) {


        return Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                new PullWorker(gitFolder, gitRepository, onAnyStatusConsumer, onchangeConsumer, onErrorConsumer),
                initialDelayMilliSeconds,
                periodMilliseconds,
                TimeUnit.MILLISECONDS);
    }

    private static class PullWorker implements Runnable {

        private final Consumer<RebaseResult> onAnyStatusConsumer;
        private final Consumer<RebaseResult> onchangeConsumer;
        private final Consumer<Throwable> onErrorConsumer;
        private final File gitFolder;
        private final String gitRepository;

        private PullWorker(File gitFolder,
                           String gitRepository,
                           Consumer<RebaseResult> onAnyStatusConsumer,
                           Consumer<RebaseResult> onchangeConsumer,
                           Consumer<Throwable> onErrorConsumer) {
            this.gitRepository = gitRepository;
            this.gitFolder = gitFolder;
            this.onAnyStatusConsumer = onAnyStatusConsumer;
            this.onchangeConsumer = onchangeConsumer;
            this.onErrorConsumer = onErrorConsumer;

        }

        @Override
        public void run() {
            synchronized (this) {
                boolean needResetting = false;
                try {
                    final RebaseResult rebaseResult = getGitRepo(gitFolder, gitRepository)
                            .pull()
                            .setRebase(Boolean.TRUE)
                            .call()
                            .getRebaseResult();

                    if (onAnyStatusConsumer != null) {
                        onAnyStatusConsumer.accept(rebaseResult);
                    }

                    if (rebaseResult.getStatus().isSuccessful()) {
                        if (onchangeConsumer != null && statusOnChange.contains(rebaseResult.getStatus())) {
                            onchangeConsumer.accept(rebaseResult);
                        }
                    } else {
                        LOG.warn("Pull not successful({})", rebaseResult.getStatus());
                        needResetting = true;
                    }

                } catch (Throwable ex) {
                    LOG.error("ERROR pulling: ", ex);
                    if (onErrorConsumer != null) {
                        onErrorConsumer.accept(ex);
                        needResetting = true;

                    }
                }
                if(needResetting) {
                    deleteDirectory(gitFolder);
                }
            }

        }


        private static Git getGitRepo(File gitFolder, String gitRepository) {
            try {
                return gitFolder.exists()
                        ? Git.open(gitFolder)
                        : Git.cloneRepository().setURI(gitRepository).setDirectory(gitFolder).call();

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        private static void deleteDirectory(File gitFolder) {
            LOG.debug("Deleting directory: {}", gitFolder.getAbsolutePath());
            try {
                FileUtils.deleteDirectory(gitFolder);
                LOG.debug("Deleted successfully directory: {}", gitFolder.getAbsolutePath());
            } catch (Exception e) {
                LOG.error("File couldn't be deleted(in order to be reset", e);
            }
        }
    }


    public static class Builder {
        private String gitFolder;
        private String gitRepository;
        private long initialDelayMilliSeconds = 1000L;// default 1 second
        private long periodMilliseconds = 10 * 60 * 1000L;// default 10 minutes
        private Consumer<Throwable> errorHandler = null;

        private Builder() {
        }

        public Builder gitFolder(String gitFolder) {
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
            Util.checkNullEmpty(gitFolder, "git folder");
            Util.checkNullEmpty(gitRepository, "git repository");
            return new GitPuller(
                    new File(gitFolder),
                    gitRepository,
                    initialDelayMilliSeconds,
                    periodMilliseconds,
                    errorHandler);
        }
    }
}

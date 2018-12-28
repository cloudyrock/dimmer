package com.github.cloudyrock.dimmer.config.util;

import com.github.cloudyrock.dimmer.DimmerLogger;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static org.eclipse.jgit.api.RebaseResult.Status.FAST_FORWARD;
import static org.eclipse.jgit.api.RebaseResult.Status.OK;

class GitPullWorker implements Runnable {

    private static final DimmerLogger LOG = new DimmerLogger(GitPuller.class);
    private static final Set<RebaseResult.Status> statusOnChange = new HashSet<>(Arrays.asList(OK, FAST_FORWARD));


    private final Consumer<RebaseResult> onAnyStatusConsumer;
    private final Consumer<RebaseResult> onchangeConsumer;
    private final Consumer<Throwable> onErrorConsumer;
    private final File gitFolder;
    private final String gitRepository;
    private final CredentialsProvider credentialsProvider;

    GitPullWorker(String username,
                  String password,
                  File gitFolder,
                  String gitRepository,
                  Consumer<RebaseResult> onAnyStatusConsumer,
                  Consumer<RebaseResult> onchangeConsumer,
                  Consumer<Throwable> onErrorConsumer) {
        this.gitRepository = gitRepository;
        this.gitFolder = gitFolder;
        this.onAnyStatusConsumer = onAnyStatusConsumer;
        this.onchangeConsumer = onchangeConsumer;
        this.onErrorConsumer = onErrorConsumer;
        this.credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
    }

    @Override
    public void run() {
        LOG.trace("Running git pull worker");
        synchronized (this) {
            boolean needResetting = false;
            try {
                LOG.trace("git rebasing");
                final RebaseResult rebaseResult = getGitRepo(credentialsProvider, gitFolder, gitRepository)
                        .pull()
                        .setRebase(Boolean.TRUE)
                        .call()
                        .getRebaseResult();
                LOG.trace("git rebase status {}", rebaseResult);

                if (onAnyStatusConsumer != null) {
                    LOG.debug("git pull worker invoking onAny consumer");
                    onAnyStatusConsumer.accept(rebaseResult);
                }

                if (rebaseResult.getStatus().isSuccessful()) {
                    if (onchangeConsumer != null && statusOnChange.contains(rebaseResult.getStatus())) {
                        LOG.debug("git pull worker invoking onChange consumer");
                        onchangeConsumer.accept(rebaseResult);
                    }
                } else {
                    LOG.warn("Pull not successful({})", rebaseResult.getStatus());
                    needResetting = true;
                }

            } catch (Throwable ex) {
                LOG.error("ERROR pulling: ", ex);
                if (onErrorConsumer != null) {
                    LOG.debug("git pull worker invoking onError consumer");
                    onErrorConsumer.accept(ex);
                }
                needResetting = true;
            }
            if (needResetting) {
                LOG.debug("Deleting directory: {}", gitFolder.getAbsolutePath());
                deleteDirectory(gitFolder);
            }
        }

    }


    private static Git getGitRepo(CredentialsProvider credentialsProvider, File gitFolder, String gitRepository) {
        try {
            final Git git;
            if(gitFolder.exists()) {
                LOG.trace("folder already exists, trying git open");
                git = Git.open(gitFolder);
                LOG.trace("Git open executed successfully");
            } else {
                LOG.trace("folder not created, trying git cloning");
                git = Git.cloneRepository()
                        .setURI(gitRepository)
                        .setCredentialsProvider(credentialsProvider)
                        .setDirectory(gitFolder)
                        .call();
                LOG.trace("Git cloning executed successfully");
            }
            return git;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void deleteDirectory(File gitFolder) {
        try {
            FileUtils.deleteDirectory(gitFolder);
            LOG.debug("Deleted successfully directory: {}", gitFolder.getAbsolutePath());
        } catch (Exception e) {
            LOG.error("File couldn't be deleted(in order to be reset", e);
        }
    }
}

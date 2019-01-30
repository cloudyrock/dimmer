package com.github.cloudyrock.dimmer.config.util;

import com.github.cloudyrock.dimmer.DimmerLogger;
import com.github.cloudyrock.dimmer.Preconditions;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.IoScheduler;

import java.io.File;
import java.nio.file.*;
import java.util.function.Consumer;

public final class FileWatcher {
    private static final DimmerLogger LOG = new DimmerLogger(FileWatcher.class);


    private final String directoryLocation;
    private final String fileName;
    private final boolean onFileCreation;
    private final boolean onFileEdition;
    private final boolean onFileDeletion;

    public static Builder builder() {
        return new Builder();
    }

    private FileWatcher(String directoryLocation,
                        String fileName,
                        boolean onFileCreation,
                        boolean onFileEdition,
                        boolean onFileDeletion) {
        this.directoryLocation = directoryLocation;
        this.fileName = fileName;
        this.onFileCreation = onFileCreation;
        this.onFileEdition = onFileEdition;
        this.onFileDeletion = onFileDeletion;
    }

    public Disposable subscribeAsync(Consumer<FileChange> successObserver, Consumer<Throwable> errorObserver) {
        LOG.trace("file watcher subscribed async");
        return subscribe(successObserver, errorObserver, new IoScheduler());
    }

    private Disposable subscribe(Consumer<FileChange> successObserver,
                                 Consumer<Throwable> errorObserver,
                                 Scheduler scheduler) {
        LOG.trace("file watcher subscribed async with scheduler: {}", scheduler.getClass().getSimpleName());
        return Flowable.create(getFlowable(directoryLocation, fileName, getEventKinds()), BackpressureStrategy.LATEST)
                .subscribeOn(scheduler)
                .subscribe(successObserver::accept, errorObserver::accept);

    }

    private static FlowableOnSubscribe<FileChange> getFlowable(String directoryLocation,
                                                               String fileName,
                                                               WatchEvent.Kind[] eventKinds) {
        return emitter -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                final String filePath = String.format(directoryLocation + (directoryLocation.endsWith("/")
                        ? "%s" : "/%s"), fileName);
                Paths.get(directoryLocation).register(watchService, eventKinds);
                LOG.trace("file watcher initialized and registered");
                while (!emitter.isCancelled()) {
                    try {

                        LOG.trace("file watcher listening for changes in file: {}/{}", directoryLocation, fileName);
                        WatchKey key = watchService.take();
                        LOG.trace("file watcher. New event received: {}", key);
                        if (emitter.isCancelled()) {
                            LOG.debug("file watcher cancelled");
                            break;
                        }
                        for (WatchEvent<?> event : key.pollEvents()) {
                            final String fileAffected = event.context().toString();
                            if(fileName.equals(fileAffected)) {
                                LOG.debug("file watcher. New event received: {} and file {}/{} affected",
                                        key, directoryLocation, fileName);
                                emitter.onNext(new FileChange(filePath));
                            } else {
                                LOG.trace("file watcher. New event received: {}, but file not affected", key);
                            }
                        }
                        key.reset();
                    } catch (Throwable th) {
                        LOG.error("file watcher ERROR in loop: ", th);
                        emitter.onError(th);
                    }
                }
            } catch (Throwable th) {
                LOG.error("file watcher ERROR before loop: ", th);
                if(!emitter.isCancelled()) {
                    emitter.onError(th);
                }
            }
            emitter.onComplete();
        };
    }

    private WatchEvent.Kind[] getEventKinds() {
        final int size = getInt(onFileCreation) + getInt(onFileEdition) + getInt(onFileDeletion);
        final WatchEvent.Kind[] eventKinds = new WatchEvent.Kind[size];
        int index = 0;
        if (onFileCreation) {
            eventKinds[index++] = StandardWatchEventKinds.ENTRY_CREATE;
        }
        if (onFileEdition) {
            eventKinds[index++] = StandardWatchEventKinds.ENTRY_MODIFY;
        }
        if (onFileDeletion) {
            eventKinds[index] = StandardWatchEventKinds.ENTRY_DELETE;
        }
        return eventKinds;
    }

    private int getInt(boolean bool) {
        return bool ? 1 : 0;
    }


    public static class Builder {
        private String fileLocation;
        private boolean onFileCreation;
        private boolean onFileEdition;
        private boolean onFileDeletion;

        private Builder() {
        }

        public Builder fileLocation(String dirPath) {
            this.fileLocation = dirPath;
            return this;
        }

        public Builder onFileCreation() {
            this.onFileCreation = true;
            return this;
        }

        public Builder onFileEdition() {
            this.onFileEdition = true;
            return this;
        }

        public Builder onFileDeletion() {
            this.onFileDeletion = true;
            return this;
        }

        public FileWatcher build() {
            LOG.trace("building file watcher with builder:\n {}", this);
            Preconditions.checkNullOrEmpty(fileLocation, "fileLocation");
            final int lastIndex = fileLocation.lastIndexOf("/");
            final String fileName = fileLocation.substring(lastIndex+1);
            final String dirLocation = fileLocation.substring(0, lastIndex);
            return new FileWatcher(dirLocation, fileName, onFileCreation, onFileEdition, onFileDeletion);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "fileLocation='" + fileLocation + '\'' +
                    ", onFileCreation=" + onFileCreation +
                    ", onFileEdition=" + onFileEdition +
                    ", onFileDeletion=" + onFileDeletion +
                    '}';
        }
    }
}

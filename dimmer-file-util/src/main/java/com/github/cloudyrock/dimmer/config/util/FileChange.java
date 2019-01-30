package com.github.cloudyrock.dimmer.config.util;

import java.util.Objects;

public final class FileChange {

    private final String location;

    public FileChange(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FileChange that = (FileChange) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return "FileChange{location='" + location + "'}";
    }
}

package com.github.cloudyrock.dimmer.reader;

public interface DimmerConfigReader {
    DimmerConfig fromProperties(String filePath);
}

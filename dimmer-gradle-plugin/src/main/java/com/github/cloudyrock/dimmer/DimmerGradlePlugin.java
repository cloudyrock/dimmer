package com.github.cloudyrock.dimmer;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimmerGradlePlugin implements Plugin<Project> {

    public static final Logger logger = LoggerFactory.getLogger(DimmerGradlePlugin.class);

    public void apply(Project project) {

        logger.info("EXECUTING PLUGIN DEF");

        project.getTasks().create("test", task -> {
            System.out.println("HI from task");
        });

    }
}
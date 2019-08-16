package com.github.cloudyrock.dimmer;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class TestTask extends DefaultTask {
    @TaskAction
    public void dimmerTask() {
        System.out.println("Hello from MyJavaTask");
    }
}

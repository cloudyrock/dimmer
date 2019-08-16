package com.github.cloudyrock.dimmer;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.jarjar.org.apache.commons.io.output.StringBuilderWriter;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

public class TestDimmerGradlePlugin {

    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File buildFile;

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
        final StringBuilderWriter stringBuilderWriter = new StringBuilderWriter();
        stringBuilderWriter.append("plugins{id}");
    }

//TODO Change scope of tests: add a test that actually makes a call to the dimmer plugin during execution--> needs a bit more of thought
    @Test
    public void whenPluginIsApplied_thenProjectShouldHavePlugin() {

        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("dimmer");

        assertTrue(project.getPluginManager().hasPlugin("dimmer"));
        assertNotNull(project.getTasks().getByName("test"));

    }

    @Test
    public void whenExecutingTask_shouldPrintOutMessage() {

        final BuildResult result = GradleRunner.create().withPluginClasspath()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("test")
                .build();

        assertThat(result.getOutput(), is("HI from task"));
    }


}

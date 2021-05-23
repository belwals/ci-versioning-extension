package com.sab.maven.model;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = AutoVersioningMojo.GOAL, defaultPhase = LifecyclePhase.CLEAN, threadSafe = true)
public class AutoVersioningMojo extends AbstractMojo {
    static final String GOAL = "versioning";

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public synchronized void execute() {
        getLog().info("ci-versioning-extension");
    }
}

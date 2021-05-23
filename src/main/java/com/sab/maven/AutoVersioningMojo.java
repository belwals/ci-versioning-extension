package com.sab.maven;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.Enumeration;
import java.util.Properties;

@Mojo(name = AutoVersioningMojo.GOAL, defaultPhase = LifecyclePhase.INITIALIZE,
        threadSafe = true)
public class AutoVersioningMojo extends AbstractMojo {

    static final String GOAL = "auto-versioning";

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("*********PLUGIN CALLED***************");
        Properties properties = project.getProperties();

        Enumeration<Object> keys = properties.keys();

        System.out.println("Found the properties: " + properties.size());
        while (keys.hasMoreElements()) {
            System.out.println("Iterating first time for the keys: " + keys);
            getLog().info("Iterating first time for the keys: " + keys);
            System.out.println(keys.nextElement());
        }

        Artifact artifactId = project.getArtifact();
        String version = project.getVersion();
        System.out.println(artifactId);
        System.out.println(version);
    }
}

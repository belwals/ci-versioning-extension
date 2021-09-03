package com.sab.maven.model;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.sab.maven.git.GitBranchStatus;
import com.sab.maven.git.GitUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.building.Source;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelProcessor;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.session.scope.internal.SessionScope;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;

@Slf4j
@Named("core-default")
@Singleton
@Typed(ModelProcessor.class)
public class CiVersioningModelProcessor extends DefaultModelProcessor {

    private final Set<File> projectModules = new HashSet<>();
    private final String MAVEN_CONFIG = "maven.config";

    private MavenSession mavenSession;
    private GitBranchStatus gitBranchStatus;
    private volatile boolean initialized = false;

    @Inject
    private SessionScope sessionScope;
    private static final String VERSION_SEPARATOR = ".";

    @Override
    public Model read(File input, Map<String, ?> options) throws IOException {
        final Model projectModel = super.read(input, options);
        return processModel(projectModel, options);
    }

    @Override
    public Model read(Reader input, Map<String, ?> options) throws IOException {
        final Model projectModel = super.read(input, options);
        return processModel(projectModel, options);
    }

    @Override
    public Model read(InputStream input, Map<String, ?> options) throws IOException {
        final Model projectModel = super.read(input, options);
        return processModel(projectModel, options);
    }

    public Model processModel(Model projectModel, Map<String, ?> options) throws IOException {
        log.debug("Processing project model: {}", projectModel.getArtifactId());
        final Source pomSource = (Source) options.get(ModelProcessor.SOURCE);
        if (pomSource != null) {
            File file = new File(pomSource.getLocation());
            projectModel.setPomFile(file);
        } else {
            return projectModel;
        }
        if (!initialized) {
            log.debug("Initializing the plugin");
            init(projectModel);
            initialized = true;
        }

        String newDynamicVersion = generateBuildVersion(gitBranchStatus);

        //In case of no git repo initialize then we ll ignore overriding the dynamic version
        if(newDynamicVersion.length() == 0) {
            return projectModel;
        }
        Properties existingMavenProperties = projectModel.getProperties();
        existingMavenProperties.setProperty("revision", newDynamicVersion);
        return projectModel;
    }

    private String generateBuildVersion(GitBranchStatus gitSituation) {
        String ciGeneratedVersion = "";
        if (Objects.nonNull(gitSituation)) {
            String branchName = gitSituation.getBranchName();
            if ("master".equals(branchName) || "main".equals(branchName)) {
                ciGeneratedVersion = gitSituation.getCommitTimestamp() + VERSION_SEPARATOR + gitSituation.getCommitHash();
            } else {
                ciGeneratedVersion = gitSituation.getCommitTimestamp() + VERSION_SEPARATOR + branchName + VERSION_SEPARATOR + gitSituation.getCommitHash();
            }
        }
        return ciGeneratedVersion;
    }

    private void init(Model projectModel) throws IOException {

        try {
            mavenSession = sessionScope.scope(Key.get(MavenSession.class), null).get();
        } catch (OutOfScopeException ex) {
            return;
        }
        File executionRootDirectory = new File(mavenSession.getRequest().getBaseDirectory());

        gitBranchStatus = GitUtil.situation(executionRootDirectory);
        log.info("Getting git situations:{}", gitBranchStatus);
        if (gitBranchStatus == null) {
            return;
        }

        projectModules.add(projectModel.getPomFile());
    }
}

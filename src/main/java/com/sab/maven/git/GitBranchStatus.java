package com.sab.maven.git;

import lombok.ToString;

import static java.util.Objects.requireNonNull;

@ToString
public class GitBranchStatus {

    private final String commitHash;
    private final String commitTimestamp;
    private final String branchName;

    public GitBranchStatus(String commitHash,
                           String commitTimestamp,
                           String branchName) {

        this.commitHash = requireNonNull(commitHash);
        this.commitTimestamp = commitTimestamp;
        this.branchName = branchName;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public String getCommitTimestamp() {
        return commitTimestamp;
    }

    public String getBranchName() {
        return branchName;
    }
}
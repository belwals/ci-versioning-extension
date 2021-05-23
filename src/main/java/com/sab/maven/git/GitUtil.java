package com.sab.maven.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.eclipse.jgit.lib.Constants.HEAD;

@Slf4j
public final class GitUtil {

    private static final String NO_COMMIT = "00000";
    private static final String VERSION_DATE_FORMAT = "yyMMddHHmmss";
    private static final String NO_COMMIT_TIMESTAMP = "0";

    public static String getBranchName(Repository repository) throws IOException {
        String branch = repository.getBranch();
        if (ObjectId.isId(branch)) {
            return null;
        }
        return branch;
    }

    public static String revParse(Repository repository, String revstr) throws IOException {
        ObjectId rev = repository.resolve(revstr);
        if (rev == null) {
            return NO_COMMIT;
        }
        return rev.getName().length() >= 6 ? rev.getName().substring(0, 6) : rev.getName();
    }

    public static String getCommitTimestamp(Repository repository, String revstr) throws IOException {
        ObjectId rev = repository.resolve(revstr);

        if (rev == null) {
            return NO_COMMIT_TIMESTAMP;
        }
        LocalDateTime triggerTime =
                LocalDateTime.ofEpochSecond(repository.parseCommit(rev).getCommitTime(), 0, OffsetDateTime.now().getOffset());
        return triggerTime.format(DateTimeFormatter.ofPattern(VERSION_DATE_FORMAT));
    }

    public static GitBranchStatus situation(File directory) throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder().findGitDir(directory);
        if (repositoryBuilder.getGitDir() == null) {
            return null;
        }
        try (Repository repository = repositoryBuilder.build()) {
            String headCommit = GitUtil.revParse(repository, HEAD);
            String headCommitTimestamp = GitUtil.getCommitTimestamp(repository, HEAD);
            String headBranch = GitUtil.getBranchName(repository);
            return new GitBranchStatus(headCommit, headCommitTimestamp, headBranch);
        }
    }
}
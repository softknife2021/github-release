package com.softknife.release.github.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kohsuke.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author amatsaylo on 2019-05-10
 * @project release-notes
 */
@Component
public class GitHelper {

    @Resource
    private GitHub gitHub;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //TODO:WORK IN PROGRESS
    protected GHCompare compareBranches(String orgName, String repoName, String sourceBranch, String targetBranch) throws IOException {
        GHBranch ghBranchSource = gitHub.getOrganization(orgName).getRepository(repoName).getBranch(sourceBranch);
        GHBranch ghBranchTarget = gitHub.getOrganization(orgName).getRepository(repoName).getBranch(targetBranch);
        return gitHub.getOrganization(orgName).getRepository(repoName).getCompare(ghBranchSource, ghBranchTarget);

    }

    private Date generateDateInThePast(int daysBack, int hoursBack){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(daysBack > 0){
            calendar.add(Calendar.DATE, - daysBack);
        }
        if(hoursBack > 0){
            calendar.add(Calendar.HOUR, - hoursBack);
        }
        return calendar.getTime();
    }

    protected Optional<List<GHCommit>> getCommitsSince(String orgName, String githubRepo, String branch, int daysBack, int hoursBack) {
        try {
            return Optional.of(gitHub.getOrganization(orgName).getRepository(githubRepo).queryCommits().since(generateDateInThePast(daysBack,hoursBack))
                    .from(branch).list().toList());
        } catch (Throwable e) {
            logger.warn("Failed to get commits from {} branch in the repo {} because {}", branch, githubRepo, e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    protected Optional<List<GHCommit>> getCommitsBetweenDates(String orgName, String githubRepo, String branch, Date sinceDate, Date untilDate) {
        try {
            return Optional.of(gitHub.getOrganization(orgName).getRepository(githubRepo).queryCommits()
                    .since(sinceDate).until(untilDate)
                    .from(branch).list().toList());
        } catch (Throwable e) {
            logger.warn("Failed to get commits from {} branch in the repo {} because {}", branch, githubRepo, e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    protected Optional<GHRepository> getRepository(String orgName, String repoName) {
        try {
            return Optional.of(gitHub.getOrganization(orgName).getRepository(repoName));
        } catch (IOException e) {
            logger.error("Repo does not exist:{}", repoName);
            e.printStackTrace();
        }
        return Optional.empty();
    }


    protected Optional<List<GHContent>> getGHContent(String orgName, String repoName, String fileName, String branchName) throws IOException {
        return Optional.ofNullable(gitHub.getOrganization(orgName)
                .getRepository(repoName)
                .getDirectoryContent(fileName, branchName));
    }

}

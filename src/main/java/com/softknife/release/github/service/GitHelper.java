package com.softknife.release.github.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softknife.release.github.model.image.ImageMetaData;
import com.softknife.release.util.AppHelper;
import org.kohsuke.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * @author amatsaylo on 2019-05-10
 * @project release-notes
 */
@Component
public class GitHelper {

    @Resource
    private GitHub gitHub;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private AppHelper appHelper;

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


    protected Map<String, ImageMetaData> applyModuleFilter(List<GHContent> ghContents, List<String> appNames) {

        Map<String, ImageMetaData> matchedApplications = new TreeMap();
        for (GHContent ghContent : ghContents) {
            String ghContentName = ghContent.getName().split("\\.")[0];
            for (String appName : appNames) {
                if (ghContentName.equalsIgnoreCase(appName)) {
                    try {
                        logger.info("Found image metadata for appName :{}", appName);
                        matchedApplications.put(appName, objectMapper.readValue(appHelper.ymlToJson(ghContent.getContent()), ImageMetaData.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return matchedApplications;
    }

    protected Optional<List<GHContent>> getGHContent(String orgName, String repoName, String fileName, String branchName) throws IOException {
        return Optional.ofNullable(gitHub.getOrganization(orgName)
                .getRepository(repoName)
                .getDirectoryContent(fileName, branchName));
    }

}

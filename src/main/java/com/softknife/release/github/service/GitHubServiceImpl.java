package com.softknife.release.github.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restbusters.data.templating.TemplateManager;
import com.restbusters.exception.RecordNotFound;
import com.softknife.release.github.model.report.GitCommit;
import com.softknife.release.github.model.request.CommitsSince;
import com.softknife.release.github.model.request.CommitsSinceUntil;
import com.softknife.release.github.model.request.CompareBranches;
import com.softknife.release.github.model.response.BranchCompare;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * @author amatsaylo on 2019-05-10
 * @project release-notes
 */
@Service("gitHubSrv")
public class GitHubServiceImpl {

    @Resource
    GitHelper gitHelper;
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private GitReportHelper gitReportHelper;

    @Resource
    private TemplateManager templateManager;


    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public String getCommitsFromBranch(CommitsSince commitsSince) throws IOException {
        Map<String, List<GitCommit>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(commitsSince.getRepoNames()) && commitsSince.getRepoNames().size() > 0) {
            //TODO:throw an exception no repos
        }
        for (String repo : commitsSince.getRepoNames()) {
            Optional<List<GHCommit>> listOptional = gitHelper.getCommitsSince(commitsSince.getOrgName(), repo, commitsSince.getBranchName(),
                    commitsSince.getDaysBack(), commitsSince.getHoursBack());
            if (listOptional.isPresent()) {
                logger.info("Found commits for repo {}", repo);
                List<GitCommit> gitCommits = new ArrayList<>();
                for (GHCommit ghCommit : listOptional.get()) {
                    if (excludeBasedOnPath(ghCommit, commitsSince.getPathExclusion())) {
                        if (!StringUtils.startsWith(ghCommit.getCommitShortInfo().getMessage(), "Merge")) {
                            gitCommits.add(gitReportHelper.buildGitCommit(ghCommit));
                        }
                    }
                }
                result.put(repo, gitCommits);
            }
        }
        try {
            if (commitsSince.getReportType() != null) {
                return this.templateManager.processTemplateWithJsonInput(commitsSince.getReportType().getName(), "0.1", this.objectMapper.writeValueAsString(result));
            }
            return objectMapper.writeValueAsString(result);
        } catch (RecordNotFound e) {
            throw new RuntimeException(e);
        } catch (freemarker.template.TemplateException e) {
            throw new RuntimeException(e);
        }

    }

    public String getCommitsInTimeRange(CommitsSinceUntil commitsSinceUntil) throws IOException {
        Map<String, List<GitCommit>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(commitsSinceUntil.getRepoNames()) && commitsSinceUntil.getRepoNames().size() > 0) {
            //TODO:throw an exception no repos
        }
        for (String repo : commitsSinceUntil.getRepoNames()) {

            Optional<List<GHCommit>> listOptional = gitHelper.getCommitsBetweenDates(commitsSinceUntil.getOrgName(), repo, commitsSinceUntil.getBranchName(),
                    new Date(commitsSinceUntil.getStartDate()), new Date(commitsSinceUntil.getUntilDate()));
            if (listOptional.isPresent()) {
                logger.info("Found commits for repo {}", repo);
                List<GitCommit> gitCommits = new ArrayList<>();
                for (GHCommit ghCommit : listOptional.get()) {
                    if (excludeBasedOnPath(ghCommit, commitsSinceUntil.getPathExclusion())) {
                        if (!StringUtils.startsWith(ghCommit.getCommitShortInfo().getMessage(), "Merge")) {
                            gitCommits.add(gitReportHelper.buildGitCommit(ghCommit));
                        }
                    }
                }
                result.put(repo, gitCommits);
            }
        }
        try {
            if (commitsSinceUntil.getReportType() != null) {
                return this.templateManager.processTemplateWithJsonInput(commitsSinceUntil.getReportType().getName(), "0.1", this.objectMapper.writeValueAsString(result));
            }
            return objectMapper.writeValueAsString(result);
        } catch (RecordNotFound e) {
            throw new RuntimeException(e);
        } catch (freemarker.template.TemplateException e) {
            throw new RuntimeException(e);
        }

    }


    private boolean excludeBasedOnPath(GHCommit ghCommit, List<String> pathExclusion) throws IOException {
        boolean result = false;
        int fileMatchCounter = 0;
        int fileActualSize = ghCommit.getFiles().size();
        for (GHCommit.File file : ghCommit.getFiles()) {
            for(String path : pathExclusion){
                if (file.getFileName().contains(path)) {
                    fileMatchCounter++;
                }
            }
        }
        if (fileMatchCounter == fileActualSize) {
            return false;
        }
        return !result;
    }

    public List<BranchCompare> compareBranches(CompareBranches cp) {
        List<BranchCompare> branchCompareList = new ArrayList<>();
        for(String repo : cp.getRepoNames()){
            try {
                BranchCompare branchCompare = new BranchCompare();
                GHCompare ghCompare = this.gitHelper.compareBranches(cp.getOrgName(), repo, cp.getSourceBranch(), cp.getTargetBranch());
                branchCompare.setRepoName(repo);
                branchCompare.setTargetBranch(cp.getTargetBranch());
                branchCompare.setSourceBranch(cp.getSourceBranch());
                branchCompare.setAheadBy(ghCompare.getAheadBy());
                branchCompare.setBehind_by(ghCompare.getBehindBy());
                branchCompare.setStatus(ghCompare.getStatus().toString());
                branchCompareList.add(branchCompare);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return branchCompareList;
    }
}

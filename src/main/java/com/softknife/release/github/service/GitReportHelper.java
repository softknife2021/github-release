package com.softknife.release.github.service;

import com.restbusters.exception.RecordNotFound;
import com.softknife.release.github.model.report.GitCommit;
import com.softknife.release.github.model.report.GitReport;
import com.softknife.release.jira.service.JiraServiceImpl;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author amatsaylo on 2019-08-01
 * @project release-notes
 */
@Component
public class GitReportHelper {

    @Resource
    private GitHub gitHub;

    @Resource
    private JiraServiceImpl jiraService;

    protected GitReport buildGitReport(String orgName, String repoName, Map<String, List<GHCommit>> mappedReposFromApps, int startIndex, int endIndex) throws IOException {
        GitReport gitReport = new GitReport();
        gitReport.setOrgName(orgName);
        List<GitCommit> selectedCommits = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            selectedCommits.add(buildGitCommit(mappedReposFromApps.get(repoName).get(i)));
        }
        gitReport.setGitCommits(selectedCommits);
        gitReport.setTotalCommits(String.valueOf(endIndex - startIndex + 1));
        return gitReport;
    }

    protected GitCommit buildGitCommit(GHCommit ghCommit) throws IOException {
        GitCommit commit = new GitCommit();
        Optional<String> jiraId = extractFromString(ghCommit.getCommitShortInfo().getMessage());
        if (jiraId.isPresent()) {
            commit.setJiraId(extractFromString(ghCommit.getCommitShortInfo().getMessage()).get());
            try {
                commit.setJiraStatus(jiraService.getJiraStatus(jiraId.get()));
            } catch (RecordNotFound e) {
                throw new RuntimeException(e);
            }
        }
        if(ghCommit.getAuthor() == null){
            String author = "Author has not been set";
            commit.setAuthor(author);
            commit.setAuthorIcon(author);
        }
        else {
            commit.setAuthor(ghCommit.getAuthor().getLogin());
            commit.setAuthorIcon(ghCommit.getCommitter().getAvatarUrl());
        }
        commit.setGitSha(ghCommit.getSHA1());
        commit.setCommitMessage(ghCommit.getCommitShortInfo().getMessage());
        commit.setCommittedDate(ghCommit.getCommitDate().toString());
        commit.setCommitUrl(ghCommit.getHtmlUrl().toExternalForm());
        List<String> effectedFile = new ArrayList<>();
        for (GHCommit.File file : ghCommit.getFiles()) {
            int filePosition = file.getFileName().split("/").length - 1;
            String fileName = file.getFileName().split("/")[filePosition];
            effectedFile.add(fileName);
            commit.setEffectedFile(Collections.singletonList(effectedFile).toString().replaceAll("\\[\\[", "")
                    .replaceAll("\\]\\]", ""));
        }
        return commit;
    }

    private Optional<String> extractFromString(String stringToSearch) {

        Pattern p = Pattern.compile("[A-Z]{1,4}-[0-9]{1,4}");   // the pattern to search for
        Matcher m = p.matcher(stringToSearch);
        // if we find a match, get the group
        if (m.find()) {
            // we're only looking for one group, so get it
            String theGroup = m.group(0);

            // print the group out for verification
            System.out.format("'%s'\n", theGroup);
            return Optional.of(theGroup);
        }
        return Optional.empty();
    }


}

package com.softknife.release.util;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.Attachment;
import com.softknife.release.config.AppProp;
import com.softknife.release.github.model.report.GitCommit;
import com.softknife.release.github.model.report.GitReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amatsaylo on 2019-07-17
 * @project release-notes
 */
@Service
public class SlackUtil {

    @Resource
    private Slack slack;
    @Resource
    private AppProp appProp;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private void postToSlack(com.github.seratch.jslack.api.webhook.Payload payload) {
        try {
            slack.send(appProp.getSlackWebHook(), (com.github.seratch.jslack.api.webhook.Payload) payload);
        } catch (IOException e) {
            logger.error("Failed to send message to Slack : ", e.getMessage());
        }
    }

    public void sendMessage(GitReport gitReport) {
        List<Attachment> attachments = setAttachments(gitReport);
        com.github.seratch.jslack.api.webhook.Payload payload = com.github.seratch.jslack.api.webhook.Payload.builder()
                .attachments(attachments)
                .build();
        postToSlack(payload);

    }

    public List<Attachment> setAttachments(GitReport gitReport) {
        List<Attachment> attachments = new ArrayList<>();
        Attachment summaryAttachment = Attachment.builder()
                .title("Deployment: " + gitReport.getRepoName())
                .text(buildSummaryAttachement(gitReport))
                .color("#1F45FC")
                .build();
        attachments.add(summaryAttachment);
        buildAttachment(gitReport, attachments);
        return attachments;

    }

    private String buildSummaryAttachement(GitReport gitReport) {
        final StringBuffer sb = new StringBuffer("*GitHub Repo:* " + gitReport.getRepoName()).append('\n');
        sb.append("*Total commits:* ").append(gitReport.getTotalCommits()).append('\n');
        return sb.toString();
    }


    private void buildAttachment(GitReport gitReport, List<Attachment> attachments) {
        Attachment attachment;
        for (GitCommit gitCommit : gitReport.getGitCommits()) {
            attachment = Attachment.builder()
                    .color("#32CD32")
                    .title(gitCommit.getJiraId() + " in : " + gitCommit.getJiraStatus())
                    .titleLink(gitCommit.getJiraId())
                    .authorName(gitCommit.getAuthor())
                    .authorIcon(gitCommit.getAuthorIcon())
                    .authorLink(gitCommit.getCommitUrl())
                    .text(buildText(gitCommit))
                    .build();
            attachments.add(attachment);
        }
    }

    private String buildText(GitCommit gitCommit) {
        final StringBuffer sb = new StringBuffer();
        return sb.append(gitCommit.getCommittedDate() + '\n')
                .append(gitCommit.getGitSha() + '\n')
                .append(gitCommit.getCommitMessage()).toString();

    }

}


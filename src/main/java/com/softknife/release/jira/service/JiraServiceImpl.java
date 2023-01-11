package com.softknife.release.jira.service;

import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.google.common.collect.Iterators;
import com.restbusters.exception.RecordNotFound;
import com.restbusters.integraton.jira.JiraHelper;
import com.softknife.release.config.AppProp;
import com.softknife.release.jira.model.SubmitIssueRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author amatsaylo on 2019-05-29
 * @project dart
 */
@Service("jiraService")
public class JiraServiceImpl {

    @Autowired
    private JiraHelper jiraHelper;

    @Autowired
    private AppProp appProp;

    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public String getJiraStatus(String jiraId) throws RecordNotFound {
        Optional<Issue> optionalIssue = jiraHelper.findIssueByIssueId(jiraId);
        if (!optionalIssue.isPresent()) {
            throw new RecordNotFound("Issue not found: " + jiraId);
        }
        return optionalIssue.get().getStatus().getName();
    }

    public Issue getIssue(String jiraId) throws RecordNotFound {
        Optional<Issue> optionalIssue = jiraHelper.findIssueByIssueId(jiraId);
        if (!optionalIssue.isPresent()) {
            throw new RecordNotFound("Issue not found: " + jiraId);
        }
        return optionalIssue.get();

    }

    public void transitionJira(String issueKey, String transitionState) {
        try {
            jiraHelper.transitIssue(issueKey, transitionState);
        } catch (com.restbusters.exception.RecordNotFound recordNotFound) {
            recordNotFound.printStackTrace();
        }
    }

    public Optional<List<Version>> getVersionByProjectName(String projectName, String versionName) {
        try {
            return Optional.of(StreamSupport.stream(this.jiraHelper.getVersionListByProject(projectName).spliterator(), false)
                    .filter(version -> version.getName().equalsIgnoreCase(versionName))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public String submitIssue(SubmitIssueRequest submitIssueRequest) {

        String projectId = appProp.getJiraDefaultProjectId();
        if(StringUtils.isNoneBlank(submitIssueRequest.getProjectId())){
            projectId = submitIssueRequest.getProjectId();
        }
        BasicComponent basicComponent = getComponent(projectId, appProp.getJiraComponents());
        //String assigneeName = jiraHelper.getProjectByProjectKey("lead").get().getLead().getName();
        IssueInput issueInput = buildIssueInput(projectId, submitIssueRequest.getDesc(), null, null,
                submitIssueRequest.getSummary(), appProp.getJiraIssueTypeId(), appProp.getJiraPriorityId(), null);
        String createdIssueId = null;
        try {
            BasicIssue basicIssue = jiraHelper.createIssue(issueInput);
            createdIssueId = basicIssue.getKey();
            logger.info(basicIssue.toString());
        } catch (Exception e) {
            logger.warn("Failed to file jira {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return createdIssueId;
    }

    public BasicComponent getComponent(String projectName, String componentName) {
        Iterable<BasicComponent> basicComponent = jiraHelper.getProjectByProjectKey(projectName).get().getComponents();
        return stream(jiraHelper.getProjectByProjectKey(projectName).get().getComponents())
                .filter(bc -> componentName.equals(bc.getName()))
                .findAny()
                .orElse(null);
    }

    private <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iterable.iterator(),
                        Spliterator.ORDERED
                ),
                false
        );
    }

    public IssueInput buildIssueInput(String projectKey, String description, @Nullable String assignee, @Nullable BasicComponent basicComponent, String summary,
                                      @Nonnull Long issueTypeId, @Nullable Long priorityId, @Nullable Iterable<String> versions) {

        IssueInputBuilder builder = new IssueInputBuilder();
        builder.setProjectKey(projectKey)
                .setDescription(description)
                .setIssueTypeId(issueTypeId)
                .setSummary(summary);

        if (basicComponent != null) {
            builder.setComponents(basicComponent);
        }
        if (versions != null && Iterators.size(versions.iterator()) > 0) {
            builder.setAffectedVersionsNames(versions);
        }
        if (priorityId != null) {
            builder.setPriorityId(priorityId);
        }

        if (StringUtils.isNoneBlank(assignee))
            builder.setAssigneeName(assignee);

        return builder.build();

    }

}

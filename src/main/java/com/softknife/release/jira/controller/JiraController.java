package com.softknife.release.jira.controller;


import com.atlassian.jira.rest.client.api.domain.Issue;
import com.restbusters.exception.RecordNotFound;
import com.softknife.release.jira.model.SubmitIssueRequest;
import com.softknife.release.jira.service.JiraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author amatsaylo on 2019-06-29
 * @project release-notes
 */
@RestController
public class JiraController {

    @Autowired
    private JiraServiceImpl jiraService;

    @PutMapping(value = "/issue/{issueKey}/transition/{transitionState}")
    public ResponseEntity<Void> transitionState(@PathVariable String issueKey, @PathVariable String transitionState) {

        jiraService.transitionJira(issueKey.trim(), transitionState.trim());
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/issue/submit")
    public ResponseEntity<Void> submitIssue(@RequestBody final SubmitIssueRequest submitIssueRequest) {

        jiraService.submitIssue(submitIssueRequest);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{jiraId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCommitsInRange(@PathVariable String jiraId) throws RecordNotFound {
        return jiraService.getJiraStatus(jiraId);
    }

    @GetMapping(value = "/{jiraId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Issue getIssue(@PathVariable String jiraId) throws RecordNotFound {
        return jiraService.getIssue(jiraId);
    }
}

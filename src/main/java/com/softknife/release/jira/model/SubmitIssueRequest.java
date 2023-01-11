package com.softknife.release.jira.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class SubmitIssueRequest {

    private String desc;
    private String summary;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String projectId;

}

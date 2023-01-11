package com.softknife.release.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author alexander matsaylo on2/23/22
 * @project release-notes
 */
@ConfigurationProperties("app")
@Getter
@Setter
public class AppProp {

    private String swaggerHost;
    private String swaggerHostDefault;
    private String deployEnv;
    private Long watchDogTimeOut;
    private Long execHandlerTimeOut;
    private String slackWebHook;
    private String jiraUrl;
    private String jiraUser;
    private String jiraPassword;
    private String gitHubAuthToken;
    private String jiraComponents;
    private Long jiraIssueTypeId;
    private Long jiraPriorityId;
    private String jiraDefaultProjectId;

}

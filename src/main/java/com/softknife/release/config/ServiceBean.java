package com.softknife.release.config;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.seratch.jslack.Slack;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.restbusters.data.templating.TemplateManager;
import com.restbusters.integraton.jira.JiraHelper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author alexander matsaylo on 2/24/22
 * @project release-notes
 */
@Configuration
public class ServiceBean {

    private final AppProp appProp;

    public ServiceBean(AppProp appProp) {
        this.appProp = appProp;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JiraRestClient jiraClient() throws URISyntaxException {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(new URI(appProp.getJiraUrl()), appProp.getJiraUser(), appProp.getJiraPassword());
    }

    @Bean
    public com.jayway.jsonpath.Configuration jsonPathConfigProvider() {
        return com.jayway.jsonpath.Configuration.builder()
                .jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();
    }

    @Bean
    public TemplateManager templateManager(){
        String[] extension = { "ftl", "json" };
        return new TemplateManager("src/main/resources/template", extension, true, ";", "=");
    }

    @Bean(name = "gitHub")
    public GitHub gitHubServer() throws IOException {
        return GitHub.connectUsingOAuth(appProp.getGitHubAuthToken());
    }

    @Bean
    public JiraHelper jiraHelper() throws Exception {
        JiraHelper.getInstance().init(appProp.getJiraUrl(), appProp.getJiraUser(), appProp.getJiraPassword());
        return JiraHelper.getInstance();
    }


    @Profile("!local")
    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        if (StringUtils.startsWith(appProp.getSwaggerHost(),"${")) {
            server.setUrl(appProp.getSwaggerHostDefault());
            server.setDescription("Generated By OpenApiBean read as default value, provided env: " + appProp.getDeployEnv() );
        } else {
            server.setUrl(appProp.getSwaggerHost());
            server.setDescription("Generated By OpenApiBean read as env var, provided env: " + appProp.getDeployEnv());
        }
        return new OpenAPI().servers(List.of(server));
    }

    @Bean
    public Slack slackMethodClient() {
        return Slack.getInstance();
    }

}

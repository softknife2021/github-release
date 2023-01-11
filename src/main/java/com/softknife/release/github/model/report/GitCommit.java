package com.softknife.release.github.model.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GitCommit {

	private String commitMessage;
	private String author;
	private String commitUrl;
	private String effectedFile;
	private String gitSha;
	private String committedDate;
	private String authorIcon;
	private String jiraId;
	private String jiraStatus;

}

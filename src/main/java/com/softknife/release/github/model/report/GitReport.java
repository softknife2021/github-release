package com.softknife.release.github.model.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode()
public class GitReport {

	
	private List<GitCommit> gitCommits;
	private String orgName;
	private String totalCommits;
	private String absentReason;
	private String repoName;

}

package com.softknife.release.github.model.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Image{

	@JsonProperty("gitModule")
	private List<String> gitModule;

	@JsonProperty("tag")
	private String tag;

	@JsonProperty("repository")
	private String repository;

	@JsonProperty("gitRepo")
	private String gitRepo;

	@JsonProperty("pullPolicy")
	private String pullPolicy;

	@JsonProperty("pullSecret")
	private String pullSecret;
}
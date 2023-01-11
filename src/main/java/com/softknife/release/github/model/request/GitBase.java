package com.softknife.release.github.model.request;

import lombok.Data;

import java.util.List;

@Data
public class GitBase {

    private String orgName;
    private List<String> repoNames;

}

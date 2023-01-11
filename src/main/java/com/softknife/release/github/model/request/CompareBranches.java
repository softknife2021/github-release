package com.softknife.release.github.model.request;


import lombok.Data;


@Data
public class CompareBranches extends GitBase{

    private String sourceBranch;
    private String targetBranch;
}

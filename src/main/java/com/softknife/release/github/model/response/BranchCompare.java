package com.softknife.release.github.model.response;

import lombok.Data;

@Data
public class BranchCompare {

    private String repoName;
    private String sourceBranch;
    private String targetBranch;
    private String status;
    private int aheadBy;
    private int behind_by;
}

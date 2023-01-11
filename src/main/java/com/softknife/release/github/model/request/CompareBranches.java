package com.softknife.release.github.model.request;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CompareBranches extends GitBase{

    private String sourceBranch;
    private String targetBranch;
}

package com.softknife.release.github.model.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.softknife.release.github.service.ReportType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CommitsSince extends GitBase{

    private String branchName;
    @JsonSetter(nulls = Nulls.SKIP)
    private int daysBack = 0;
    @JsonSetter(nulls = Nulls.SKIP)
    private int hoursBack = 0;
    private ReportType reportType;
    private List<String> pathExclusion;
}
package com.softknife.release.github.model.request;

import com.softknife.release.github.service.ReportType;
import lombok.Data;

import java.util.List;

@Data
public class CommitsSinceUntil extends GitBase{

    private String branchName;
    private String startDate;
    private String untilDate;
    private ReportType reportType;
    private List<String> pathExclusion;
}

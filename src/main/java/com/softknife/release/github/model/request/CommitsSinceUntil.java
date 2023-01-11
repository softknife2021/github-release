package com.softknife.release.github.model.request;

import com.softknife.release.github.service.ReportType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CommitsSinceUntil extends GitBase{

    private String branchName;
    private String startDate;
    private String untilDate;
    private ReportType reportType;
    private List<String> pathExclusion;
}

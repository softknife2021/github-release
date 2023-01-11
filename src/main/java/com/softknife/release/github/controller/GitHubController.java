package com.softknife.release.github.controller;

import com.softknife.release.github.model.request.CommitsSince;
import com.softknife.release.github.model.request.CommitsSinceUntil;
import com.softknife.release.github.model.request.CompareBranches;
import com.softknife.release.github.model.response.BranchCompare;
import com.softknife.release.github.service.GitHubServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author amatsaylo on 2019-05-10
 * @project release-notes
 */
@RestController
@RequestMapping("/github")
public class GitHubController {

    @Resource
    private GitHubServiceImpl gitHubService;


    @PostMapping(value = "/compare")
    public ResponseEntity<List<BranchCompare>> compareBranches(@RequestBody final CompareBranches compareBranches) {

        return new ResponseEntity<List<BranchCompare>>(gitHubService.compareBranches(compareBranches),HttpStatus.OK);
    }

    @PostMapping(value = "/commits/since")
    public ResponseEntity<String> getCommits(@RequestBody final CommitsSince commitsSince) throws IOException {

            return new ResponseEntity<String>(gitHubService.getCommitsFromBranch(commitsSince), HttpStatus.OK);
    }

    @PostMapping(value = "/commits/range")
    public ResponseEntity<String> getCommits(@RequestBody final CommitsSinceUntil commitsSinceUntil) throws IOException {

        return new ResponseEntity<String>(gitHubService.getCommitsInTimeRange(commitsSinceUntil), HttpStatus.OK);
    }
}

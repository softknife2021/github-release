#!/usr/bin/env bash

docker run -it --rm \
-e APP_USER=release-notes \
-e APP_PASS='encoded password' \
-e JIRA_URL='jira url' \
-e JIRA_USER='jira user' \
-e GITHUB_AUTH_TOKEN='toke' \
-e JIRA_PASS='jira pass' \
-p 8080:8080 \
release-notes

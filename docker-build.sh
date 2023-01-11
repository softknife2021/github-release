#!/usr/bin/env bash

./gradlew build
DOCKER_BUILDKIT=1 docker build -t release-notes -f Dockerfile .
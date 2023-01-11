FROM amazoncorretto:11-alpine-jdk

RUN apk --no-cache add curl
RUN apk add gcompat

RUN addgroup -g 1000 app \
    && adduser --home /home/app --uid 1000 -G app --shell /bin/sh --disabled-password app
USER app
WORKDIR /home/app

RUN mkdir /home/app/config
COPY --chown=app ./src/main/resources /home/app/src/main/resources
COPY --chown=app ./entrypoint.sh /home/app/entrypoint.sh
COPY --chown=app ./build/libs/github-release-0.0.1-SNAPSHOT.jar /home/app/app.jar

ENTRYPOINT ["/home/app/entrypoint.sh"]
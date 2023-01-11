#!/bin/sh

XMS=${CQ_XMS:-2g}
XMX=${CQ_XMX:-2g}


PROFILE="${ENV:=local}"
SPRING_PROFILES_ACTIVE=${PROFILE} exec java \
-Xms${XMS} -Xmx${XMX} \
-Duser.timezone=UTC \
-Dlog4j2.formatMsgNoLookups=true \
-cp /home/app/config/:app.jar -jar /home/app/app.jar \
--spring.config.additional-location=/home/app/config/ \
--spring.profiles.active=${PROFILE}
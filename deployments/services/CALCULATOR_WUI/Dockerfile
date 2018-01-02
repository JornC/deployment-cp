FROM maven:3.5-jdk-8

ARG prId=local
ENV prId ${prId}
ARG oAuthToken=local
ENV oAuthToken ${oAuthToken}

RUN apt update \
    && apt install -y \
    git

RUN mkdir AERIUS-II
WORKDIR AERIUS-II

RUN git init \
    && git pull https://${oAuthToken}@github.com/aerius/AERIUS-II.git \
    && git remote add origin https://${oAuthToken}@github.com/aerius/AERIUS-II.git \
    && mvn dependency:resolve -Pcalculator -fn

RUN git fetch origin pull/${prId}/head:PR-${prId} \
    && git checkout PR-${prId}

RUN mvn install -Pcalculator -DskipTests

RUN mv aerius-wui-calculator-server/target/calculator##*.war /artifact.war

WORKDIR /
RUN rm -rf AERIUS-II

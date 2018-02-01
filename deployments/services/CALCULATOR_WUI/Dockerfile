# Create a mostly-cacheable builder container that builds the pull request
FROM maven:3.5-jdk-8 as builder

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

ARG prId=local
ENV prId ${prId}

RUN git fetch origin pull/${prId}/head:PR-${prId} \
    && git checkout PR-${prId}

RUN mvn install -Pcalculator -DskipTests

RUN mv aerius-wui-calculator-server/target/calculator##*.war /artifact.war

# Clean up the build container and start from a tomcat container, copying the artifact
FROM tomcat:9-jre8-alpine

#RUN rm -rf /usr/local/tomcat/webapps/

RUN apk add --update \
    unzip \
    postgresql-client

COPY --from=builder /artifact.war /calculator.war
RUN mkdir /usr/local/tomcat/webapps/calculator
RUN unzip /calculator.war -d /usr/local/tomcat/webapps/calculator/

COPY context.xml /usr/local/tomcat/webapps/calculator/META-INF/context.xml
COPY ./wait-for-postgres.sh wait-for-postgres.sh

COPY tomcat-users.xml /usr/local/tomcat/conf/
COPY manager.xml /usr/local/tomcat/conf/Catalina/localhost/
COPY context-manager.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
COPY context-manager.xml /usr/local/tomcat/webapps/host-manager/META-INF/context.xml

EXPOSE 8009

LABEL nl.aerius.docker.service=true
LABEL nl.aerius.docker.service.type=CALCULATOR_WUI
LABEL nl.aerius.docker.service.prId={{cp.pr.id}}
LABEL nl.aerius.docker.service.hash={{service.local.hash}}

CMD ["catalina.sh", "run"]

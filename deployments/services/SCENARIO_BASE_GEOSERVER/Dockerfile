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

RUN mvn install -Pgeoserver-calculator -DskipTests

RUN mv aerius-geoserver-calculator/target/geoserver-calculator##*.war /artifact.war

# Clean up the build container and start from a tomcat container, copying the artifact
FROM tomcat:9-jre8-alpine

#RUN rm -rf /usr/local/tomcat/webapps/

RUN apk add --update \
    unzip \
    postgresql-client \
    ttf-dejavu

#RUN apt update -y \
#    && apt install unzip \
#    postgresql -y
ARG geoserverPassword=local
ENV geoserverPassword ${geoserverPassword}

COPY --from=builder /artifact.war /geoserver-calculator.war
RUN mkdir /usr/local/tomcat/webapps/geoserver-calculator
RUN unzip /geoserver-calculator.war -d /usr/local/tomcat/webapps/geoserver-calculator/

COPY context.xml /usr/local/tomcat/webapps/geoserver-calculator/META-INF/context.xml
COPY logging.xml /usr/local/tomcat/webapps/geoserver-calculator/data/logging.xml

COPY tomcat-users.xml /usr/local/tomcat/conf/
COPY ./wait-for-postgres.sh wait-for-postgres.sh
COPY manager.xml /usr/local/tomcat/conf/Catalina/localhost/
COPY context-manager.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
COPY context-manager.xml /usr/local/tomcat/webapps/host-manager/META-INF/context.xml

RUN mkdir -p /etc/tomcat
RUN echo "${geoserverPassword}" > /etc/tomcat/geoserverpasswd

COPY users.xml /usr/local/tomcat/webapps/geoserver-calculator/security/usergroup/default/users.xml

EXPOSE 8009

LABEL nl.aerius.docker.service=true
LABEL nl.aerius.docker.service.type=SCENARIO_BASE_GEOSERVER
LABEL nl.aerius.docker.service.prId={{cp.pr.id}}
LABEL nl.aerius.docker.service.hash={{service.local.hash}}


CMD ["catalina.sh", "run"]

# Create a mostly-cacheable builder container that builds the pull request
FROM maven:3.5-jdk-8 as builder

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

# Clean up the build container and start from a tomcat container, copying the artifact
FROM tomcat:jre8-alpine

#RUN rm -rf /usr/local/tomcat/webapps/

RUN apk add --update \
    unzip

COPY --from=builder /artifact.war /calculator.war
RUN mkdir /usr/local/tomcat/webapps/calculator
RUN unzip /calculator.war -d /usr/local/tomcat/webapps/calculator/

COPY context.xml /usr/local/tomcat/webapps/calculator/META-INF/context.xml

COPY tomcat-users.xml /usr/local/tomcat/conf/

CMD ["catalina.sh", "run"]

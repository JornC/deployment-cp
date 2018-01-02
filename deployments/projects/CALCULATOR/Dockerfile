FROM tomcat:8.0-jre8

RUN rm -rf /usr/local/tomcat/webapps/

COPY artifact.war /usr/local/tomcat/webapps/ROOT.war
COPY context.xml /usr/local/tomcat/webapps/META-INF/context.xml

CMD ["catalina.sh", "run"]

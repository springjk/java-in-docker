ARG JDK_VERSION=8

FROM tomcat:9.0.8-jre${JDK_VERSION}-slim

VOLUME /usr/local/tomcat/logs

CMD /usr/local/tomcat/bin/catalina.sh run

EXPOSE 8080

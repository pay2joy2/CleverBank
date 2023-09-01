FROM tomcat:latest

RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
COPY ./build/libs/CleverBank2-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
COPY ./build /usr/local/tomact/webapps/

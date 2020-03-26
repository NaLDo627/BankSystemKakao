FROM gradle:5.6.4-jdk11 as compile
COPY . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle bootWar

FROM tomcat:8.5.46-jdk8-openjdk
RUN rm -Rf /usr/local/tomcat/webapps/ROOT ## tomcat root 경로 삭제

EXPOSE 8080
COPY --from=compile /home/gradle/project/build/libs/bank-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
# COPY ./build/libs/bank-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
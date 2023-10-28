FROM openjdk:17
MAINTAINER qingmumu
ADD /target/Dating-Community-1.0.jar Dating.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Dating.jar"]
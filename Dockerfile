FROM openjdk:17
MAINTAINER susu
ADD /target/Blog-system-0.0.1-SNAPSHOT.jar blog.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","blog.jar"]
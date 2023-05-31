FROM openjdk:17-oracle
MAINTAINER joeabala
WORKDIR /opt/tracking
COPY target/iotatrackingservice-0.0.1-SNAPSHOT.jar ./
EXPOSE 8080
CMD java -jar iotatrackingservice-0.0.1-SNAPSHOT.jar

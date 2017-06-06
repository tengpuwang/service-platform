FROM java:8

MAINTAINER shumin<shumin@chianc.com>

# Install wget
#RUN apt-get update && apt-get install -y curl

WORKDIR /tengpu

# Install maven
RUN wget http://apache.fayea.com/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
#COPY lib/apache-maven-3.3.9-bin.tar.gz /tengpu/lib/apache-maven-3.3.9-bin.tar.gz
#COPY lib/m2.tar.gz /tengpu/lib/m2.tar.gz

#RUN tar -zxf /tengpu/lib/apache-maven-3.3.9-bin.tar.gz -C /opt
RUN tar -zxf apache-maven-3.3.9-bin.tar.gz -C /opt
#RUN tar -zxf /tengpu/lib/m2.tar.gz -C /root
RUN mkdir -p /root/.m2 && mkdir -p /web-api

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH /opt/apache-maven-3.3.9/bin:$JAVA_HOME/bin:$PATH

# Prepare by downloading dependencies
ADD settings.xml /root/.m2
 
ADD pom.xml /tengpu/pom.xml
ADD common/pom.xml  /tengpu/common/pom.xml
ADD web-api/pom.xml  /tengpu/web-api/pom.xml

#RUN ["mvn", "dependency:resolve"]
#RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD common/src/main  /tengpu/common/src/main
ADD web-api/src/main  /tengpu/web-api/src/main

RUN ["mvn", "package"]

RUN mv /tengpu/web-api/target/tengp-web-api.jar /web-api &&  mv /tengpu/web-api/target/lib /web-api
RUN rm -rf /tengpu && rm -rf /root/.m2 && rm -rf /opt/apache-maven-3.3.9

EXPOSE 8000
CMD ["java","-Xms256m","-Xmx512m", "-jar", "/web-api/tengp-web-api.jar"]
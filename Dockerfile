FROM maven:3.5-jdk-8
#tomcat:8.5.93-jdk8
ADD ./ /app
WORKDIR /app
COPY mvnsetting/settings.xml /usr/share/maven/conf/settings.xml
#RUN apt-get update && \
#    apt-get install -y curl wget \
#RUN mvn clean package
EXPOSE 8000

CMD ["mvn","tomcat7:run"]
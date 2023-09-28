FROM maven:3.5-jdk-8

ADD ./ /app
WORKDIR /app
#RUN apt-get update && \
#    apt-get install -y curl wget \

EXPOSE 8000
CMD ["mvn","tomcat7:run"]
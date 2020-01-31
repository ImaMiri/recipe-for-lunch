# Recipe for lunch
An API that provides recipes bases on available ingredients. The application has been developed using Java 11, Maven 4, springboot 2.2.4


## Quick start
I. To build the application using Maven, use the below command:

$ mvn clean package

II. Run the application 

$ mvn spring-boot:run

III. To fetch the outcome use the following command:

$ curl -X GET http://localhost:8080/lunch


### Build and run using docker

Install the docker if you haven't already, https://docs.docker.com/install/

I. Build the docker image as below:

$ docker build -t recipe:latest .
 
II. Run the application using docker image

$ docker run -p 8080:8080 -t recipe:latest

III. To fetch the outcome use the following command:

$ curl -X GET http://localhost:8080/lunch

IV.To stop the docker image use the below command

$ docker rm $(docker stop $(docker ps -a -q --filter ancestor=recipe:latest --format="{{.ID}}"))
